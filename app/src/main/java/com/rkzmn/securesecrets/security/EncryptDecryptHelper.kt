package com.rkzmn.securesecrets.security

import android.util.Base64
import android.util.Log
import com.cossacklabs.themis.SecureCell
import com.cossacklabs.themis.SecureCellData

/**
 * Encrypt/Decrypt helper methods using Themis (Android)
 *
 * Reference : https://medium.com/proandroiddev/protecting-secrets-in-an-android-project-ff99eaf7b9ec
 */
object EncryptDecryptHelper {

    private const val TAG = "Encryption"

    fun generateKey(encryptionKey: String): ByteArray {
        val rawKey = buildString(5) {
            append(byteArrayOf(0x12, 0x27, 0x42).base64EncodedString())
            append(500 + 6 / 7 * 89)
            append(encryptionKey)
            append("pghy^%£aft")
        }
        return rawKey.toByteArray()
    }

    fun encrypt(message: String, key: String): ByteArray {
        val encryptionKey = generateKey(key)
        val cell = SecureCell.SealWithKey(encryptionKey)
        return cell.encrypt(message.toByteArray())
    }

    fun decrypt(encryptedData: ByteArray, key: String): String {
        val encryptionKey = generateKey(key)
        try {
            val cell = SecureCell.SealWithKey(encryptionKey)
            val cellData = SecureCellData(encryptedData, null)
            val decodedData = cell.decrypt(cellData.protectedData)
            return String(decodedData)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to decrypt", e)
            throw e
        }
    }

    private fun ByteArray.base64EncodedString() =
        Base64.encodeToString(this, Base64.NO_WRAP)

}