package com.example.edvora.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.edvora.repository.RidesRepository

class MainViewModelProviderFactory (private val ridesRepository: RidesRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(ridesRepository) as T

}