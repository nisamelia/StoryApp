package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.pref.StoryRepository
import com.example.storyapp.data.pref.UserPreferences
import com.example.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
    }
}