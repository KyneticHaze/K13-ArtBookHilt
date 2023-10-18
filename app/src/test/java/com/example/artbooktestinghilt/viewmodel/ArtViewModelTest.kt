package com.example.artbooktestinghilt.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.artbooktestinghilt.MainCoroutineRule
import com.example.artbooktestinghilt.getOrAwaitValueTest
import com.example.artbooktestinghilt.repo.FakeArtRepository
import com.example.artbooktestinghilt.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Thread tanımaksızın işlemleri execute etmeye yarar


    @get: Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ArtViewModel

    @Before
    fun setup() {

        // Test Doubles - Kopyasını test etme

        viewModel = ArtViewModel(FakeArtRepository())
    }

    @Test
    fun `insert art without year returns error` () {
        viewModel.makeArt("Mona Lisa", "Leonardo Da Vinci", "")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        /// getOrAwaitValueTest() fonksiyonu siteden bulunan bir kod. LiveData'yı normal dataya çevirir.
        // Bunun sebebi LiveData'nın asenkron bir işlem gütmesi ile alakalı. Bize senkron işlem yapan veri gerekli ki test yapabilelim.
        assertThat(value.status)
            .isEqualTo(Status.ERROR)
    }

    @Test
    // Test fonksiyonu isimlerini backtick (``) ile net şekilde belirtmemiz profesyonel açıdan önemlidir.
    fun `insert art without name returns error` () {
        viewModel.makeArt("", "Leonardo Da Vinci", "1200")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()

        assertThat(value.status)
            .isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without artistName returns error` () {
        viewModel.makeArt("Mona Lisa", "", "1200")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()

        assertThat(value.status)
            .isEqualTo(Status.ERROR)
    }
}