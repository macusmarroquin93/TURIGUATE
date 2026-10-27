package com.example.turiguate

import com.example.turiguate.data.Category
import com.example.turiguate.data.LocalCatalog
import com.example.turiguate.data.TravelPlan
import com.example.turiguate.data.filterDestinations
import org.junit.Assert.*
import org.junit.Test

class TravelLogicTest {

    @Test
    fun searchIgnoresAccentsAndCapitalization() {
        val results = filterDestinations(
            destinations = LocalCatalog.destinations,
            query = "  ATITLAN  ",
            category = null
        )

        assertEquals(listOf("atitlan"), results.map { it.id })
    }

    @Test
    fun searchAndCategoryAreCombined() {
        val results = filterDestinations(
            destinations = LocalCatalog.destinations,
            query = "Tikal",
            category = Category.NATURE
        )

        assertTrue(results.isEmpty())
    }

    @Test
    fun emptyQueryKeepsTheCategoryFilter() {
        val results = filterDestinations(
            destinations = LocalCatalog.destinations,
            query = "",
            category = Category.NATURE
        )

        assertEquals(2, results.size)
        assertTrue(results.all { it.category == Category.NATURE })
    }

    @Test
    fun clearingFiltersReturnsTheWholeCatalog() {
        val results = filterDestinations(
            destinations = LocalCatalog.destinations,
            query = "",
            category = null
        )

        assertEquals(LocalCatalog.destinations, results)
    }

    @Test(expected = IllegalArgumentException::class)
    fun cannotCreateAnEmptyPlan() {
        TravelPlan(
            id = "test",
            title = "Mi viaje",
            days = 3,
            destinationIds = emptyList()
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun cannotRepeatDestinations() {
        TravelPlan(
            id = "test",
            title = "Mi viaje",
            days = 3,
            destinationIds = listOf("antigua", "antigua")
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun cannotCreateAPlanWithInvalidDuration() {
        TravelPlan(
            id = "test",
            title = "Mi viaje",
            days = 0,
            destinationIds = listOf("antigua")
        )
    }
}