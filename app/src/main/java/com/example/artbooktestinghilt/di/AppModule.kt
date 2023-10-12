package com.example.artbooktestinghilt.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.artbooktestinghilt.R
import com.example.artbooktestinghilt.api.RetrofitApi
import com.example.artbooktestinghilt.repository.ArtRepository
import com.example.artbooktestinghilt.repository.ArtRepositoryInterface
import com.example.artbooktestinghilt.roomdb.ArtDao
import com.example.artbooktestinghilt.roomdb.ArtDatabase
import com.example.artbooktestinghilt.util.Util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Bu modül, object anahtar kelimesi ile tanımlanarak modülün yapısının singleton bir yapı olduğunu belirtir.
 * Bu modülü yazarak boilerplate kodlardan kurtulmuş oluruz.
 * @author furkanharmanci
 * @since 1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Bu method ile room veritabanı inşası sağlanır.
     * @param context
     */
    @Singleton
    @Provides
    fun injectRoomDatabase(
        @ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        ArtDatabase::class.java,
        "ArtBookDB"
    ).build()

    /**
     * Bu methoda inşa edilen veritabanına ait olan [ArtDatabase.artDao] methodu tanımlanır.
     * @param database
     */
    @Singleton
    @Provides
    fun injectDao(database: ArtDatabase) = database.artDao()

    /**
     * Bu method ile Retrofit inşası yapılır ve [RetrofitApi] arabirimi servisi eklenir.
     * @return [RetrofitApi]
     */
    @Singleton
    @Provides
    fun injectRetrofitAPI(): RetrofitApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build().create(RetrofitApi::class.java)
    }

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.compose)
                .error(R.drawable.compose)
        )

    @Singleton
    @Provides
    fun injectNormalRepo(dao: ArtDao, api: RetrofitApi) = ArtRepository(dao, api) as ArtRepositoryInterface
}