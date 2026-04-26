package com.shadow.apps.memoir.ui.onboarding.accountsetup

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shadow.apps.memoir.R
import com.shadow.apps.memoir.ui.components.AppScreen
import com.shadow.apps.memoir.ui.theme.Cyan5
import com.shadow.apps.memoir.ui.theme.Emerald500
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme

@Composable
fun AccountSetupScreen(
    onContinue: () -> Unit,
    viewModel: AccountSetupViewModel = hiltViewModel(),
) {
    BackHandler(enabled = true) {}
    val uiState by viewModel.uiState.collectAsState()

    AccountSetupContent(
        uiState = uiState,
        onContinue = onContinue,
        onRetry = viewModel::retry,
    )
}

@Composable
private fun AccountSetupContent(
    uiState: AccountSetupUiState,
    onContinue: () -> Unit,
    onRetry: () -> Unit,
) {
    val showFooter = uiState.phase == AccountSetupUiState.Phase.Success || uiState.error != null

    AppScreen(
        footer = if (showFooter) {
            {
                if (uiState.error != null) {
                    PrimaryCtaButton(label = "Try again", onClick = onRetry)
                } else {
                    PrimaryCtaButton(label = stringResource(R.string.setup_complete_cta), onClick = onContinue)
                }
                Spacer(Modifier.height(48.dp))
            }
        } else null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(1f))

            AnimatedContent(
                targetState = when {
                    uiState.error != null -> Stage.Error
                    uiState.phase == AccountSetupUiState.Phase.Success -> Stage.Success
                    else -> Stage.Loading
                },
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "AccountSetupStage",
            ) { stage ->
                when (stage) {
                    Stage.Loading -> LoadingState()
                    Stage.Success -> SuccessState()
                    Stage.Error -> ErrorState(message = uiState.error.orEmpty())
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}

private enum class Stage { Loading, Success, Error }

@Composable
private fun LoadingState() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(color = Emerald500, strokeWidth = 3.dp)

        Spacer(Modifier.height(28.dp))

        Text(
            text = "FINAL TOUCHES",
            style = MaterialTheme.typography.labelSmall,
            letterSpacing = 1.5.sp,
            color = Emerald500,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Setting things up…",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SuccessState() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(color = Emerald500.copy(alpha = 0.12f), shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(color = Emerald500, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp),
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.setup_complete_label),
            style = MaterialTheme.typography.labelSmall,
            letterSpacing = 1.5.sp,
            color = Emerald500,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.setup_complete_headline),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.setup_complete_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ErrorState(message: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PrimaryCtaButton(label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Cyan5,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
        }
    }
}

/**
 * Previews
 */

@Preview(name = "AccountSetup – Loading", showBackground = true, showSystemUi = true)
@Composable
private fun AccountSetupLoadingPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        AccountSetupContent(
            uiState = AccountSetupUiState(phase = AccountSetupUiState.Phase.Loading),
            onContinue = {},
            onRetry = {},
        )
    }
}

@Preview(name = "AccountSetup – Success Light", showBackground = true, showSystemUi = true)
@Composable
private fun AccountSetupSuccessLightPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        AccountSetupContent(
            uiState = AccountSetupUiState(phase = AccountSetupUiState.Phase.Success),
            onContinue = {},
            onRetry = {},
        )
    }
}

@Preview(name = "AccountSetup – Success Dark", showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AccountSetupSuccessDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) {
        AccountSetupContent(
            uiState = AccountSetupUiState(phase = AccountSetupUiState.Phase.Success),
            onContinue = {},
            onRetry = {},
        )
    }
}

@Preview(name = "AccountSetup – Error", showBackground = true, showSystemUi = true)
@Composable
private fun AccountSetupErrorPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        AccountSetupContent(
            uiState = AccountSetupUiState(error = "Network unreachable."),
            onContinue = {},
            onRetry = {},
        )
    }
}
