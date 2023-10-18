package com.example.artbooktestinghilt.repository

import androidx.lifecycle.LiveData
import com.example.artbooktestinghilt.api.RetrofitApi
import com.example.artbooktestinghilt.di.AppModule
import com.example.artbooktestinghilt.model.ImageResponse
import com.example.artbooktestinghilt.roomdb.Art
import com.example.artbooktestinghilt.roomdb.ArtDao
import com.example.artbooktestinghilt.util.Resource
import javax.inject.Inject

class ArtRepository @Inject constructor(
    private val artDao: ArtDao,
    private val retrofitApi: RetrofitApi
): ArtRepositoryInterface {
    override suspend fun insertArt(art: Art) {
        artDao.insertArt(art)
    }

    override suspend fun deleteArt(art: Art) {
        artDao.deleteArt(art)
    }

    override fun getArt(): LiveData<List<Art>> {
        return artDao.observeArts()
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {
            val response = retrofitApi.imageSearch(imageString)

            if (response.isSuccessful) {
                response.body()?.let {imageResponse ->
                    return@let Resource.success(imageResponse)
                } ?: Resource.error("Error", null)
            } else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error("No Data!", null)
        }
    }
}