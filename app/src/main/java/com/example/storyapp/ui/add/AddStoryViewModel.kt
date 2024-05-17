package com.example.storyapp.ui.add

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.R
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.pref.StoryRepository
import com.example.storyapp.data.pref.UserPreferences
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.response.AddStoryResponse
import com.example.storyapp.data.utils.reduceFileImage
import com.example.storyapp.data.utils.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter
import retrofit2.HttpException


class AddViewModel(private val storyRepository: StoryRepository) : ViewModel() {
//    fun addStory(file: MultipartBody.Part, description: RequestBody) = storyRepository.addStory(file, description)
//    fun uploadStory(
//        currentImageUri: Uri?,
//        context: Context,
//        description: String,
//        showLoading: (Boolean) -> Unit,
//        showToast: (String) -> Unit,
//        onError: (String) -> Unit
//    ) {
//        viewModelScope.launch {
//            currentImageUri?.let { uri ->
//                val imageFile = uriToFile(uri, context).reduceFileImage()
//                storyRepository.upload(imageFile, description, showLoading, showToast, onError)
//            } ?: showToast(context.getString(R.string.description))
//        }
//    }
//    fun upload(
//        currentImageUri: Uri?,
//        description: String,
//        showLoading: (Boolean) ->Unit,
//        showToast: (String) -> Unit,
//        onError: (String) -> Unit
//    ) {
//        currentImageUri?.let { uri ->
//
//            val imageFile = uriToFile(uri, context).reduceFileImage()
////            Log.d("Image File", "showImage: ${imageFile.path}")
////            val description = binding.textInputEditText.text.toString()
////            showLoading(true)
//
//            val requestBody = description.toRequestBody("text/plain".toMediaType())
//            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
//            val multipartBody = MultipartBody.Part.createFormData(
//                "photo",
//                imageFile.name,
//                requestImageFile
//            )
//
//            viewModelScope.launch {
//                val userPreferences = UserPreferences.getInstance(context.dataStore)
//                val token = userPreferences.getSession().first().token
//
//                if (token.isNotEmpty()) {
//                    try {
//                        val apiService = ApiConfig.getApiService(token)
//                        val successResponse = apiService.uploadImage(multipartBody, requestBody)
//                        showToast(successResponse.message)
////                        showLoading(false)
////                        finish()
//                    } catch (e: HttpException) {
//                        val errorBody = e.response()?.errorBody()?.string()
//                        val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
//                        showToast(errorResponse.message)
//                        showLoading(false)
//                    }
//                } else {
//                    showToast("Token is missing")
//                    showLoading(false)
//                }
//            }
//
//        } ?: showToast(context.getString(R.string.description))
//    }
}