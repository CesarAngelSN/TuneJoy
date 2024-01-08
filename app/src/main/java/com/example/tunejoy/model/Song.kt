package com.example.tunejoy.model

import androidx.compose.runtime.MutableState

class Song (
    private val id: String,
    private val name: String,
    private val artist: String,
    private val lyrics: String,
    private val link: String,
    private val album: String,
    private val liked: Boolean
) : MutableState<Song?> {
    fun getId(): String {
        return id
    }

    fun getName(): String {
        return name
    }

    fun getArtist(): String {
        return artist
    }

    fun getLyrics(): String {
        return lyrics
    }

    fun getLink(): String {
        return link
    }

    fun getAlbum(): String {
        return album
    }

    fun isLiked(): Boolean {
        return liked
    }

    private var _value: Song? = null
    override var value: Song?
        get() = _value
        set(value) {
            _value = value
        }

    override fun component1(): Song? {
        TODO("Not yet implemented")
    }

    override fun component2(): (Song?) -> Unit {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Song(name='$name', lyrics='$lyrics', link='$link')"
    }
}