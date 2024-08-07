package com.example.bingebox.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("tv_show_prefs", Context.MODE_PRIVATE)
    private val selectedTvShowIdKey = "selected_tv_show_id"

    fun getSelectedTVShowId(): Int {
        return sharedPreferences.getInt(selectedTvShowIdKey, -1)
    }

    fun setSelectedTVShowId(id: Int) {
        sharedPreferences.edit().putInt(selectedTvShowIdKey, id).apply()
    }
}
