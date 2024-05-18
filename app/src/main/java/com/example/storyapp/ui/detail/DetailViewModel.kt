package com.example.storyapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.pref.StoryRepository
import com.example.storyapp.data.response.DetailResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _detail = MutableLiveData<DetailResponse>()
    val detail: LiveData<DetailResponse> = _detail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setDetail(id: String) {
        _isLoading.value = true
        try {
            _isLoading.value = true
            viewModelScope.launch {
                val detailStory = repository.setDetail(id)
                _detail.value = detailStory
            }
        } catch (e: Exception) {
            _detail.value = DetailResponse(true, e.message)
        } finally {
            _isLoading.value = false
        }
    }
}