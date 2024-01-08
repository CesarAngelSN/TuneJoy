package com.example.tunejoy.ui.navigation

sealed class Paths(val path: String) {
    object IconActivity : Paths("iconactivity")
    object LoginActivity : Paths("loginactivity")
    object HomeActivity : Paths("homeactivity")
    object AlbumActivity : Paths("albumactivity")
    object ArtistActivity : Paths("artistactivity")
    object PlaylistActivity : Paths("playlistactivity")
    object SongActivity : Paths("songactivity")
    object SearchActivity : Paths("searchactivity")
    object AllArtistsActivity : Paths("allartistsactivity")
    object AllAlbumsActivity : Paths("allalbumsactivity")
    object AllPlaylistsActivity : Paths("allplaylistsactivity")
}
