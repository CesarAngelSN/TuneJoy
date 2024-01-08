package com.example.tunejoy.model

class Playlist(
    private val id: String,
    private val name: String,
    private val songs: String
) {
    fun getId(): String {
        return id
    }

    fun getName(): String {
       return name
    }

    fun getSongs(): List<String>{
        return songs.split(",")
    }
}