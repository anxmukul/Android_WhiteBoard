package com.example.whiteboard

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whiteboard.data.Photos
import com.example.whiteboard.data.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

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