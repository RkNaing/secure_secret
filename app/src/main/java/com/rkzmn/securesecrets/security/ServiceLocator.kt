package com.rkzmn.securesecrets.security

import android.content.Context
import androidx.security.crypto.MasterKey
import com.rkzmn.securesecrets.security.file.EncryptedFileSystem
import com.rkzmn.securesecrets.security.file.EncryptedFileSystemImpl
import com.rkzmn.securesecrets.security.preference.EncryptedPreference
import com.rkzmn.securesecrets.security.preference.EncryptedPreferenceImpl

fun getEncryptedFileSystem(context: Context, masterKey: MasterKey): EncryptedFileSystem {
    return EncryptedFileSystemImpl(context, masterKey)
}

fun getEncryptedSharedPreference(context: Context, masterKey: MasterKey): EncryptedPreference {
    return EncryptedPreferenceImpl(context, masterKey)
}