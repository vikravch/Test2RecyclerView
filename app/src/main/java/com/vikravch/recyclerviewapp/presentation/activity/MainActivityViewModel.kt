package com.vikravch.recyclerviewapp.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikravch.recyclerviewapp.domain.repository.PostsRepository
import com.vikravch.recyclerviewapp.presentation.adapter.RecyclerViewAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val postsRepository: PostsRepository
): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val adapter = RecyclerViewAdapter()
    private var isLoading = false
    private var isLastPage = false
    private var counter = 1
    private var totalPages: Int = 0

    fun initAdapter() {
        viewModelScope.launch {
            val res = postsRepository.getItems(counter)
            withContext(Dispatchers.Main){
                if (res.isSuccess) {
                    adapter.setItems(res.getOrNull()?.posts?: emptyList())
                    totalPages = res.getOrNull()?.totalPages?:0
                } else {
                    _uiEvent.send(UiEvent.LoadedInitError(
                        res.exceptionOrNull()?.message?: "Loading error"
                    ))
                }
            }
        }
    }
    fun loadItems() {
        if (!isLoading && !isLastPage) {
            isLoading = true
            counter++
            viewModelScope.launch {
                val res = postsRepository.getItems(counter)
                withContext(Dispatchers.Main) {
                    if (res.isSuccess) {
                        adapter.removeLoading()
                        adapter.addItems(res.getOrNull()?.posts ?: emptyList())
                        isLoading = false
                        if (counter != totalPages) {
                            adapter.addLoading()
                        } else {
                            isLastPage = true
                        }
                    } else {
                        _uiEvent.send(UiEvent.LoadedMoreError(
                            res.exceptionOrNull()?.message?: "Loading error"
                        ))
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _uiEvent.close()
        adapter.clear()
        counter = 1
    }

    sealed class UiEvent {
        data class LoadedInitError(val message: String): UiEvent()
        data class LoadedMoreError(val message: String): UiEvent()
    }
}