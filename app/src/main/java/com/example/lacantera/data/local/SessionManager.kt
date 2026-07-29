package com.example.lacantera.data.local



import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore(
    name = "user_session"
)

class SessionManager(
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

        val ROL: Preferences.Key<String> =
            stringPreferencesKey("rol")

        val IS_LOGGED_IN: Preferences.Key<Boolean> =
            booleanPreferencesKey("is_logged_in")
    }

    val accessToken: Flow<String?> =
        context.sessionDataStore.data.map { preferences ->
            preferences[Keys.ACCESS_TOKEN]
        }

    val refreshToken: Flow<String?> =
        context.sessionDataStore.data.map { preferences ->
            preferences[Keys.REFRESH_TOKEN]
        }

    val isLoggedIn: Flow<Boolean> =
        context.sessionDataStore.data.map { preferences ->
            preferences[Keys.IS_LOGGED_IN] ?: false
        }

    val userSession: Flow<UserSession> =
        context.sessionDataStore.data.map { preferences ->
            UserSession(
                userId = preferences[Keys.USER_ID],
                username = preferences[Keys.USERNAME].orEmpty(),
                nombreCorto = preferences[Keys.NOMBRE_CORTO].orEmpty(),
                rol = preferences[Keys.ROL].orEmpty(),
                accessToken = preferences[Keys.ACCESS_TOKEN],
                refreshToken = preferences[Keys.REFRESH_TOKEN],
                isLoggedIn = preferences[Keys.IS_LOGGED_IN] ?: false
            )
        }

    suspend fun saveSession(
        accessToken: String,
        refreshToken: String,
        userId: Int,
        username: String,
        nombreCorto: String,
        rol: String
    ) {
        context.sessionDataStore.edit { preferences ->
            preferences[Keys.ACCESS_TOKEN] = accessToken
            preferences[Keys.REFRESH_TOKEN] = refreshToken
            preferences[Keys.USER_ID] = userId
            preferences[Keys.USERNAME] = username
            preferences[Keys.NOMBRE_CORTO] = nombreCorto
            preferences[Keys.ROL] = rol
            preferences[Keys.IS_LOGGED_IN] = true
        }
    }

    suspend fun updateAccessToken(accessToken: String) {
        context.sessionDataStore.edit { preferences ->
            preferences[Keys.ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

data class UserSession(
    val userId: Int?,
    val username: String,
    val nombreCorto: String,
    val rol: String,
    val accessToken: String?,
    val refreshToken: String?,
    val isLoggedIn: Boolean
)