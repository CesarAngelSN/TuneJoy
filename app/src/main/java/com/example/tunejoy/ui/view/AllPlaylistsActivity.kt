package com.example.tunejoy.ui.view

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import kotlinx.coroutines.runBlocking

@Composable
fun AllPlaylistsActivity(navController: NavController, innerPadding: PaddingValues) {
    val firestoreService = FirestoreService()
    val playlistList = mutableStateListOf<Playlist>()
    runBlocking {
        playlistList.addAll(firestoreService.getPlaylists())
    }
    Column (
        Modifier
            .fillMaxWidth()
            .padding(innerPadding)){
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Playlists", Modifier.padding(start = 20.dp), fontWeight = FontWeight.Bold, fontSize = 35.sp)
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(playlistList.size) { index ->
                Card (onClick = {
                    navController.navigate("playlistactivity/${playlistList[index].getId()}")
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
                        Box (
                            Modifier
                            .height(60.dp)
                            .width(60.dp), contentAlignment = Alignment.Center){
                            Image(painter = painterResource(
                                id = R.drawable.baseline_playlist_play_24),
                                contentDescription = null,
                                Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop)
                        }
                        Column (Modifier.padding(start = 12.dp)){
                            Text(
                                playlistList[index].getName(), Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}