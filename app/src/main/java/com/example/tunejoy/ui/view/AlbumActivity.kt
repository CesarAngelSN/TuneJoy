package com.example.tunejoy.ui.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tunejoy.R
import com.example.tunejoy.database.FirestoreService
import com.example.tunejoy.model.Playlist
import com.example.tunejoy.model.Song
import com.example.tunejoy.ui.viewmodel.ExoPlayerViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun AlbumActivity(navController: NavController, innerPadding: PaddingValues, album: String?,
                  exoPlayerViewModel: ExoPlayerViewModel,
                  applicationContext: Context) {
    println(album)
    val firestoreService = FirestoreService()
    val songs = mutableStateListOf<Song>()
    runBlocking {
        if (album != null) {
            songs.addAll(firestoreService.getSongsByAlbum(album))
        }
    }

    Column (
        Modifier
            .fillMaxSize()
            .padding(innerPadding),
        Arrangement.Top, Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(20.dp))
        Box (
            Modifier
                .width(320.dp)
                .height(320.dp),
            contentAlignment = Alignment.Center){

            if (album != null) {
                Image(painter = painterResource(id = if (album == "A Little Bit Longer") {
                    R.drawable.jonaslittlebitlonger
                }
                else if (album == "Jonas Brothers") {
                    R.drawable.jonasfivealbums
                }
                else if (album == "Absolution") {
                    R.drawable.museabsolution
                }
                else if (album == "Drones") {
                    R.drawable.musedrones
                }
                else {
                    R.drawable.jonasfivealbums
                }),
                    contentDescription = null,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop)
            }
        }
        Column (Modifier.fillMaxWidth()){
            if (album != null) {
                Text(text = album, Modifier.padding(20.dp), fontSize = 35.sp, fontWeight = FontWeight.Bold)
            }
            Text(text = "Songs", Modifier.padding(start = 20.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(songs.size) { index ->
                    Card (onClick = {
                        navController.navigate("songactivity/${songs[index].getId()}")
                        exoPlayerViewModel.playMusic(applicationContext, songs, songs[index])
                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )){
                        Row (
                            Modifier
                                .height(60.dp),
                            Arrangement.Start, Alignment.CenterVertically){
                            Image(painter = painterResource(
                                id = if(songs[index].getArtist().contains("Jonas")) {
                                    R.drawable.jonasartistphoto
                                }
                                else if (songs[index].getArtist().contains("Muse")) {
                                    R.drawable.museartistphoto
                                }
                                else {
                                    R.drawable.jonasartistphoto
                                }),
                                contentDescription = null)
                            Column (Modifier.padding(start = 12.dp)){
                                Text(
                                    songs[index].getName()
                                )
                                Text(
                                    songs[index].getArtist()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}