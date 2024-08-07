package com.example.bingebox

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.bingebox.Screens.*
import com.example.bingebox.app.BingeBoxApp
import com.example.bingebox.source.local.AppDataBase

class MainActivity : AppCompatActivity() {
    private val database by lazy { AppDataBase.getDatabase(applicationContext) }
    private val userDao by lazy { database.userDao() }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            BingeBoxApp(userDao)
        }
    }
}
