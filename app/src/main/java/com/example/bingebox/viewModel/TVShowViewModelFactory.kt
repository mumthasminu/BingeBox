package com.example.bingebox.viewModel

import TVShowViewModel
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TVShowViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TVShowViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TVShowViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
