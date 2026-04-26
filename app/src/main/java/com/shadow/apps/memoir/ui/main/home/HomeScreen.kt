package com.shadow.apps.memoir.ui.main.home

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
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Preview(name = "Home – Light", showBackground = true, showSystemUi = true)
@Composable
private fun HomeLightPreview() {
    ShadowMemoirTheme(darkTheme = false) { HomeScreen() }
}

@Preview(name = "Home – Dark", showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun HomeDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) { HomeScreen() }
}
