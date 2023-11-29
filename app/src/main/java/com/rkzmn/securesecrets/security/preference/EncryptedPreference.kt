package com.rkzmn.securesecrets.security.preference

interface EncryptedPreference {
    suspend fun save(message: String)

    suspend fun get(): String

    suspend fun delete()
}