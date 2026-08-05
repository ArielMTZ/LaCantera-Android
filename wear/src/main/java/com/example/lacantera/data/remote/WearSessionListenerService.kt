package com.example.lacantera.data.remote

import com.example.lacantera.data.local.WearSessionManager
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject

class WearSessionListenerService :
    WearableListenerService() {

    private val serviceScope =
        CoroutineScope(
            SupervisorJob() + Dispatchers.IO
        )

    override fun onMessageReceived(
        messageEvent: MessageEvent
    ) {
        super.onMessageReceived(messageEvent)

        when (messageEvent.path) {
            SESSION_PATH -> {
                receiveSession(
                    payload = messageEvent.data
                )
            }

            LOGOUT_PATH -> {
                clearSession()
            }
        }
    }

    private fun receiveSession(
        payload: ByteArray
    ) {
        serviceScope.launch {
            try {
                val jsonText = payload.toString(
                    Charsets.UTF_8
                )

                val json = JSONObject(jsonText)

                val userId = json.getInt(
                    "user_id"
                )

                val username = json.getString(
                    "username"
                )

                val nombreCorto = json.optString(
                    "nombre_corto",
                    username
                )

                val tipoUsuario = json.getString(
                    "tipo_usuario"
                )

                val accessToken = json.getString(
                    "access_token"
                )

                val refreshToken = json.getString(
                    "refresh_token"
                )

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
                    WearSessionManager(
                        applicationContext
                    ).clearSession()

                    return@launch
                }

                WearSessionManager(
                    applicationContext
                ).saveSession(
                    userId = userId,
                    username = username,
                    nombreCorto = nombreCorto,
                    tipoUsuario = tipoUsuario,
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            } catch (exception: Exception) {
                WearSessionManager(
                    applicationContext
                ).clearSession()
            }
        }
    }

    private fun clearSession() {
        serviceScope.launch {
            WearSessionManager(
                applicationContext
            ).clearSession()
        }
    }

    companion object {
        const val SESSION_PATH =
            "/lacantera/session"

        const val LOGOUT_PATH =
            "/lacantera/logout"
    }
}