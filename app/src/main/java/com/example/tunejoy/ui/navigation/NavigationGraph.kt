package com.example.tunejoy.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tunejoy.R
import com.example.tunejoy.database.FirestoreService
import com.example.tunejoy.model.Song
import com.example.tunejoy.ui.view.AlbumActivity
import com.example.tunejoy.ui.view.AllAlbumsActivity
import com.example.tunejoy.ui.view.AllArtistsActivity
import com.example.tunejoy.ui.view.AllPlaylistsActivity
import com.example.tunejoy.ui.view.ArtistActivity
import com.example.tunejoy.ui.view.HomeActivity
import com.example.tunejoy.ui.view.IconActivity
import com.example.tunejoy.ui.view.LoginActivity
import com.example.tunejoy.ui.view.PlaylistActivity
import com.example.tunejoy.ui.view.SearchActivity
import com.example.tunejoy.ui.view.SongActivity
import com.example.tunejoy.ui.viewmodel.ExoPlayerViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    applicationContext: Context,
    googleLoginAuth: GoogleSignInClient,
    changeTheme: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val currentNavController by navController.currentBackStackEntryAsState()
    val currentPath = currentNavController?.destination?.route
    val scope = rememberCoroutineScope()
    var darkTheme by remember {
        mutableStateOf(true)
    }
    var activeDialog by remember {
        mutableStateOf(false)
    }

    val exoPlayerViewModel: ExoPlayerViewModel = viewModel()
    LaunchedEffect(Unit) {
        exoPlayerViewModel.createExoPlayer(applicationContext)
    }


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    icon = { Icon(painterResource(id = R.drawable.baseline_person_24), contentDescription = null) },
                    label = { Text(text = "Artists") },
                    selected = false,
                    onClick = {
                        scope.launch(Dispatchers.Main) {
                            drawerState.close()
                        }
                        navController.navigate("allartistsactivity")
                    },
                    modifier = Modifier.padding(12.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(painterResource(id = R.drawable.baseline_playlist_play_24), contentDescription = null) },
                    label = { Text(text = "Playlists") },
                    selected = false,
                    onClick = {
                        scope.launch(Dispatchers.Main) {
                            drawerState.close()
                        }
                        navController.navigate("allplaylistsactivity")
                    },
                    modifier = Modifier.padding(12.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(painterResource(id = R.drawable.baseline_album_24), contentDescription = null) },
                    label = { Text(text = "Albums") },
                    selected = false,
                    onClick = {
                        scope.launch(Dispatchers.Main) {
                            drawerState.close()
                        }
                        navController.navigate("allalbumsactivity")
                    },
                    modifier = Modifier.padding(12.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                    label = { Text(text = "Favorite Songs") },
                    selected = false,
                    onClick = {
                        scope.launch(Dispatchers.Main) {
                            drawerState.close()
                        }
                    },
                    modifier = Modifier.padding(12.dp)
                )
                HorizontalDivider(thickness = 1.dp, color = colorResource(id = R.color.dark_grey))
                NavigationDrawerItem(
                    icon = { Icon(painterResource(id = R.drawable.baseline_logout_24), contentDescription = null) },
                    label = { Text(text = "Log Out") },
                    selected = false,
                    onClick = {
                        activeDialog = !activeDialog
                    },
                    modifier = Modifier.padding(12.dp)
                )
                if (activeDialog) {
                    AlertDialog(
                        text = {
                            Text(text = "Are you sure you want to log out?")
                        },
                        onDismissRequest = {
                            activeDialog = false
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                scope.launch(Dispatchers.Main) {
                                    googleLoginAuth.signOut()
                                    drawerState.close()
                                }
                                navController.navigate("loginactivity") {
                                    if (currentPath != null) {
                                        popUpTo(currentPath) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }) {
                                Text(text = "Confirm")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                activeDialog = false
                            }) {
                                Text(text = "Cancel")
                            }
                        })
                }

            }
        }) {
        Scaffold (
            topBar = {
                if (currentPath != Paths.IconActivity.path && currentPath != Paths.LoginActivity.path) {
                    TopAppBar(
                        navigationIcon = {
                            if (currentPath == Paths.AlbumActivity.path || currentPath == Paths.ArtistActivity.path
                                || currentPath == Paths.PlaylistActivity.path) {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "icon")
                                }
                            }
                            else if(currentPath == Paths.HomeActivity.path) {
                                IconButton(onClick = {
                                    scope.launch(Dispatchers.Main) {
                                        drawerState.open()
                                    }
                                }) {
                                    Icon(Icons.Filled.Menu, contentDescription = "icon")
                                }
                            }
                        },
                        actions = {
                              IconButton(onClick = {
                                  darkTheme = !darkTheme
                                  changeTheme(darkTheme)
                              }) {
                                  Icon(if(darkTheme) {
                                        painterResource(id = R.drawable.baseline_light_mode_24)
                                     }
                                      else {
                                           painterResource(id = R.drawable.baseline_dark_mode_24)
                                       }, contentDescription = "icon")
                              }
                        },
                        title = {})
                }
            },
            bottomBar = {
                val firestoreService = FirestoreService()
                val songList = mutableStateListOf<Song>()
                runBlocking{
                    songList.addAll(firestoreService.getItems())
                }
                if (currentPath != Paths.IconActivity.path && currentPath != Paths.LoginActivity.path) {
                    BottomAppBar (){
                        IconButton(onClick = {
                            navController.navigate("homeactivity") {
                                if (currentPath != null) {
                                    popUpTo(currentPath) {
                                        inclusive = true
                                    }
                                }
                            }
                        },
                            Modifier.weight(1f)) {
                            Icon(Icons.Filled.Home, contentDescription = "icon",)
                        }
                        IconButton(onClick = {
                            navController.navigate("searchactivity") {
                                if (currentPath != null) {
                                    popUpTo(currentPath) {
                                        inclusive = true
                                    }
                                }
                            }
                        },
                            Modifier.weight(1f)) {
                            Icon(Icons.Filled.Search, contentDescription = "icon")
                        }
                        IconButton(onClick = {
                            navController.navigate("songactivity/${songList[0].getId()}") {
                                exoPlayerViewModel.playMusic(applicationContext, songList, songList[0])
                                if (currentPath != null) {
                                    popUpTo(currentPath) {
                                        inclusive = true
                                    }
                                }
                            }
                        },
                            Modifier.weight(1f)) {
                            Icon(painterResource(id = R.drawable.baseline_music_note_24), contentDescription = "icon")
                        }
                    }
                }
            }
        ){
                innerPadding ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(navController = navController, startDestination = Paths.IconActivity.path) {
                    composable(Paths.IconActivity.path) {
                        IconActivity(navController)
                    }
                    composable(Paths.LoginActivity.path) {
                        LoginActivity(navController, applicationContext, googleLoginAuth)
                    }
                    composable(Paths.HomeActivity.path) {
                        HomeActivity(applicationContext, innerPadding, navController)
                    }
                    composable(Paths.AlbumActivity.path + "/{album}") {
                        val album = it.arguments?.getString("album")
                        AlbumActivity(navController, innerPadding, album, exoPlayerViewModel, applicationContext)
                    }
                    composable(Paths.ArtistActivity.path + "/{artistId}") {
                        val artistId = it.arguments?.getString("artistId")
                        ArtistActivity(navController, innerPadding, artistId, exoPlayerViewModel, applicationContext)
                    }
                    composable(Paths.PlaylistActivity.path + "/{playlistId}") {
                        val playlistId = it.arguments?.getString("playlistId")
                        PlaylistActivity(navController, innerPadding, playlistId, exoPlayerViewModel, applicationContext)
                    }
                    composable(Paths.SongActivity.path + "/{songId}") {
                        val songId = it.arguments?.getString("songId")
                        SongActivity(applicationContext, innerPadding, songId, exoPlayerViewModel)
                    }
                    composable(Paths.SearchActivity.path) {
                        SearchActivity(navController, innerPadding, applicationContext, exoPlayerViewModel)
                    }
                    composable(Paths.AllArtistsActivity.path) {
                        AllArtistsActivity(navController, innerPadding)
                    }
                    composable(Paths.AllAlbumsActivity.path) {
                        AllAlbumsActivity(navController, innerPadding)
                    }
                    composable(Paths.AllPlaylistsActivity.path) {
                        AllPlaylistsActivity(navController, innerPadding)
                    }
                }
            }
        }
    }
}
