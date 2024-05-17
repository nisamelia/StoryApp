package com.example.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.pref.StoryRepository
import com.example.storyapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class StoryViewModel (
    private val storyRepository: StoryRepository
) : ViewModel() {
    private val _story = MutableLiveData<StoryResponse>()
    val story: LiveData<StoryResponse> = _story

    fun setStories(){
        viewModelScope.launch {
            try {
                val storyResponse = storyRepository.setStories()
                _story.value = storyResponse
            } catch (e: Exception) {
                _story.value = StoryResponse(emptyList(), error = true, message = e.message)
            }
        }
    }
}