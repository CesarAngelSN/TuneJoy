package com.example.tunejoy.database

import com.example.tunejoy.model.Artist
import com.example.tunejoy.model.Playlist
import com.example.tunejoy.model.Song
import com.example.tunejoy.ui.viewmodel.ExoPlayerViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreService {
    private val db = Firebase.firestore

    suspend fun getItems(): ArrayList<Song> {
        return withContext(Dispatchers.IO) {
            val songs: ArrayList<Song> = arrayListOf()
            val documents = db.collection("songs").get().await()
            for (document in documents) {
                val id = document.id
                val name = document.get("name") as String
                val artist = document.get("artist") as String
                val link = document.get("link") as String
                val lyrics = document.get("lyrics") as String
                val album = document.get("album") as String
                val song = Song(id, name, artist, lyrics, link, album, false)
                songs.add(song)
            }
            songs
        }
    }

    suspend fun getSongById(id: String, VM: ExoPlayerViewModel): Song {
        return withContext(Dispatchers.IO) {
            var song = Song("", "", "", "", "", "", false)
            val document = db.collection("songs").document(id).get().await()
            if (document.exists()){
                val name = document.get("name") as String
                val artist = document.get("artist") as String
                val link = document.get("link") as String
                val lyrics = document.get("lyrics") as String
                val album = document.get("album") as String
                song = Song(id, name, artist, lyrics, link, album, false)
                VM.changeCurrentSong(song)

            }
            else {
                println("canción no encontrada")
            }

            song
        }
    }

    suspend fun getSongsByArtist(artist: String): List<Song> {
        return withContext(Dispatchers.IO) {
            val songs: ArrayList<Song> = arrayListOf()
            val documents = db.collection("songs").whereEqualTo("artist", artist).get().await()
            if (!documents.isEmpty) {
                documents.forEach {
                    val id = it.id
                    val name = it.get("name") as String
                    val artistName = it.get("artist") as String
                    val link = it.get("link") as String
                    val lyrics = it.get("lyrics") as String
                    val album = it.get("album") as String
                    val song = Song(id, name, artistName, lyrics, link, album, false)
                    songs.add(song)
                }
            }
            songs
        }
    }

    suspend fun getSongsByAlbum(album: String): List<Song> {
        return withContext(Dispatchers.IO) {
            val songs: ArrayList<Song> = arrayListOf()
            val documents = db.collection("songs").whereEqualTo("album", album).get().await()
            if (!documents.isEmpty) {
                documents.forEach {
                    val id = it.id
                    val name = it.get("name") as String
                    val artistName = it.get("artist") as String
                    val link = it.get("link") as String
                    val lyrics = it.get("lyrics") as String
                    val albumName = it.get("album") as String
                    val song = Song(id, name, artistName, lyrics, link, albumName, false)
                    songs.add(song)
                }
            }
            songs
        }
    }

    suspend fun getSongsByPlaylist(playlistId: String): String {
        return withContext(Dispatchers.IO) {
            var songs = ""
            val document = db.collection("playlists").document(playlistId).get().await()
            if (document.exists()) {
                songs = document.get("songs") as String
            } else {
                println("playlist no existe")
            }
            songs
        }
    }

    suspend fun getArtists(): List<Artist> {
        return withContext(Dispatchers.IO) {
            val artists: ArrayList<Artist> = arrayListOf()
            val documents = db.collection("artists").get().await()
            for (document in documents) {
                val id = document.id
                val name = document.get("name") as String
                val description = document.get("description") as String
                val artist = Artist(id, name, description)
                artists.add(artist)
            }
            artists
        }
    }

    suspend fun getArtistById(id: String): Artist {
        return withContext(Dispatchers.IO) {
            var artist = Artist("", "", "")
            val document = db.collection("artists").document(id).get().await()
            if (document.exists()){
                val name = document.get("name") as String
                val description = document.get("description") as String
                artist = Artist(id, name, description)
            }
            else {
                println("artista no existe")
            }
            artist
        }
    }

    suspend fun getAlbums(): List<String> {
        return withContext(Dispatchers.IO) {
            val albums: ArrayList<String> = arrayListOf()
            val documents = db.collection("songs").get().await()
            for (document in documents) {
                val album = document.get("album") as String
                albums.add(album)
            }
            albums.distinct()
        }
    }

    suspend fun getPlaylists(): List<Playlist> {
        return withContext(Dispatchers.IO) {
            val playlists: ArrayList<Playlist> = arrayListOf()
            val documents = db.collection("playlists").get().await()
            for (document in documents) {
                val id = document.id
                val name = document.get("name") as String
                val songs = document.get("songs") as String
                val playlist = Playlist(id, name, songs)
                playlists.add(playlist)
            }
            playlists
        }
    }

    suspend fun getPlaylistById(id: String): Playlist {
        return withContext(Dispatchers.IO) {
            var playlist = Playlist("", "", "")
            val document = db.collection("playlists").document(id).get().await()
            if (document.exists()){
                val name = document.get("name") as String
                val songs = document.get("songs") as String
                playlist = Playlist(id, name, songs)
            }
            else {
                println("playlist no existe")
            }
            playlist
        }
    }
}