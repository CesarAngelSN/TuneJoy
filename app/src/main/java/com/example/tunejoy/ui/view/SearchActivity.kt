package com.example.tunejoy.ui.view

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tunejoy.R
import com.example.tunejoy.database.FirestoreService
import com.example.tunejoy.model.Song
import com.example.tunejoy.ui.viewmodel.ExoPlayerViewModel
import com.example.tunejoy.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun SearchActivity(navController: NavController, innerPadding: PaddingValues, applicationContext: Context,
                   exoPlayerViewModel: ExoPlayerViewModel,
                   searchViewModel: SearchViewModel = viewModel()) {
    exoPlayerViewModel.createExoPlayer(applicationContext)
    val scope = rememberCoroutineScope()
    val query = searchViewModel.query.collectAsState().value
    val active = searchViewModel.active.collectAsState().value
    val rotation = animateFloatAsState(
        targetValue = if (active) 45f else 0f,
        animationSpec = tween(300),
        label = "rotate"
    )
    val firestoreService = FirestoreService()
    val songList = mutableStateListOf<Song>()
    scope.launch (Dispatchers.Main){
        //en principio no hace falta ni el async ni el await, pero a una mala se lo clavo
        songList.addAll(firestoreService.getItems())
    }
    Column (
        Modifier
            .fillMaxSize()
            .padding(innerPadding), Arrangement.Top, Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(20.dp))
        SearchBar(
            query = query,
            onQueryChange = { searchViewModel.queryChange(it) },
            onSearch = { searchViewModel.setActiveFalse() },
            active = active,
            onActiveChange = {
                searchViewModel.setActive(it)
            },
            colors = SearchBarDefaults.colors(
                //containerColor = Color.Green
            ),
            leadingIcon = {
                IconButton(
                    onClick = {
                        searchViewModel.activeChange()
                    },
                    Modifier.graphicsLayer(
                        rotationZ = rotation.value
                    )
                )
                {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "cross",
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = { searchViewModel.setActiveFalse() },
                    enabled = query.isNotEmpty()
                )
                {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "search"
                    )
                }
            }, modifier = Modifier
                .fillMaxWidth(0.95f)
        )
        {
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                val filteredSongs = songList.filter {
                    it.getName().contains(query, true)
                }
                items(filteredSongs.size) { index ->
                    Text(
                        filteredSongs[index].getName(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            val filteredSongs = songList.filter {
                it.getName().contains(query, true)
            }
            items(filteredSongs.size) { index ->
                Card (onClick = {
                        navController.navigate("songactivity/${filteredSongs[index].getId()}")
                        exoPlayerViewModel.playMusic(applicationContext, filteredSongs, filteredSongs[index])

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
                            id = if(filteredSongs[index].getArtist().contains("Jonas")) {
                                R.drawable.jonasartistphoto
                            }
                            else if (filteredSongs[index].getArtist().contains("Muse")) {
                                R.drawable.museartistphoto
                            }
                            else {
                                R.drawable.jonasartistphoto
                            }),
                            contentDescription = null)
                        Column (Modifier.padding(start = 12.dp)){
                            Text(
                                filteredSongs[index].getName()
                            )
                            Text(
                                filteredSongs[index].getArtist()
                            )
                        }
                    }
                }
            }
        }
    }
}