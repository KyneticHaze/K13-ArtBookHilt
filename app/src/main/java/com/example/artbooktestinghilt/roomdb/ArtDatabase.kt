package com.example.artbooktestinghilt.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Art::class], version = 1)
abstract class ArtDatabase: RoomDatabase() {

    /**
     * Oluşturulan veritabanının tek soyut methodu
     * @return [ArtDao]
     */
    abstract fun artDao(): ArtDao
}