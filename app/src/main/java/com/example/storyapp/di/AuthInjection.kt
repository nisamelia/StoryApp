package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.pref.UserPreferences
import com.example.storyapp.data.pref.UserRepository
import com.example.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object AuthInjection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}