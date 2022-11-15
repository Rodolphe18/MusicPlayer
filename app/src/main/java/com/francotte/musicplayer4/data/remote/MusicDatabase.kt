package com.francotte.musicplayer4.data.remote

import com.francotte.musicplayer4.data.entities.Song
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection("songs")

    suspend fun getAllSongs() : List<Song> {
        return try {
            songCollection.get().await().toObjects(Song ::class.java)
        }
        catch (e: Exception) {
            emptyList()
        }
    }
}