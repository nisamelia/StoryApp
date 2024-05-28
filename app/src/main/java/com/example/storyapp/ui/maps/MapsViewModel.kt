package com.example.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.pref.StoryRepository
import com.example.storyapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _story = MutableLiveData<StoryResponse>()
    val story: LiveData<StoryResponse> = _story

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setMarkers() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val mapResponse = repository.setMap()
                _story.value = mapResponse
            } catch (e: retrofit2.HttpException) {
                _story.value = StoryResponse(emptyList(), error = true, message = e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }
}