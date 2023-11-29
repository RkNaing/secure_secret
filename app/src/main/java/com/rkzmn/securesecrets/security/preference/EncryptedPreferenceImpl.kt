package com.rkzmn.securesecrets.security.preference

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class EncryptedPreferenceImpl(
    context: Context,
    masterKey: MasterKey,
    private val dispatcher: CoroutineContext = Dispatchers.IO,
) : EncryptedPreference {

    private val encryptedPreferences = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun save(message: String) {
        withContext(dispatcher) {
            encryptedPreferences.edit {
                putString(PREF_SECRET_MESSAGE, message)
            }
        }
    }

    override suspend fun get(): String {
        return withContext(dispatcher) {
            encryptedPreferences.getString(PREF_SECRET_MESSAGE, "") ?: ""
        }
    }

    override suspend fun delete() {
        withContext(dispatcher) {
            encryptedPreferences.edit {
                remove(PREF_SECRET_MESSAGE)
            }
        }
    }

    companion object {
        private const val FILE_NAME = "secure_shared_preferences"
        private const val PREF_SECRET_MESSAGE = "secret_message"
    }
}