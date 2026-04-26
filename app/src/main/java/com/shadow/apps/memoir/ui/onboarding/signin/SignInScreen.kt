package com.shadow.apps.memoir.ui.onboarding.signin

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shadow.apps.memoir.R
import com.shadow.apps.memoir.domain.model.StartupDestination
import com.shadow.apps.memoir.ui.components.AppScreen
import com.shadow.apps.memoir.ui.onboarding.components.PageDots
import com.shadow.apps.memoir.ui.theme.Cyan7
import com.shadow.apps.memoir.ui.theme.Emerald500
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme
import com.shadow.apps.memoir.ui.theme.Slate1
import com.shadow.apps.memoir.ui.theme.Slate6
import com.shadow.apps.memoir.ui.theme.Slate7
import com.shadow.apps.memoir.ui.theme.Slate9

/**
 * Setup step 3 of 3 (Auth) — lets the user sign in with Google via their
 * connected Firebase project. Uses Credential Manager + Firebase Auth.
 */
@Composable
fun SignInScreen(
    onBack: () -> Unit,
    onContinue: (StartupDestination) -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    BackHandler(onBack = onBack)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    SignInContent(
        uiState = uiState,
        onBack = onBack,
        onSignIn = { viewModel.signIn(context, onContinue) },
    )
}

@Composable
private fun SignInContent(
    uiState: SignInUiState,
    onBack: () -> Unit,
    onSignIn: () -> Unit,
) {
    val isDark = isSystemInDarkTheme()

    AppScreen(
        footer = {
            GoogleSignInButton(
                onClick = onSignIn,
                isLoading = uiState.isSigningIn,
                isDark = isDark,
            )

            Spacer(Modifier.height(16.dp))

            SecurityNote(isDark = isDark)

            Spacer(Modifier.height(20.dp))

            PageDots(total = 5, current = 3)

            Spacer(Modifier.height(36.dp))
        },
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(24.dp))

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
                text = stringResource(R.string.sign_in_step),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.50f),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.sign_in_headline),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.sign_in_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            )

            Spacer(Modifier.height(24.dp))

            FirebaseProjectCard(
                projectId = uiState.projectId,
                isDark = isDark,
            )

            if (uiState.error != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = uiState.error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}


@Composable
private fun FirebaseProjectCard(
    projectId: String,
    isDark: Boolean,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = if (isDark) Slate7 else Color.White,
        border = BorderStroke(width = 1.dp, color = if (isDark) Slate6 else Slate1),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Cyan7, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Bolt,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.sign_in_firebase_label),
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.50f),
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = projectId.ifBlank { "\u2014" },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Surface(
                shape = RoundedCornerShape(50),
                color = Emerald500.copy(alpha = 0.12f),
            ) {
                Text(
                    text = stringResource(R.string.sign_in_connected),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Emerald500,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }
        }
    }
}


@Composable
private fun GoogleSignInButton(onClick: () -> Unit, isLoading: Boolean, isDark: Boolean) {
    Surface(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isDark) Color.White else Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (isDark) Slate6 else Slate1,
        ),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Slate6,
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_google_logo),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.sign_in_google_button),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Slate9,
            )
        }
    }
}


@Composable
private fun SecurityNote(isDark: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isDark) Slate7.copy(alpha = 0.60f) else Slate1.copy(alpha = 0.50f),
        border = BorderStroke(
            width = 1.dp,
            color = if (isDark) Slate6.copy(alpha = 0.40f) else Slate1,
        ),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.40f),
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.sign_in_security_prefix))
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(R.string.sign_in_security_bold))
                    }
                    append(stringResource(R.string.sign_in_security_suffix))
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            )
        }
    }
}


@Preview(name = "SignIn – Light", showBackground = true, showSystemUi = true)
@Composable
private fun SignInLightPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        SignInContent(
            uiState = SignInUiState(projectId = "shadow-memoir-q42f8"),
            onBack = {},
            onSignIn = {},
        )
    }
}

@Preview(name = "SignIn – Dark", showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SignInDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) {
        SignInContent(
            uiState = SignInUiState(projectId = "shadow-memoir-q42f8"),
            onBack = {},
            onSignIn = {},
        )
    }
}

@Preview(name = "SignIn – Loading", showBackground = true, showSystemUi = true)
@Composable
private fun SignInLoadingPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        SignInContent(
            uiState = SignInUiState(projectId = "shadow-memoir-q42f8", isSigningIn = true),
            onBack = {},
            onSignIn = {},
        )
    }
}
