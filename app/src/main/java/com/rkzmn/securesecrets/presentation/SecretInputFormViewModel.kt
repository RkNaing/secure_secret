package com.rkzmn.securesecrets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rkzmn.securesecrets.security.file.EncryptedFileSystem
import com.rkzmn.securesecrets.security.preference.EncryptedPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SecretInputFormViewModel(
    private val encryptedFileSystem: EncryptedFileSystem,
    private val encryptedPreference: EncryptedPreference
) : ViewModel() {

    private val _fileSecretFormState = MutableStateFlow(FormState())
    val fileSecretFormState = _fileSecretFormState.asStateFlow()

    private val _prefSecretFormState = MutableStateFlow(FormState())
    val prefSecretFormState = _prefSecretFormState.asStateFlow()


    private fun deleteSecretFile() {
        viewModelScope.launch {
            encryptedFileSystem.delete()
            _fileSecretFormState.update {
                FormState()
            }
        }
    }

    fun onFileSecretChanged(secret: String) {
        _fileSecretFormState.update { it.copy(enteredSecret = secret) }
    }

    fun onFileSecretRevealToggle(reveal: Boolean) {
        if (reveal) {
            viewModelScope.launch {
                _fileSecretFormState.update {
                    it.copy(revealedSecret = encryptedFileSystem.get(), showSecret = true)
                }
            }
        } else {
            _fileSecretFormState.update {
                it.copy(revealedSecret = "", showSecret = false)
            }
        }
    }

    fun onClickedSaveFileSecret() {
        val message = _fileSecretFormState.value.enteredSecret
        viewModelScope.launch {
            encryptedFileSystem.save(message)
            if (_fileSecretFormState.value.showSecret) {
                _fileSecretFormState.update {
                    it.copy(
                        enteredSecret = "",
                        revealedSecret = message
                    )
                }
            }
        }
    }

    fun onClickedDeleteFileSecret() {
        deleteSecretFile()
    }

    private fun deleteSharedPreference(){
        viewModelScope.launch {
            encryptedPreference.delete()
            _prefSecretFormState.update {
                FormState()
            }
        }
    }

    fun onPrefSecretChanged(secret: String) {
        _prefSecretFormState.update { it.copy(enteredSecret = secret) }
    }

    fun onPrefSecretRevealToggle(reveal: Boolean) {
        if (reveal) {
            viewModelScope.launch {
                _prefSecretFormState.update {
                    it.copy(revealedSecret = encryptedPreference.get(), showSecret = true)
                }
            }
        } else {
            _prefSecretFormState.update {
                it.copy(revealedSecret = "", showSecret = false)
            }
        }
    }

    fun onClickedSavePrefSecret() {
        val message = _prefSecretFormState.value.enteredSecret
        viewModelScope.launch {
            encryptedPreference.save(message)
            if (_prefSecretFormState.value.showSecret) {
                _prefSecretFormState.update {
                    it.copy(
                        enteredSecret = "",
                        revealedSecret = message
                    )
                }
            }
        }
    }

    fun onClickedDeletePrefSecret() {
        deleteSharedPreference()
    }

    class Factory(
        private val encryptedFileSystem: EncryptedFileSystem,
        private val encryptedPreference: EncryptedPreference
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SecretInputFormViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SecretInputFormViewModel(encryptedFileSystem,encryptedPreference) as T
            }
            return super.create(modelClass)
        }
    }

}