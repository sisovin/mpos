package com.peanech.mpos.ui.splash

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Displays a full-screen background image loaded from assets/launcher.png
 * and the circular MPos logo from assets/ic_mpos_logo.png at the center.
 * After the delay, it runs [onTimeout].
 */
@Composable
fun SplashScreen(onTimeout: () -> Unit, timeoutMillis: Long = 2000L) {
    val context = LocalContext.current
    val backgroundBitmap = remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    val logoBitmap = remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }

    // Load assets lazily
    LaunchedEffect(Unit) {
        try {
            context.assets.open("launcher.png").use { stream ->
                val bmp = BitmapFactory.decodeStream(stream)
                backgroundBitmap.value = bmp.asImageBitmap()
            }
        } catch (_: Exception) {
            // ignore — show plain background
        }
        try {
            context.assets.open("ic_mpos_logo.png").use { stream ->
                val bmp = BitmapFactory.decodeStream(stream)
                logoBitmap.value = bmp.asImageBitmap()
            }
        } catch (_: Exception) {
            // ignore
        }
        delay(timeoutMillis)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        backgroundBitmap.value?.let { bg ->
            Image(bitmap = bg, contentDescription = "Splash background", modifier = Modifier.fillMaxSize())
        }

        logoBitmap.value?.let { l ->
            Image(bitmap = l, contentDescription = "MPos logo", modifier = Modifier.size(200.dp))
        }
    }
}
