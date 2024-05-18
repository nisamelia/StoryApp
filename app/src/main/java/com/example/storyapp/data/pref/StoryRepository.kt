package com.example.storyapp.data.pref

import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.response.DetailResponse
import com.example.storyapp.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow

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

    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences,
        ) = StoryRepository(apiService, userPreference)
    }
}