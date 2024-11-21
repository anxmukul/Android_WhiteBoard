package com.example.whiteboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whiteboard.domain.ILocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private var ILocalRepository: ILocalRepository) :
    ViewModel() {


    fun saveIsSignedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            ILocalRepository.saveSignIn(false)
        }
    }
}