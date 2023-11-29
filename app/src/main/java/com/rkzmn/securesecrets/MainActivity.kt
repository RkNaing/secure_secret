package com.rkzmn.securesecrets

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.security.crypto.MasterKey
import com.rkzmn.securesecrets.presentation.SecretInputFormViewModel
import com.rkzmn.securesecrets.presentation.SecretsInputFormScreen
import com.rkzmn.securesecrets.security.EncryptDecryptHelper
import com.rkzmn.securesecrets.security.Secrets
import com.rkzmn.securesecrets.security.getEncryptedFileSystem
import com.rkzmn.securesecrets.security.getEncryptedSharedPreference
import com.rkzmn.securesecrets.ui.theme.SecureSecretsTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SecretInputFormViewModel> {
        val masterKey = createMasterKey()
        val encryptedFileSystem = getEncryptedFileSystem(
            context = applicationContext,
            masterKey = masterKey
        )
        val encryptedPreference = getEncryptedSharedPreference(
            context = applicationContext,
            masterKey = masterKey
        )
        SecretInputFormViewModel.Factory(encryptedFileSystem, encryptedPreference)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        encryptionTest()
        Log.d(TAG, "onCreate: Native Secret is ${Secrets.getAPIKey()}")
        setContent {
            SecureSecretsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SecretsInputFormScreen(viewModel = viewModel)
                }
            }
        }
    }

    private fun createMasterKey(): MasterKey {
        // StrongBox is available only Android P afterward
        val useStrongBox = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

        return MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .setUserAuthenticationRequired(hasSecureLock(), 10)
            .setRequestStrongBoxBacked(useStrongBox)
            .build()
    }

    private fun encryptionTest() {
        val encryptionKey = "EncryptionKey"
//        val encryptedValue = EncryptDecryptHelper.encrypt(message = secretValue, key = encryptionKey)
//        Log.d(TAG, "encryptionTest: Encrypted Value ${encryptedValue.joinToString()}")
//        val secretValue = "SomethingSecret"

        val encryptedValue = byteArrayOf(
            0,
            1,
            1,
            64,
            12,
            0,
            0,
            0,
            16,
            0,
            0,
            0,
            15,
            0,
            0,
            0,
            -83,
            123,
            -35,
            110,
            109,
            92,
            -44,
            45,
            -7,
            101,
            -52,
            74,
            -23,
            -44,
            44,
            -78,
            46,
            -79,
            122,
            -3,
            -102,
            58,
            122,
            89,
            -2,
            73,
            106,
            -53,
            -68,
            -9,
            -20,
            66,
            84,
            52,
            2,
            54,
            0,
            -86,
            -110,
            63,
            54,
            -70,
            112
        )
        val decryptedValue =
            EncryptDecryptHelper.decrypt(encryptedData = encryptedValue, key = encryptionKey)
        Log.d(TAG, "encryptionTest: Decrypted Value => $decryptedValue")

    }

    private fun hasSecureLock(): Boolean {
        val keyguardManager =
            getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager ?: return false
        return keyguardManager.isKeyguardSecure.also {
            Log.d(
                TAG,
                "hasSecureLock() returned: $it"
            )
        }
    }
}

private const val TAG = "MainActivity"