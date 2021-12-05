package com.example.dindinn.ui.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dindinn.data.entities.categories.CategoriesResponse
import com.example.dindinn.data.entities.categories.Drink
import com.example.dindinn.data.entities.search.Ingredient
import com.example.dindinn.data.entities.search.SearchResponse
import com.example.dindinn.data.network.BaseRepository
import com.example.dindinn.ui.RxImmediateSchedulerRule
import com.example.dindinn.ui.getOrAwaitValue
import io.reactivex.rxjava3.core.Observable
import org.junit.Assert.*
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CategoriesViewModelTest {

    /*
     this because the default scheduler returned by AndroidSchedulers.mainThread() is an instance
     of LooperScheduler and relies on Android dependencies that are not available in JUnit tests.
     */
    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var baseRepository: BaseRepository

    private lateinit var categoriesViewModel: CategoriesViewModel

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
        categoriesViewModel = CategoriesViewModel(baseRepository)
    }

    @Test
    fun autoComplete_successWithData() {
        val returnedList = listOf(Ingredient("Cold Drinks", "test",
            "test", "test", "test", "test"))
        val returnedResponse = SearchResponse(returnedList)
        Mockito.`when`(baseRepository.getSearchResults(ArgumentMatchers.anyString())).thenReturn(Observable.just(returnedResponse))
        categoriesViewModel.autoComplete("coca")
        assertEquals(categoriesViewModel.autoCompleteList.getOrAwaitValue().size, 1)
    }

    @Test
    fun autoComplete_successWithNoData() {
        val returnedList = listOf<Ingredient>()
        val returnedResponse = SearchResponse(returnedList)
        Mockito.`when`(baseRepository.getSearchResults(ArgumentMatchers.anyString())).thenReturn(Observable.just(returnedResponse))
        categoriesViewModel.autoComplete("coca")
        assertEquals(categoriesViewModel.autoCompleteList.getOrAwaitValue().size, 0)
    }

    @Test
    fun getCategories_successWithData() {
        val returnedList = listOf(Drink("Cold Drinks"))
        val returnedResponse = CategoriesResponse(returnedList)
        Mockito.`when`(baseRepository.getCategories()).thenReturn(Observable.just(returnedResponse))
        categoriesViewModel.getCategories()
        assertEquals(categoriesViewModel.categoriesList.getOrAwaitValue().size, 1)
    }

    @Test
    fun getCategories_successWithNoData() {
        val returnedList = listOf<Drink>()
        val returnedResponse = CategoriesResponse(returnedList)
        Mockito.`when`(baseRepository.getCategories()).thenReturn(Observable.just(returnedResponse))
        categoriesViewModel.getCategories()
        assertEquals(categoriesViewModel.categoriesList.getOrAwaitValue().size, 0)
    }
}