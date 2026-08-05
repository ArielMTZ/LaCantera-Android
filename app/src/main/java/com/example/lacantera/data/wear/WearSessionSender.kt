package com.example.lacantera.data.wear

import android.content.Context
import com.example.lacantera.data.model.Usuario
import com.google.android.gms.wearable.Wearable
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class WearSessionSender(
    context: Context
) {
    private val appContext =
        context.applicationContext

    private val nodeClient =
        Wearable.getNodeClient(appContext)

    private val messageClient =
        Wearable.getMessageClient(appContext)

    fun sendSession(
        usuario: Usuario,
        accessToken: String,
        refreshToken: String,
        onResult: (
            WearSendResult
        ) -> Unit = {}
    ) {
        if (
            accessToken.isBlank() ||
            refreshToken.isBlank()
        ) {
            onResult(
                WearSendResult.Error(
                    "La sesión no contiene tokens válidos."
                )
            )

            return
        }

        val tipoUsuario =
            usuario.tipoUsuario.lowercase()

        val supportedUserTypes = setOf(
            "superadmin",
            "staff",
            "admin_principal",
            "admin",
            "capitan",
            "arbitro"
        )

        if (
            tipoUsuario !in supportedUserTypes
        ) {
            onResult(
                WearSendResult.Error(
                    "Este tipo de usuario no tiene acceso al smartwatch."
                )
            )

            return
        }

        val payload = JSONObject()
            .put(
                "user_id",
                usuario.id
            )
            .put(
                "username",
                usuario.username
            )
            .put(
                "nombre_corto",
                usuario.nombreCorto
            )
            .put(
                "tipo_usuario",
                tipoUsuario
            )
            .put(
                "access_token",
                accessToken
            )
            .put(
                "refresh_token",
                refreshToken
            )
            .toString()
            .toByteArray(
                Charsets.UTF_8
            )

        nodeClient.connectedNodes
            .addOnSuccessListener { nodes ->
                if (nodes.isEmpty()) {
                    onResult(
                        WearSendResult.NoWatchConnected
                    )

                    return@addOnSuccessListener
                }

                val pendingMessages =
                    AtomicInteger(nodes.size)

                val atLeastOneSuccess =
                    AtomicBoolean(false)

                nodes.forEach { node ->
                    messageClient.sendMessage(
                        node.id,
                        SESSION_PATH,
                        payload
                    ).addOnSuccessListener {
                        atLeastOneSuccess.set(true)

                        completeIfFinished(
                            pendingMessages =
                                pendingMessages,
                            atLeastOneSuccess =
                                atLeastOneSuccess,
                            onResult = onResult
                        )
                    }.addOnFailureListener {
                        completeIfFinished(
                            pendingMessages =
                                pendingMessages,
                            atLeastOneSuccess =
                                atLeastOneSuccess,
                            onResult = onResult
                        )
                    }
                }
            }
            .addOnFailureListener { exception ->
                onResult(
                    WearSendResult.Error(
                        exception.message
                            ?: "No fue posible buscar el reloj."
                    )
                )
            }
    }

    fun sendLogout(
        onResult: (
            WearSendResult
        ) -> Unit = {}
    ) {
        nodeClient.connectedNodes
            .addOnSuccessListener { nodes ->
                if (nodes.isEmpty()) {
                    onResult(
                        WearSendResult.NoWatchConnected
                    )

                    return@addOnSuccessListener
                }

                val pendingMessages =
                    AtomicInteger(nodes.size)

                val atLeastOneSuccess =
                    AtomicBoolean(false)

                nodes.forEach { node ->
                    messageClient.sendMessage(
                        node.id,
                        LOGOUT_PATH,
                        ByteArray(0)
                    ).addOnSuccessListener {
                        atLeastOneSuccess.set(true)

                        completeIfFinished(
                            pendingMessages,
                            atLeastOneSuccess,
                            onResult
                        )
                    }.addOnFailureListener {
                        completeIfFinished(
                            pendingMessages,
                            atLeastOneSuccess,
                            onResult
                        )
                    }
                }
            }
            .addOnFailureListener { exception ->
                onResult(
                    WearSendResult.Error(
                        exception.message
                            ?: "No fue posible buscar el reloj."
                    )
                )
            }
    }

    private fun completeIfFinished(
        pendingMessages: AtomicInteger,
        atLeastOneSuccess: AtomicBoolean,
        onResult: (
            WearSendResult
        ) -> Unit
    ) {
        val remaining =
            pendingMessages.decrementAndGet()

        if (remaining != 0) {
            return
        }

        if (atLeastOneSuccess.get()) {
            onResult(
                WearSendResult.Success
            )
        } else {
            onResult(
                WearSendResult.Error(
                    "No fue posible enviar la información al reloj."
                )
            )
        }
    }

    companion object {
        const val SESSION_PATH =
            "/lacantera/session"

        const val LOGOUT_PATH =
            "/lacantera/logout"
    }
}

sealed interface WearSendResult {
    data object Success :
        WearSendResult

    data object NoWatchConnected :
        WearSendResult

    data class Error(
        val message: String
    ) : WearSendResult
}