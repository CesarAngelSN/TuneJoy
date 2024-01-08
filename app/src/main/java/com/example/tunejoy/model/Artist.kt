package com.example.tunejoy.model

class Artist(
    private val id: String,
    private val name: String,
    private val description: String
) {
    fun getId(): String {
        return id
    }

    fun getName(): String {
        return name
    }

    fun getDescription(): String {
        return description
    }

    override fun toString(): String {
        return "Artist(id='$id', name='$name', description='$description')"
    }

}

//CREAR ARTISTAS EN FIREBASE