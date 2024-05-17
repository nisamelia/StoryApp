package com.example.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.pref.UserRepository
import com.example.storyapp.data.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setRegister(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.registerUser(name, email, password)
                _registerResult.value = response

            } catch (e: Exception){

            } finally {
                _isLoading.value = false
            }
        }
    }
}

//class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
//    val registerResponse: LiveData<RegisterResponse> = repository.registerResponse
//
//    fun setRegister(name: String, email: String, password: String) {
//        viewModelScope.launch {
//            repository.registerUser(name, email, password)
//        }
//    }
//}