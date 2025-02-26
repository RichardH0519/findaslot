package com.example.findaslot.logic.network

import com.example.findaslot.models.parking.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("place/nearbysearch/json")
    suspend fun nearbySearchParking(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("types") types: String,
        @Query("key") key: String
    ): SearchResponse
}