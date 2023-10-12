package com.example.artbooktestinghilt.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtDao {

    /**
     * Tüm art ögelerini ekler
     *
     * Aynı id'den yazarsak çakışma durumunda yerine yazar diyerek anote ettik
     * @param [art]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArt(art: Art)

    /**
     * Tüm tablo ögelerini siler
     * @param [art]
     */
    @Delete
    suspend fun deleteArt(art: Art)


    /**
     * ViewModel'deki verileri gözlemlemek için bu fonksiyonu yazdık.
     * suspend anahtar kelimesini belirtmemize gerek yok çünkü LiveData yapısı zaten asenkron çalışır.
     * arts tablosundaki tüm verileri al
     * @return [LiveData]
     */
    @Query("SELECT * FROM arts")
    fun observeArts(): LiveData<List<Art>>
}