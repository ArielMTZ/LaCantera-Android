package com.example.lacantera.data.remote

import android.content.Context
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.TokenRefreshRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    context: Context
) : Authenticator {

    private val sessionManager = SessionManager(
        context = context.applicationContext
    )

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {

        // Evita un ciclo infinito cuando la petición reintentada
        // también recibe un error 401.
        if (responseCount(response) >= MAX_ATTEMPTS) {
            clearSession()
            return null
        }

        val failedAccessToken = response.request
            .header(AUTHORIZATION_HEADER)
            ?.removePrefix(BEARER_PREFIX)
            ?.trim()

        /*
         * Solo una petición puede renovar el token a la vez.
         * Las demás esperan y después reutilizan el token ya renovado.
         */
        return synchronized(refreshLock) {
            runBlocking {
                val currentAccessToken =
                    sessionManager.accessToken.first()

                /*
                 * Mientras esta petición esperaba, otra pudo haber renovado
                 * el token. En ese caso no hacemos otro refresh.
                 */
                if (
                    !currentAccessToken.isNullOrBlank() &&
                    currentAccessToken != failedAccessToken
                ) {
                    return@runBlocking buildAuthorizedRequest(
                        originalRequest = response.request,
                        accessToken = currentAccessToken
                    )
                }

                val refreshToken =
                    sessionManager.refreshToken.first()

                if (refreshToken.isNullOrBlank()) {
                    sessionManager.clearSession()
                    return@runBlocking null
                }

                try {
                    val refreshResponse =
                        RefreshTokenClient.api.refreshToken(
                            request = TokenRefreshRequest(
                                refresh = refreshToken
                            )
                        )

                    if (!refreshResponse.isSuccessful) {
                        refreshResponse.errorBody()?.close()
                        sessionManager.clearSession()
                        return@runBlocking null
                    }

                    val tokenResponse =
                        refreshResponse.body()

                    val newAccessToken =
                        tokenResponse?.access

                    if (newAccessToken.isNullOrBlank()) {
                        sessionManager.clearSession()
                        return@runBlocking null
                    }

                    sessionManager.updateTokens(
                        accessToken = newAccessToken,
                        refreshToken = tokenResponse.refresh
                    )

                    buildAuthorizedRequest(
                        originalRequest = response.request,
                        accessToken = newAccessToken
                    )
                } catch (_: Exception) {
                    /*
                     * Un error de conexión no significa necesariamente que
                     * el refresh token sea inválido, así que no borramos
                     * la sesión en este caso.
                     */
                    null
                }
            }
        }
    }

    private fun buildAuthorizedRequest(
        originalRequest: Request,
        accessToken: String
    ): Request {
        return originalRequest
            .newBuilder()
            .header(
                AUTHORIZATION_HEADER,
                "$BEARER_PREFIX$accessToken"
            )
            .build()
    }

    private fun clearSession() {
        runBlocking {
            sessionManager.clearSession()
        }
    }

    private fun responseCount(
        response: Response
    ): Int {
        var count = 1
        var previousResponse = response.priorResponse

        while (previousResponse != null) {
            count++
            previousResponse = previousResponse.priorResponse
        }

        return count
    }

    private companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer "
        const val MAX_ATTEMPTS = 2

        val refreshLock = Any()
    }
}