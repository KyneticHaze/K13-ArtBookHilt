package com.example.artbooktestinghilt.repository

import androidx.lifecycle.LiveData
import com.example.artbooktestinghilt.model.ImageResponse
import com.example.artbooktestinghilt.roomdb.Art
import com.example.artbooktestinghilt.util.Resource

/**
 * Bu arabirim, test için yazılmış fake bir repository'dir.
 */
interface ArtRepositoryInterface {

    suspend fun insertArt(art: Art)

    suspend fun deleteArt(art: Art)

    fun getArt(): LiveData<List<Art>>

    suspend fun searchImage(imageString: String): Resource<ImageResponse>
}