package com.example.storyapp.data.pref

import android.util.Log
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterResponse
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    suspend fun registerUser(name: String, email: String, password: String): RegisterResponse {
        return try {
            apiService.register(name, email, password)
        } catch (e: HttpException) {
            // Handle exceptions like network errors
            Log.e("SendRegistrationData", "Error sending registration data: ${e.message}")
            RegisterResponse(message = "Failed to send registration data")

        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreferences.saveSession(user)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(email, password)
        if (!response.error!!) {
            response.loginResult?.token?.let { token ->
                userPreferences.saveSession(UserModel(email, token, true))
            }
        }
        return response
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences
        ) = UserRepository(apiService, userPreference)
    }
}
