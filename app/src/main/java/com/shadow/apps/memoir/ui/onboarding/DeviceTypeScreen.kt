package com.shadow.apps.memoir.ui.onboarding

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadow.apps.memoir.R
import com.shadow.apps.memoir.ui.onboarding.components.PageDots
import com.shadow.apps.memoir.ui.theme.Cyan5
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme
import com.shadow.apps.memoir.ui.theme.Slate0
import com.shadow.apps.memoir.ui.theme.Slate1
import com.shadow.apps.memoir.ui.theme.Slate2
import com.shadow.apps.memoir.ui.theme.Slate5
import com.shadow.apps.memoir.ui.theme.Slate6
import com.shadow.apps.memoir.ui.theme.Slate7
import com.shadow.apps.memoir.ui.theme.Slate8
import com.shadow.apps.memoir.ui.theme.Slate9


/**
 * Setup step 1 of 3 — lets the user choose between creating a new Firebase
 * vault (primary device) or pairing with an existing one via QR code (secondary device).
 */
@Composable
fun DeviceTypeScreen(onBack: () -> Unit, onSetupNewVault: () -> Unit, onPairExistingVault: () -> Unit) {
    BackHandler(onBack = onBack)
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
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(48.dp))

            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    onClick = onBack,
                    color = Color.Transparent,
                ) {
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
                text = stringResource(R.string.device_type_setup_step),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.50f),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.device_type_headline),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.device_type_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            )

            Spacer(Modifier.height(32.dp))

            VaultOptionCard(
                icon = Icons.Outlined.Storage,
                iconBg = Cyan5,
                iconTint = Color.White,
                title = stringResource(R.string.device_type_primary_title),
                badgeLabel = stringResource(R.string.device_type_primary_badge),
                badgeColor = MaterialTheme.colorScheme.primary,
                description = stringResource(R.string.device_type_primary_desc),
                onClick = onSetupNewVault,
            )

            Spacer(Modifier.height(16.dp))

            VaultOptionCard(
                icon = Icons.Outlined.QrCodeScanner,
                iconBg = if (isDark) Slate6 else Slate1,
                iconTint = if (isDark) Slate2 else Slate5,
                title = stringResource(R.string.device_type_secondary_title),
                badgeLabel = stringResource(R.string.device_type_secondary_badge),
                badgeColor = MaterialTheme.colorScheme.onSurfaceVariant,
                description = stringResource(R.string.device_type_secondary_desc),
                onClick = onPairExistingVault,
            )

            Spacer(Modifier.height(24.dp))
        }

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PageDots(total = 5, current = 1)
            Spacer(Modifier.height(36.dp))
        }
    }
}


@Composable
private fun VaultOptionCard(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    badgeLabel: String,
    badgeColor: Color,
    description: String,
    onClick: () -> Unit,
) {
    val isDark = isSystemInDarkTheme()

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isDark) Slate7 else Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (isDark) Slate6 else Slate1,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(color = iconBg, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp),
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = badgeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 1.sp,
                        color = badgeColor,
                    )
                }

                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f),
                    modifier = Modifier.size(20.dp),
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            )
        }
    }
}


@Preview(
    name = "DeviceType – Light",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun DeviceTypeLightPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        DeviceTypeScreen(onBack = {}, onSetupNewVault = {}, onPairExistingVault = {})
    }
}

@Preview(
    name = "DeviceType – Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
)
@Composable
private fun DeviceTypeDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) {
        DeviceTypeScreen(onBack = {}, onSetupNewVault = {}, onPairExistingVault = {})
    }
}
