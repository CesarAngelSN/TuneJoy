package com.example.tunejoy.ui.view

import android.media.browse.MediaBrowser.MediaItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.tunejoy.R
import kotlinx.coroutines.delay

@Composable
fun IconActivity(navController: NavHostController) {
    Box (
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.logotunejoydef), contentDescription = "logo", Modifier.scale(1.3f, 1.3f))
    }
    LaunchedEffect(true) {
        delay(2000)
        navController.navigate("loginactivity"){
            popUpTo("iconactivity") {
                inclusive = true
            }
        }
    }
}

