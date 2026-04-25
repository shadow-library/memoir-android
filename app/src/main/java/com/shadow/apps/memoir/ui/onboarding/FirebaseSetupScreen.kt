package com.shadow.apps.memoir.ui.onboarding

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shadow.apps.memoir.R
import com.shadow.apps.memoir.ui.onboarding.components.PageDots
import com.shadow.apps.memoir.ui.theme.Cyan5
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme
import com.shadow.apps.memoir.ui.theme.Slate0
import com.shadow.apps.memoir.ui.theme.Slate1
import com.shadow.apps.memoir.ui.theme.Slate6
import com.shadow.apps.memoir.ui.theme.Slate7
import com.shadow.apps.memoir.ui.theme.Slate8
import com.shadow.apps.memoir.ui.theme.Slate9
import com.shadow.apps.memoir.ui.theme.Violet200
import com.shadow.apps.memoir.ui.theme.Violet500

/**
 * Setup step 2 of 3 (Primary) — lets the user connect their Firebase project by
 * uploading a `google-services.json` file or entering the credential fields manually.
 */
@Composable
fun FirebaseSetupScreen(onBack: () -> Unit, onContinue: () -> Unit, viewModel: FirebaseSetupViewModel = hiltViewModel()) {
    BackHandler(onBack = onBack)
    val uiState by viewModel.uiState.collectAsState()

    FirebaseSetupContent(
        uiState = uiState,
        onBack = onBack,
        onContinue = { viewModel.saveAndContinue(onContinue) },
        onFileUploaded = viewModel::onFileUploaded,
        onProjectIdChanged = viewModel::onProjectIdChanged,
        onApiKeyChanged = viewModel::onApiKeyChanged,
        onAppIdChanged = viewModel::onAppIdChanged,
        onStorageBucketChanged = viewModel::onStorageBucketChanged,
        onDatabaseUrlChanged = viewModel::onDatabaseUrlChanged,
    )
}

@Composable
private fun FirebaseSetupContent(
    uiState: FirebaseSetupUiState,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onFileUploaded: (String) -> Unit,
    onProjectIdChanged: (String) -> Unit,
    onApiKeyChanged: (String) -> Unit,
    onAppIdChanged: (String) -> Unit,
    onStorageBucketChanged: (String) -> Unit,
    onDatabaseUrlChanged: (String) -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    val background = if (isDark) {
        Modifier.background(Brush.verticalGradient(listOf(Slate9, Slate8)))
    } else {
        Modifier.background(Slate0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(background),
    ) {
        Box(modifier = Modifier.weight(1f)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(48.dp))

            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(onClick = onBack, color = Color.Transparent) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.back),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f),
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.firebase_setup_step),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.50f),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.firebase_setup_headline),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.firebase_setup_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            )

            Spacer(Modifier.height(24.dp))

            UploadCard(
                onFileUploaded = onFileUploaded,
                uploadError = uiState.uploadError,
                isDark = isDark,
            )

            Spacer(Modifier.height(20.dp))

            // Divider with inline label
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                )
                Text(
                    text = stringResource(R.string.firebase_setup_or_manual),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f),
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                )
            }

            Spacer(Modifier.height(20.dp))

            FieldsSection(
                projectId = uiState.projectId,
                apiKey = uiState.apiKey,
                appId = uiState.appId,
                storageBucket = uiState.storageBucket,
                databaseUrl = uiState.databaseUrl,
                onProjectIdChanged = onProjectIdChanged,
                onApiKeyChanged = onApiKeyChanged,
                onAppIdChanged = onAppIdChanged,
                onStorageBucketChanged = onStorageBucketChanged,
                onDatabaseUrlChanged = onDatabaseUrlChanged,
            )

            Spacer(Modifier.height(20.dp))

            HintCard(isDark = isDark)

            Spacer(Modifier.height(24.dp))
        }

            // Fade edge — content dissolves before reaching the footer
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                if (isDark) Slate8 else Slate0,
                            ),
                        ),
                    ),
            )
        }

        // Footer: dots + continue button
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PageDots(total = 5, current = 2)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onContinue,
                enabled = uiState.isValid && !uiState.isSaving,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.firebase_setup_continue),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(Modifier.height(36.dp))
        }
    }
}


@Composable
private fun UploadCard(
    onFileUploaded: (String) -> Unit,
    uploadError: String?,
    isDark: Boolean,
) {
    val context = LocalContext.current
    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val content = runCatching {
            context.contentResolver.openInputStream(uri)?.bufferedReader()?.readText()
        }.getOrNull()
        if (content != null) onFileUploaded(content)
    }

    Surface(
        onClick = { filePicker.launch(arrayOf("application/json", "text/plain", "*/*")) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = if (isDark) Slate7 else Color.White,
        border = BorderStroke(width = 1.dp, color = if (isDark) Slate6 else Slate1),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.UploadFile,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = Cyan5,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.firebase_setup_upload_title),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.firebase_setup_upload_desc),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                textAlign = TextAlign.Center,
            )
        }
    }

    if (uploadError != null) {
        Spacer(Modifier.height(6.dp))
        Text(
            text = uploadError,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
        )
    }
}


@Composable
private fun FieldsSection(
    projectId: String,
    apiKey: String,
    appId: String,
    storageBucket: String,
    databaseUrl: String,
    onProjectIdChanged: (String) -> Unit,
    onApiKeyChanged: (String) -> Unit,
    onAppIdChanged: (String) -> Unit,
    onStorageBucketChanged: (String) -> Unit,
    onDatabaseUrlChanged: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = projectId,
            onValueChange = onProjectIdChanged,
            label = { Text(stringResource(R.string.firebase_setup_field_project_id)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
        )
        OutlinedTextField(
            value = apiKey,
            onValueChange = onApiKeyChanged,
            label = { Text(stringResource(R.string.firebase_setup_field_api_key)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
        )
        OutlinedTextField(
            value = appId,
            onValueChange = onAppIdChanged,
            label = { Text(stringResource(R.string.firebase_setup_field_app_id)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
        )
        OutlinedTextField(
            value = storageBucket,
            onValueChange = onStorageBucketChanged,
            label = { Text(stringResource(R.string.firebase_setup_field_storage_bucket)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
        )
        OutlinedTextField(
            value = databaseUrl,
            onValueChange = onDatabaseUrlChanged,
            label = { Text(stringResource(R.string.firebase_setup_field_database_url)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
        )
    }
}


@Composable
private fun HintCard(isDark: Boolean) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Violet200.copy(alpha = if (isDark) 0.10f else 0.18f),
        border = BorderStroke(
            width = 1.dp,
            color = Violet200.copy(alpha = if (isDark) 0.20f else 0.35f),
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = Violet500,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.firebase_setup_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
            )
        }
    }
}


@Preview(name = "FirebaseSetup – Light", showBackground = true, showSystemUi = true)
@Composable
private fun FirebaseSetupLightPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        FirebaseSetupContent(
            uiState = FirebaseSetupUiState(),
            onBack = {}, onContinue = {}, onFileUploaded = {},
            onProjectIdChanged = {}, onApiKeyChanged = {},
            onAppIdChanged = {}, onStorageBucketChanged = {}, onDatabaseUrlChanged = {},
        )
    }
}

@Preview(name = "FirebaseSetup – Dark", showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FirebaseSetupDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) {
        FirebaseSetupContent(
            uiState = FirebaseSetupUiState(),
            onBack = {}, onContinue = {}, onFileUploaded = {},
            onProjectIdChanged = {}, onApiKeyChanged = {},
            onAppIdChanged = {}, onStorageBucketChanged = {}, onDatabaseUrlChanged = {},
        )
    }
}

@Preview(name = "FirebaseSetup – Filled", showBackground = true, showSystemUi = true)
@Composable
private fun FirebaseSetupFilledPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        FirebaseSetupContent(
            uiState = FirebaseSetupUiState(
                projectId = "shadow-memoir-12345",
                apiKey = "AIzaSyB-xxxxx",
                appId = "1:123456:android:abcdef",
                storageBucket = "shadow-memoir-12345.appspot.com",
            ),
            onBack = {}, onContinue = {}, onFileUploaded = {},
            onProjectIdChanged = {}, onApiKeyChanged = {},
            onAppIdChanged = {}, onStorageBucketChanged = {}, onDatabaseUrlChanged = {},
        )
    }
}
