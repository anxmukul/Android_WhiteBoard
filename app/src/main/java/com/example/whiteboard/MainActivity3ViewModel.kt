package com.example.whiteboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whiteboard.domain.ILocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivity3ViewModel @Inject constructor(private var ILocalRepository: ILocalRepository) :
    ViewModel() {
    private val _isLoggedIn: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    init {
        isSignedIn()
    }

    private fun isSignedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            val isSignedIn = ILocalRepository.isSignedIn()
            if (isSignedIn != null) {
                if (isSignedIn) {
                    _isLoggedIn.value = true
                } else {
                    _isLoggedIn.value = false
                }
            } else {
                _isLoggedIn.value = false
            }
        }
    }

    fun saveIsSignedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            ILocalRepository.saveSignIn(true)
        }

    }
}