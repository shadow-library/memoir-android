package com.shadow.apps.memoir.ui.main.more

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme

@Composable
fun MoreScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "More",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Preview(name = "More – Light", showBackground = true, showSystemUi = true)
@Composable
private fun MoreLightPreview() {
    ShadowMemoirTheme(darkTheme = false) { MoreScreen() }
}

@Preview(name = "More – Dark", showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MoreDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) { MoreScreen() }
}
