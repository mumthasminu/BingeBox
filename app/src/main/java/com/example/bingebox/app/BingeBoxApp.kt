package com.example.bingebox.app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bingebox.BingeBoxNavHost
import com.example.bingebox.source.local.UserDao
import com.example.bingebox.viewModel.ColorViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BingeBoxApp(userDao: UserDao) {

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        BingeBoxNavHost(userDao = userDao)
    }
}