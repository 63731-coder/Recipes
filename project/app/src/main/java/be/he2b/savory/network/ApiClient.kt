package be.he2b.savory.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object that sets up Retrofit to connect the app to the API.
 */
object ApiClient {

    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    private val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    val api: TheMealDbApi = retrofit.create(TheMealDbApi::class.java)
}