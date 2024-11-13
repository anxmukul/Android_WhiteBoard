package com.example.whiteboard

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
//    private val _data = mutableStateOf<List<ApiResponse>>(emptyList())
//    val data: State<List<ApiResponse>> = _data

    private val _data = mutableStateOf<List<Photos>>(emptyList())
    val data: State<List<Photos>> = _data

    init {
        fetchData()
    }

    fun fetchData() {
        Log.e("TAG", "Calling fetchData() ")
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMarsData().photos
                Log.e("TAG", "fetchData: $response")
                _data.value = response
            } catch (e: Exception) {
                // Handle the error
                e.printStackTrace()
            }
        }
    }
}