package com.rayan.rayantube.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rayan.rayantube.BuildConfig
import com.rayan.rayantube.R

@Composable
fun SettingsScreen(
    darkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.dark_mode), style = MaterialTheme.typography.bodyLarge)
            Switch(checked = darkModeEnabled, onCheckedChange = onDarkModeChange)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.clear_cache), style = MaterialTheme.typography.bodyLarge)
            TextButton(onClick = { context.cacheDir.deleteRecursively() }) {
                Text("مسح")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.about_app), style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Rayantube v${BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.scope_note),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
