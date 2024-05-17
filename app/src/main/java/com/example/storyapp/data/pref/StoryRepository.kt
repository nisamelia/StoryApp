package com.example.storyapp.data.pref

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import com.example.storyapp.R
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.response.AddStoryResponse
import com.example.storyapp.data.response.DetailResponse
import com.example.storyapp.data.response.StoryResponse
import com.example.storyapp.data.utils.reduceFileImage
import com.example.storyapp.data.utils.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
    suspend fun setStories() : StoryResponse {
        return apiService.getStories()
    }

    suspend fun setDetail(id : String) : DetailResponse {
        return apiService.getDetail(id)
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    suspend fun logout() {
        userPreferences.logout()
    }


    fun addStory(file: MultipartBody.Part, description: RequestBody): LiveData<Result<AddStoryResponse>> = liveData {
        try {
            val response = apiService.uploadImage(file, description)
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("AddStoryViewModel", "postStory: ${e.message.toString()}")
        }
    }

//    suspend fun upload(
//        imageFile: File,
//        description: String,
//        showLoading: (Boolean) -> Unit,
//        showToast: (String) -> Unit,
//        onError: (String) -> Unit
//    ) {
//            val requestBody = description.toRequestBody("text/plain".toMediaType())
//            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
//            val multipartBody = MultipartBody.Part.createFormData(
//                "photo",
//                imageFile.name,
//                requestImageFile
//            )
//
//            try {
//                showLoading(true)
//                val token = userPreferences.getSession().first().token
//
//                if (token.isNotEmpty()) {
//                    val successResponse = apiService.uploadImage(multipartBody, requestBody)
//                    showToast(successResponse.message)
//                } else {
//                    showToast("Token is missing")
//                }
//            } catch (e: HttpException) {
//                val errorBody = e.response()?.errorBody()?.string()
//                val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
//                showToast(errorResponse.message)
//                onError(errorResponse.message)
//            } finally {
//                showLoading(false)
//            }
//    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences,
        ) = StoryRepository(apiService, userPreference)
    }
}