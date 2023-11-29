package com.rkzmn.securesecrets.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rkzmn.securesecrets.ui.theme.SecureSecretsTheme
import com.rkzmn.securesecrets.utils.app.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecretsInputFormScreen(viewModel: SecretInputFormViewModel) {

    val fileSecretsFormState by viewModel.fileSecretFormState.collectAsStateWithLifecycle()
    val prefSecretsFormState by viewModel.prefSecretFormState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = AppStrings.app_name)) })
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SecretsInputForm(
                state = fileSecretsFormState,
                formTitle = stringResource(id = AppStrings.title_file_secret),
                onSecretChanged = viewModel::onFileSecretChanged,
                onClickedSaveSecret = viewModel::onClickedSaveFileSecret,
                onClickedDeleteSecret = viewModel::onClickedDeleteFileSecret,
                onToggleReveal = viewModel::onFileSecretRevealToggle
            )

            SecretsInputForm(
                state = prefSecretsFormState,
                formTitle = stringResource(id = AppStrings.title_pref_secret),
                onSecretChanged = viewModel::onPrefSecretChanged,
                onClickedSaveSecret = viewModel::onClickedSavePrefSecret,
                onClickedDeleteSecret = viewModel::onClickedDeletePrefSecret,
                onToggleReveal = viewModel::onPrefSecretRevealToggle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SecretsInputForm(
    state: FormState,
    formTitle: String,
    modifier: Modifier = Modifier,
    onSecretChanged: (String) -> Unit,
    onClickedSaveSecret: () -> Unit,
    onClickedDeleteSecret: () -> Unit,
    onToggleReveal: (Boolean) -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = formTitle,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.enteredSecret,
            onValueChange = onSecretChanged
        )

        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            onClick = {
                onClickedSaveSecret()
                keyboardController?.hide()
            }
        ) {
            Text(text = stringResource(id = AppStrings.lbl_save))
        }

        if (state.showSecret) {
            if (state.revealedSecret.isBlank()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = AppStrings.msg_no_saved_secret)
                )
            } else {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.revealedSecret
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.showSecret) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onToggleReveal(false) }
                ) {
                    Text(text = stringResource(id = AppStrings.lbl_hide))
                }
            } else {
                ElevatedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onToggleReveal(true) }
                ) {
                    Text(text = stringResource(id = AppStrings.lbl_reveal))
                }
            }

            IconButton(onClick = onClickedDeleteSecret) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Secret"
                )
            }
        }

    }
}

@Preview
@Composable
fun SecretInputFormPreview() {
    SecureSecretsTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SecretsInputForm(
                state = FormState(
                    enteredSecret = "mucius",
                    revealedSecret = "",
                    isLoading = false,
                    showSecret = false
                ),
                formTitle = stringResource(id = AppStrings.title_file_secret),
                onSecretChanged = {},
                onClickedSaveSecret = { /*TODO*/ },
                onClickedDeleteSecret = { /*TODO*/ },
                onToggleReveal = {}
            )
        }
    }
}