package com.example.artbooktestinghilt.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.artbooktestinghilt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDaoTest {

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Main Thread'de çalıştıracağımızı söylemek için gerekli oluyor.
    private lateinit var dao: ArtDao

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
    lateinit var db: ArtDatabase

    @Before
    fun setUp() {

        hiltRule.inject()
        dao = db.artDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `inserting art from db testing` () = runBlocking {

        val exampleArt = Art("Mona Lisa", "Da Vinci", 1200, "test.com", 1)

        dao.insertArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValue()

        assertThat(list).contains(exampleArt)
    }

    @Test
    fun `deleting art from db testing` () = runBlocking {
        val exampleArt = Art("The Scream", "Edvard Munch", 1300, "test2.com", 2)

        dao.insertArt(exampleArt)

        dao.deleteArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValue()

        assertThat(list).doesNotContain(exampleArt)

    }
}