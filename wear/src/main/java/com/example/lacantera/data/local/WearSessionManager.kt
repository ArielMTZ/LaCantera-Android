package com.example.lacantera.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lacantera.data.model.WearSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.wearSessionDataStore by preferencesDataStore(
    name = "wear_user_session"
)

class WearSessionManager(
    private val context: Context
) {

    private object Keys {
        val ACCESS_TOKEN: Preferences.Key<String> =
            stringPreferencesKey("access_token")

        val REFRESH_TOKEN: Preferences.Key<String> =
            stringPreferencesKey("refresh_token")

        val USER_ID: Preferences.Key<Int> =
            intPreferencesKey("user_id")

        val USERNAME: Preferences.Key<String> =
            stringPreferencesKey("username")

        val NOMBRE_CORTO: Preferences.Key<String> =
            stringPreferencesKey("nombre_corto")

        val TIPO_USUARIO: Preferences.Key<String> =
            stringPreferencesKey("tipo_usuario")

        val IS_LOGGED_IN: Preferences.Key<Boolean> =
            booleanPreferencesKey("is_logged_in")
    }

    val session: Flow<WearSession> =
        context.wearSessionDataStore.data.map { preferences ->
            WearSession(
                userId = preferences[Keys.USER_ID],
                username = preferences[Keys.USERNAME].orEmpty(),
                nombreCorto =
                    preferences[Keys.NOMBRE_CORTO].orEmpty(),
                tipoUsuario =
                    preferences[Keys.TIPO_USUARIO].orEmpty(),
                accessToken =
                    preferences[Keys.ACCESS_TOKEN],
                refreshToken =
                    preferences[Keys.REFRESH_TOKEN],
                isLoggedIn =
                    preferences[Keys.IS_LOGGED_IN]
                        ?: false
            )
        }

    suspend fun saveSession(
        userId: Int,
        username: String,
        nombreCorto: String,
        tipoUsuario: String,
        accessToken: String,
        refreshToken: String
    ) {
        context.wearSessionDataStore.edit { preferences ->
            preferences[Keys.USER_ID] = userId
            preferences[Keys.USERNAME] = username
            preferences[Keys.NOMBRE_CORTO] =
                nombreCorto
            preferences[Keys.TIPO_USUARIO] =
                tipoUsuario
            preferences[Keys.ACCESS_TOKEN] =
                accessToken
            preferences[Keys.REFRESH_TOKEN] =
                refreshToken
            preferences[Keys.IS_LOGGED_IN] = true
        }
    }

    suspend fun updateAccessToken(
        accessToken: String
    ) {
        context.wearSessionDataStore.edit { preferences ->
            preferences[Keys.ACCESS_TOKEN] =
                accessToken
        }
    }

    suspend fun clearSession() {
        context.wearSessionDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}