package com.example.tunejoy.database

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StorageService {
    private val storage = FirebaseStorage.getInstance()

    suspend fun getReference(link: String): String {
        return withContext(Dispatchers.IO) {
            var httpLink = ""
            val storageRef = storage.reference
            val songRef = storageRef.child(link)
            songRef.downloadUrl.addOnSuccessListener {
                httpLink = it.toString()
                println("Link http dentro: $httpLink")
            }
            println("Link http: $httpLink")
            httpLink
        }
    }
}