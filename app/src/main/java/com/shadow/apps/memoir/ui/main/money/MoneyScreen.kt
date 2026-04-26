package com.shadow.apps.memoir.ui.main.money

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
fun MoneyScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Money",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Preview(name = "Money – Light", showBackground = true, showSystemUi = true)
@Composable
private fun MoneyLightPreview() {
    ShadowMemoirTheme(darkTheme = false) { MoneyScreen() }
}

@Preview(name = "Money – Dark", showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MoneyDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) { MoneyScreen() }
}
