package com.example.storyapp.data.pref

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.response.DetailResponse
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
    private val _resgisterResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _resgisterResponse

    suspend fun registerUser(name: String, email: String, password: String) : RegisterResponse {
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

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    suspend fun logout() {
        userPreferences.logout()
    }

//    suspend fun setStories() : StoryResponse {
//        return apiService.getStories()
//    }
//
//    suspend fun setDetail(id : String) : DetailResponse {
//        return apiService.getDetail(id)
//    }

//    suspend fun login(email: String, password: String): LoginResponse {
//        return try {
//            // Lakukan proses login ke backend menggunakan ApiService
//            val response = apiService.login(email, password)
//
//            if (!response.error!!) {
//                response.loginResult?.token?.let { token ->
//                    userPreferences.saveSession(UserModel(email, token, true))
//                }
//            }
//
//            // Kembalikan hasil login
//            return response
//        } catch (e: HttpException) {
//            // Tangani exception seperti error jaringan
//            Log.e("Login", "Error during login: ${e.message}")
//            LoginResponse(error = true, message = "Failed to log in")
//        }
//    }
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
        @Volatile
        private var instance: UserRepository? = null
//        fun getInstance(
//            apiService: ApiService,
//            userPreference: UserPreferences
//        ): UserRepository =
//            instance ?: synchronized(this) {
//                instance ?: UserRepository(apiService, userPreference)
//            }.also { instance = it }
fun getInstance(
    apiService: ApiService,
    userPreference: UserPreferences
) = UserRepository(apiService, userPreference)
    }
}

//fun setDetail(login : String) {
//    _isLoading.value = true
//
//    val client = DetailConfig.getDetailConfig().getDetailUser(login)
//    client.enqueue(object  : Callback<DetailGithubResponse> {
//        override fun onResponse(
//            call: Call<DetailGithubResponse>,
//            response: Response<DetailGithubResponse>
//        ) {
//            _isLoading.value = false
//            if (response.isSuccessful) {
//                _detail.postValue(response.body())
//            }
//        }
//
//        override fun onFailure(call: Call<DetailGithubResponse>, t: Throwable) {
//            Log.e("onFailure", t.message.toString())
//        }
//
//    })
//}