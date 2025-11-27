package co.peanech.mpos.core.common.ui.logo

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val logo = remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    LaunchedEffect(Unit) {
        try {
            context.assets.open("ic_mpos_logo.png").use { stream ->
                val bmp = BitmapFactory.decodeStream(stream)
                logo.value = bmp.asImageBitmap()
            }
        } catch (_: Exception) {
        }
    }

    logo.value?.let { img ->
        Image(bitmap = img, contentDescription = "MPos logo", modifier = modifier.size(48.dp), contentScale = ContentScale.Crop)
    }
}
