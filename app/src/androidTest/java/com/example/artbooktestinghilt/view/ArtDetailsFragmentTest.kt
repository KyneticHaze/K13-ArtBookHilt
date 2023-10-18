package com.example.artbooktestinghilt.view

import com.example.artbooktestinghilt.R

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.example.artbooktestinghilt.getOrAwaitValue
import com.example.artbooktestinghilt.launchFragmentInHiltContainer
import com.example.artbooktestinghilt.repo.FakeArtRepositoryAndroidTest
import com.example.artbooktestinghilt.roomdb.Art
import com.example.artbooktestinghilt.viewmodel.ArtViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtDetailsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `image API going by imageView clicked` () {

        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.artImageView)).perform(click())

        Mockito.verify(navController).navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())
    }

    @Test
    fun  `nav back on back pressed` () {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.pressBack() // ViewActions'un methodu değil!
        // pressBack() -> ViewActions.pressBack() Maalesef bu kod çalışmadı!

        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun `Art save art details fragment there` () {
        val testViewModel = ArtViewModel(FakeArtRepositoryAndroidTest())

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ) {
            viewModel = testViewModel /// viewModel'e, testViewModel'i tanımladık.
        }

        Espresso.onView(ViewMatchers.withId(R.id.artNameText)).perform(replaceText("Mona Lisa"))
        Espresso.onView(ViewMatchers.withId(R.id.artistNameText)).perform(replaceText("Da Vinci"))
        Espresso.onView(ViewMatchers.withId(R.id.yearNameText)).perform(replaceText("1200"))
        Espresso.onView(ViewMatchers.withId(R.id.saveButton)).perform(click())

        val testArtInfo = testViewModel.artList.getOrAwaitValue()

        assertThat(testArtInfo).contains(
            Art(
                name = "Mona Lisa",
                artistName = "Da Vinci",
                year = 1200,
                imageUrl = ""
            )
        )
    }
}