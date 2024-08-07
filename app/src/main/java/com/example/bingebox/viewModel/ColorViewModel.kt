package com.example.bingebox.viewModel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ColorViewModel : ViewModel() {
    private val _backgroundColor = MutableStateFlow(Color.White)
    val backgroundColor: StateFlow<Color> = _backgroundColor

    fun updateBackgroundColor(color: Color) {
        Log.d("ColorUpdate", "Updating background color to: $color") // Debugging
        _backgroundColor.value = color
    }
}