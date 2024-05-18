package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.pref.UserRepository
import com.example.storyapp.data.response.ErrorResponse
import com.example.storyapp.data.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun saveLogin(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                _loginResult.value = response
                if (!response.error!!) {
                    val token = response.loginResult?.token
                    token?.let {
                        val userModel = UserModel(email, it, true)
                        saveSession(userModel)
                    }
                } else {
                    _loginResult.value = LoginResponse(null,true, "Gagal Masuk")
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _isError.value = errorMessage ?: "Gagal Masuk"
            } finally {
                _isLoading.value = false
            }
        }
    }
}