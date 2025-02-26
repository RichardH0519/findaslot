package com.example.findaslot.logic.parking

import com.example.findaslot.logic.network.RetrofitObject
import com.example.findaslot.models.parking.SearchResponse

class ParkingRepository {
    private var searchService = RetrofitObject.retrofitService
    private var API_KEY = ""

    suspend fun getResponse(location: String): SearchResponse {
        return searchService.nearbySearchParking(location, "1000", "parking", API_KEY)
    }
}
