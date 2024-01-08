package com.example.tunejoy.ui.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tunejoy.R
import com.example.tunejoy.database.FirestoreService
import com.example.tunejoy.model.Artist
import com.example.tunejoy.model.Playlist
import com.example.tunejoy.model.Song
import com.example.tunejoy.ui.viewmodel.ExoPlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

@Composable
fun HomeActivity(applicationContext: Context, innerPadding: PaddingValues, navController: NavController, exoPlayerViewModel: ExoPlayerViewModel = viewModel()) {
    val scope = rememberCoroutineScope()
    val firestoreService = FirestoreService()
    val songList = mutableStateListOf<Song>()
    val artistList = mutableStateListOf<Artist>()
    val albumList = mutableStateListOf<String>()
    val playlistList = mutableStateListOf<Playlist>()
    runBlocking{
        //en principio no hace falta ni el async ni el await, pero a una mala se lo clavo
        songList.addAll(firestoreService.getItems())
        artistList.addAll(firestoreService.getArtists())
        albumList.addAll(firestoreService.getAlbums())
        playlistList.addAll(firestoreService.getPlaylists())
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())) {
        Column {
            Text(text = "Artists", Modifier.padding(start = 20.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            LazyRow(contentPadding = PaddingValues(8.dp)) {
                items(artistList.size) {
                    CardElement(type = "artist", text = artistList[it].getName()) {
                        navController.navigate("artistactivity/${artistList[it].getId()}")
                    }
                }
            }
        }
        Column {
            Text(text = "Albums", Modifier.padding(start = 20.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            LazyRow(contentPadding = PaddingValues(8.dp)) {
                items(albumList.size) {
                    CardElement(type = "album", text = albumList[it]) {
                        navController.navigate("albumactivity/${albumList[it]}")
                    }
                }

            }
        }
        Column {
            Text(text = "Playlists", Modifier.padding(start = 20.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            LazyRow(contentPadding = PaddingValues(8.dp)) {
                items(playlistList.size) {
                    CardElement(type = "playlist", text = playlistList[it].getName()) {
                        navController.navigate("playlistactivity/${playlistList[it].getId()}")
                    }
                }

            }
        }
    }
}

@Composable
fun CardElement(type: String, text: String, onNavigate: () -> Unit) {
    Card (
        onClick = {
            onNavigate()
        },
        modifier = Modifier
            .height(190.dp)
            .padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )){
        Column (
            Modifier
                .width(150.dp)
                .height(150.dp), Arrangement.SpaceAround, Alignment.CenterHorizontally){
            if (type == "playlist") {
                Icon(painter = painterResource(id = R.drawable.baseline_playlist_play_24),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp))
            }
            else {
                Image(
                    painter = painterResource(if (type == "artist") {
                        if(text.contains("Jonas")) {
                            R.drawable.jonasartistphoto
                        }
                        else if (text.contains("Muse")) {
                            R.drawable.museartistphoto
                        }
                        else {
                            R.drawable.jonasartistphoto
                        }
                    }
                    else if (type == "album") {
                        if (text == "A Little Bit Longer") {
                            R.drawable.jonaslittlebitlonger
                        }
                        else if (text == "Jonas Brothers") {
                            R.drawable.jonasfivealbums
                        }
                        else if (text == "Absolution") {
                            R.drawable.museabsolution
                        }
                        else if (text == "Drones") {
                            R.drawable.musedrones
                        }
                        else {
                            R.drawable.jonasfivealbums
                        }
                    }
                    else {
                        R.drawable.jonasartistphoto
                    }),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(
                            if (type == "artist") {
                                CircleShape
                            } else {
                                RoundedCornerShape(8.dp)
                            }
                        )
                )
            }

            Text(text = text)
        }
    }
}

fun <T> List<T>.randomElement(): T {
    return this[Random.nextInt(size)]
}

/*fun getRandomColor(): Color {
    val colorList = listOf(Color.Green, Color.Blue, Color.Red, Color.Green, Color.Gray, Color.Magenta, Color.Yellow)
    return colorList.randomElement()
}*/