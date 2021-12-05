package com.example.dindinn.ui.ingredients

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dindinn.data.entities.ingredients.Ingredient
import com.example.dindinn.data.entities.ingredients.IngredientsResponse
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

class IngredientsViewModelTest {
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

    private lateinit var ingredientsViewModel: IngredientsViewModel

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
        ingredientsViewModel = IngredientsViewModel(baseRepository)
    }

    @Test
    fun getIngredients_successWithData() {
        val returnedList = listOf(Ingredient("Cocktail", "test", "test"))
        val returnedResponse = IngredientsResponse(returnedList)
        Mockito.`when`(baseRepository.getIngredients(ArgumentMatchers.anyString())).thenReturn(Observable.just(returnedResponse))
        ingredientsViewModel.getIngredients("Cocktail")
        assertEquals(ingredientsViewModel.ingredientsList.getOrAwaitValue().size, 1)
    }

    @Test
    fun getIngredients_successWithNoData() {
        val returnedList = listOf<Ingredient>()
        val returnedResponse = IngredientsResponse(returnedList)
        Mockito.`when`(baseRepository.getIngredients(ArgumentMatchers.anyString())).thenReturn(Observable.just(returnedResponse))
        ingredientsViewModel.getIngredients("Cocktail")
        assertEquals(ingredientsViewModel.ingredientsList.getOrAwaitValue().size, 0)
    }

}