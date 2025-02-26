package com.example.findaslot.logic.network

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findaslot.logic.parking.ParkingRepository
import com.example.findaslot.models.parking.SearchResponse
import kotlinx.coroutines.launch

class RetrofitViewModel : ViewModel() {
    private var repository = ParkingRepository()
    var retrofitResponse: MutableState<SearchResponse> = mutableStateOf(SearchResponse())

    fun getResponse(location: String) {
        viewModelScope.launch {
            try {
                var responseReturned = repository.getResponse(location)
                retrofitResponse.value = responseReturned
            } catch (e: Exception) {
                Log.i("Error", "Response failed")
            }
        }
    }
}