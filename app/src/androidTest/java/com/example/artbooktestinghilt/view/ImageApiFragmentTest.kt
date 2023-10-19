package com.example.artbooktestinghilt.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.artbooktestinghilt.launchFragmentInHiltContainer
import com.example.artbooktestinghilt.repo.FakeArtRepositoryAndroidTest
import com.example.artbooktestinghilt.viewmodel.ArtViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import com.example.artbooktestinghilt.R
import com.example.artbooktestinghilt.adapter.ImageRecyclerAdapter
import com.example.artbooktestinghilt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ImageApiFragmentTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `select image` () {
        val navController = Mockito.mock(NavController::class.java)

        val selectedImageUrl = "Test.com" // FakeArtRepository'deki listede hiç eleman olmadığından bir eleman eklememiz lazım

        val testViewModel = ArtViewModel(FakeArtRepositoryAndroidTest())

        launchFragmentInHiltContainer<ImageApiFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)

            viewModel = testViewModel

            imageRecyclerAdapter.images = listOf(selectedImageUrl)
        // adapterdeki listeye bir eleman eklemiş olduk. // Bu ilk elemanın index'i 0. Zaten biz de sıfırıncı index'e click edeceğiz.
        }

        Espresso.onView(withId(R.id.imageRecyclerView)).perform(
            /// Nereye tıkladığımız
            RecyclerViewActions.actionOnItemAtPosition<ImageRecyclerAdapter.ImageViewHolder>(0, click())
        )

        Mockito.verify(navController).popBackStack()

        /*

        viewModel, bu url stringlerini selectedImageUrl ile takip ediyor.
        selectedImageUrl LiveData türünden olduğu için onu yazılmış senkron yapma fonksiyonuna tabi tutuyoruz.
        Ve eşit mi bizim yazdığımız url stringine ? diyoruz.

        */
        assertThat(testViewModel.selectedImageUrl.getOrAwaitValue()).isEqualTo(selectedImageUrl)
    }
}