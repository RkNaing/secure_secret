package com.rkzmn.securesecrets.security.file

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.charset.StandardCharsets
import kotlin.coroutines.CoroutineContext

class EncryptedFileSystemImpl(
    context: Context,
    masterKey: MasterKey,
    private val dispatcher: CoroutineContext = Dispatchers.IO,
) : EncryptedFileSystem {

    private val secretFile = File(context.filesDir, FILE_NAME)

    private val encryptedFile = EncryptedFile.Builder(
        context,
        secretFile,
        masterKey,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    override suspend fun save(message: String) {
        withContext(dispatcher) {
            delete()
            if (message.isNotBlank()) {
                encryptedFile.openFileOutput().use { fos ->
                    fos.write(message.toByteArray(StandardCharsets.UTF_8))
                    fos.flush()
                }
            }
        }
    }

    override suspend fun get(): String {
        return withContext(dispatcher) {
            runCatching {
                encryptedFile.openFileInput().use { fis ->
                    fis.bufferedReader().use { reader ->
                        reader.readText()
                    }
                }
            }.getOrDefault("")
        }
    }

    override suspend fun delete() {
        withContext(dispatcher) {
            if (secretFile.exists()) {
                secretFile.delete()
            }
        }
    }

    companion object {
        private const val FILE_NAME = "secret_file.txt"
    }
}