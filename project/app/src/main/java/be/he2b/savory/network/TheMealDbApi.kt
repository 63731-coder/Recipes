package be.he2b.savory.network

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines the network calls to search for meals and list food categories using the API.
 */
interface TheMealDbApi {
    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealResponse

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): MealResponse

    @GET("filter.php")
    suspend fun getMealsByArea(@Query("a") area: String): MealResponse

    @GET("list.php?c=list")
    suspend fun getMealCategories(): CategoryListResponse

    @GET("list.php?a=list")
    suspend fun getMealCountries(): CountryListResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealResponse
}