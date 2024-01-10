package com.example.tunejoy.ui.view

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunejoy.R
import com.example.tunejoy.database.FirestoreService
import com.example.tunejoy.model.Song
import com.example.tunejoy.ui.viewmodel.ExoPlayerViewModel
import com.example.tunejoy.ui.viewmodel.SongViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun SongActivity(applicationContext: Context, innerPadding: PaddingValues, songId: String?,
                 exoPlayerViewModel: ExoPlayerViewModel,
                 songViewModel: SongViewModel = viewModel()) {
    val microphoneState = songViewModel.microphoneState.collectAsState().value
    val heartState = songViewModel.heartState.collectAsState().value
    val shuffleState = songViewModel.shuffleState.collectAsState().value
    val playState = songViewModel.playState.collectAsState().value
    val loopState = songViewModel.loopState.collectAsState().value
    val sliderValue = songViewModel.sliderValue.collectAsState().value
    val exoPlayer = exoPlayerViewModel.exoPlayer.collectAsState().value
    val currentTimePassed = songViewModel.currentTimePassed.collectAsState().value
    val boxHeight = songViewModel.boxHeight.collectAsState().value
    val songList = exoPlayerViewModel.songList.collectAsState().value
    val currentSong = exoPlayerViewModel.currentSong.collectAsState().value
    val progress = exoPlayerViewModel.progress.collectAsState().value
    val duration = exoPlayerViewModel.duration.collectAsState().value
    val scrollState = rememberScrollState()

    val scope = rememberCoroutineScope()
    var song by remember { mutableStateOf<Song?>(null) }

    val firestoreService = FirestoreService()

    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val vinilRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    //REVISAAARRRRRR
    runBlocking {
        if (songId != null) {
            song = firestoreService.getSongById(songId)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(innerPadding), Arrangement.SpaceAround, Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            Modifier
                .width(320.dp)
                .height(boxHeight),
            contentAlignment = Alignment.Center
        ) {
            if (microphoneState) {
                if (playState) {
                    LaunchedEffect(Unit) {
                        scrollState.animateScrollTo(scrollState.value)
                    }
                }
                Column(Modifier.verticalScroll(scrollState)) {
                    if (song != null) {
                        Text(text = songViewModel.formatLyrics(song!!.getLyrics()))
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .width(320.dp)
                        .height(320.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                        .graphicsLayer(
                            rotationZ = if (playState) {
                                vinilRotation
                            } else {
                                0f
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(
                            id = if (song != null) {
                                if (song!!.getAlbum() == "A Little Bit Longer") {
                                    R.drawable.jonaslittlebitlonger
                                } else if (song?.getAlbum() == "Jonas Brothers") {
                                    R.drawable.jonasfivealbums
                                } else if (song?.getAlbum() == "Absolution") {
                                    R.drawable.museabsolution
                                } else if (song?.getAlbum() == "Drones") {
                                    R.drawable.musedrones
                                } else {
                                    R.drawable.jonasfivealbums
                                }
                            } else {
                                R.drawable.jonasfivealbums
                            }
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.background
                            )
                    ) {
                    }
                }

            }
        }
        Row(
            Modifier
                .width(380.dp)
                .height(150.dp),
            Arrangement.SpaceBetween, Alignment.CenterVertically
        ) {
            Column {
                if (song != null) {
                    Text(text = song!!.getName(), fontSize = 20.sp)
                    Text(text = song!!.getArtist())
                }
            }
            Row(Modifier.width(130.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                IconButton(onClick = {
                    songViewModel.setNewHeartState(!heartState)
                }) {
                    Icon(
                        if (heartState) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = null
                    )
                }
                IconButton(onClick = {

                }) {
                    Icon(
                        painterResource(id = R.drawable.baseline_playlist_add_24),
                        contentDescription = null
                    )
                }
                IconButton(onClick = {
                    songViewModel.setNewMicrophoneState(!microphoneState)
                }) {
                    Icon(
                        painterResource(
                            if (microphoneState) {
                                R.drawable.baseline_mic_24
                            } else {
                                R.drawable.baseline_mic_none_24
                            }
                        ),
                        contentDescription = null
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .width(380.dp)
                .height(70.dp), Arrangement.SpaceAround, Alignment.CenterHorizontally
        ) {
            if (exoPlayer != null) {
                if (exoPlayer.duration > -1) {
                    Slider(
                        value = progress.toFloat()/1000,
                        onValueChange = {
                            exoPlayerViewModel.setNewProgress(it.toLong())
                        },
                        valueRange = 0f..(duration/ 1000).toFloat(),
                        steps = (duration / 1000).toInt(),
                        colors = SliderDefaults.colors(
                            thumbColor = colorResource(id = R.color.dark_orange),
                            activeTrackColor = colorResource(id = R.color.light_orange),
                            inactiveTrackColor = Color.Gray
                        )
                    )
                }
            }
            Row(
                modifier = Modifier.width(380.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(text = songViewModel.formatDuration(progress))
                if (exoPlayer != null) {
                    Text(text = songViewModel.formatDuration(duration))
                }
            }
        }
        Row(
            Modifier
                .width(380.dp)
                .height(150.dp), Arrangement.SpaceBetween, Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                songViewModel.setNewShuffleState(!shuffleState)
                if (shuffleState) {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Shuffle mode activated",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Shuffle mode deactivated",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }

            }) {
                Icon(
                    painter = painterResource(
                        if (shuffleState) {
                            R.drawable.baseline_shuffle_on_24
                        } else {
                            R.drawable.baseline_shuffle_24
                        }
                    ),
                    contentDescription = null
                )
            }
            Row(
                modifier = Modifier.width(200.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    exoPlayerViewModel.changeSong(applicationContext, "normal", "prev")
                    println("Current: " + currentSong.value.toString())
                    if (currentSong.value != null) {
                        song = currentSong.value
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_skip_previous_24),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                        if (exoPlayer != null) {
                            exoPlayerViewModel.playOrPause()
                            songViewModel.setNewPlayState(exoPlayer.isPlaying)
                        }
                    },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            if (playState) {
                                R.drawable.baseline_pause_circle_outline_24
                            } else {
                                R.drawable.baseline_play_circle_outline_24
                            }
                        ),
                        contentDescription = null
                    )
                }
                IconButton(onClick = {
                    exoPlayerViewModel.changeSong(applicationContext, "normal", "next")
                    println("Current: " + currentSong.value.toString())
                    if (currentSong.value != null) {
                        println("Current dentro del if: " + currentSong.value.toString())
                        song = currentSong.value
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_skip_next_24),
                        contentDescription = null
                    )
                }
            }
            IconButton(onClick = {
                songViewModel.setNewLoopState(!loopState)
                if (loopState) {
                    exoPlayerViewModel.setLoopMode(true)
                    val toast = Toast.makeText(
                        applicationContext,
                        "Loop mode activated",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    exoPlayerViewModel.setLoopMode(false)
                    val toast = Toast.makeText(
                        applicationContext,
                        "Loop mode deactivated",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_loop_24),
                    contentDescription = null
                )
            }
        }
    }
}


