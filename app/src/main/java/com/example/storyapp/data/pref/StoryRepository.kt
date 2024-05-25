package com.example.storyapp.data.pref

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.paging.StoryPagingSource
import com.example.storyapp.data.response.DetailResponse
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.data.response.Story
import com.example.storyapp.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
//    suspend fun setStories() : StoryResponse {
//        return apiService.getStories()
//    }
    fun setStories(): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
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
suspend fun setMap(location: Int = 1): StoryResponse {
    return apiService.getStoriesWithLocation(location)
}

    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences,
        ) = StoryRepository(apiService, userPreference)
    }
}