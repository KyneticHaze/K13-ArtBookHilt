package com.example.artbooktestinghilt.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.artbooktestinghilt.model.ImageResponse
import com.example.artbooktestinghilt.repository.ArtRepositoryInterface
import com.example.artbooktestinghilt.roomdb.Art
import com.example.artbooktestinghilt.util.Resource

class FakeArtRepositoryAndroidTest: ArtRepositoryInterface {

    private val arts = mutableListOf<Art>() // Art objesi listesi
    private val artsLiveData = MutableLiveData<List<Art>>(arts) // Bu obje listesinin değiştirilebilir canlı veri tutması

    override suspend fun insertArt(art: Art) {
        arts.add(art)
        refreshData()
    }

    override suspend fun deleteArt(art: Art) {
        arts.remove(art)
        refreshData()
    }

    override fun getArt(): LiveData<List<Art>> {
        return artsLiveData
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return Resource.success(ImageResponse(listOf(),0,0))
    }

    private fun refreshData() {
        artsLiveData.postValue(arts)
    }
}