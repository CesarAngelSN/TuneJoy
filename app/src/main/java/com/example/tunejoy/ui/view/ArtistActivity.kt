package com.example.tunejoy.ui.view

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.tunejoy.model.Artist
import com.example.tunejoy.model.Song
import com.example.tunejoy.ui.viewmodel.ExoPlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun ArtistActivity(navController: NavController, innerPadding: PaddingValues, artistId: String?,
                   exoPlayerViewModel: ExoPlayerViewModel,
                   applicationContext: Context) {
    val firestoreService = FirestoreService()
    var artist by remember { mutableStateOf<Artist?>(null) }
    val songList = mutableStateListOf<Song>()
    runBlocking {
        if (artistId != null) {
            artist = firestoreService.getArtistById(artistId)
            songList.addAll(firestoreService.getSongsByArtist(artist!!.getName()))
        }
    }
    var alpha by remember {
        mutableStateOf(1f)
    }
    var infoActive by remember {
        mutableStateOf(false)
    }
    var opacity by remember {
        mutableStateOf(1f)
    }

    Column (
        Modifier
            .fillMaxSize()
            .padding(innerPadding),
        Arrangement.Top,
        Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.height(20.dp))
        Box (
            Modifier
                .width(320.dp)
                .height(320.dp),
            contentAlignment = Alignment.Center){
            Image(
                painter = painterResource(id = if (artist?.getName() == "Jonas Brothers") {
                    R.drawable.jonasartistphoto
                }
                else if (artist?.getName() == "Muse") {
                    R.drawable.museartistphoto
                }
                else {
                    R.drawable.jonasartistphoto
                }),
                contentDescription = "",
                Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = opacity)
            if (infoActive) {
                Column (Modifier.verticalScroll(rememberScrollState())){
                    if (artist != null) {
                        Text(text = artist!!.getDescription())
                    }
                }
            }
        }
        Column (Modifier.fillMaxWidth()){
            if (artist != null) {
                Row (Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically){
                    Text(text = artist!!.getName(), Modifier.padding(20.dp), fontSize = 35.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {
                        infoActive = !infoActive
                        opacity = if (infoActive) {
                            0.2f
                        } else {
                            1f
                        }
                    }) {
                        Icon(painterResource(id = R.drawable.baseline_info_24), contentDescription = null,
                            Modifier
                                .size(110.dp)
                                .padding(end = 20.dp))
                    }
                }
            }
            Text(text = "Songs", Modifier.padding(start = 20.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(songList.size) { index ->
                    Card (onClick = {
                        navController.navigate("songactivity/${songList[index].getId()}")
                        exoPlayerViewModel.playMusic(applicationContext, songList, songList[index])
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
                                id = if(songList[index].getArtist().contains("Jonas")) {
                                    R.drawable.jonasartistphoto
                                }
                                else if (songList[index].getArtist().contains("Muse")) {
                                    R.drawable.museartistphoto
                                }
                                else {
                                    R.drawable.jonasartistphoto
                                }),
                                contentDescription = null)
                            Column (Modifier.padding(start = 12.dp)){
                                Text(
                                    songList[index].getName()
                                )
                                Text(
                                    songList[index].getArtist()
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}