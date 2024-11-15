package com.example.whiteboard

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whiteboard.data.MarsFact
import com.example.whiteboard.data.Photos
import com.example.whiteboard.data.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivity2ViewModel @Inject constructor() : ViewModel() {

    private val _data = mutableStateOf<MarsFact?>(null)
    val data: State<MarsFact?> = _data

    init {
        fetchData()
    }

    fun fetchData() {
        Log.e("TAG", "Calling fetchData() ")
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMarsFact()
                Log.e("TAG", "Mars Facts: $response")
                _data.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}