package com.rkzmn.securesecrets.security.file

interface EncryptedFileSystem {

    suspend fun save(message: String)

    suspend fun get(): String

    suspend fun delete()

}