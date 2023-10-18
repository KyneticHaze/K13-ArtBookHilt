package com.example.artbooktestinghilt.view

import com.example.artbooktestinghilt.R

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.example.artbooktestinghilt.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject



@MediumTest
@HiltAndroidTest
class ArtFragmentTest {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `test Nav from Art To Art Details` () {

        val navController = Mockito.mock(NavController::class.java) // nav controller'ın sahte objesini oluşturduk

        /// fragmanımızı başlattık
        launchFragmentInHiltContainer<ArtFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        /// onView ile view üzerindeki fab'ın perform(new yapsın?) methodu içinde içinde tıklama işlevini test etme
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(click())

        /// sahte objemizle bir nav işlemi yapılacak onu doğrula dedik
        Mockito.verify(navController).navigate(
            ArtFragmentDirections.actionArtFragmentToArtDetailsFragment()
        )
    }
}