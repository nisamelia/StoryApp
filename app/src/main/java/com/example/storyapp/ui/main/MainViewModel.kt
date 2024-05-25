package com.example.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.pref.StoryRepository
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: StoryRepository
) : ViewModel() {
//    private val _story = MutableLiveData<StoryResponse>()
    private val _story = MutableLiveData<PagingData<ListStoryItem>>()
//    val story: LiveData<StoryResponse> = _story
    val story: LiveData<PagingData<ListStoryItem>> = repository.setStories().cachedIn(viewModelScope)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

//    fun setStories() {
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val storyResponse = repository.setStories()
//                _story.value = storyResponse
//            } catch (e: Exception) {
//                _story.value = StoryResponse(emptyList(), error = true, message = e.message)
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }


}