package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.pref.UserRepository
import com.example.storyapp.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

//    fun saveLogin(email: String, password: String) {
//        viewModelScope.launch {
//            try {
//                // Lakukan proses login
//                val loginResponse = repository.login(email, password)
//
//                // Jika login berhasil, simpan informasi sesi pengguna
//                if (!loginResponse.error!!) {
//                    val token = loginResponse.loginResult?.token
//                    token?.let {
//                        saveSession(UserModel(email, token, true))
//                    }
//                } else {
//                    // Tangani kasus jika login gagal
//                    // Contoh: Tampilkan pesan kesalahan login
//                }
//            } catch (e: Exception) {
//                // Tangani exception jika terjadi error saat login
//                // Contoh: Tampilkan pesan kesalahan jaringan
//            }
//        }
//    }

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
                    // Handle login failure
                    // Example: Tampilkan pesan kesalahan
                }
            } catch (e: Exception) {
                // Handle login error
                // Example: Tampilkan pesan kesalahan jaringan
            } finally {
                _isLoading.value = false
            }
        }
    }
}