package com.nicktz.revoluttest.utils

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

object KeyboardUtil {

    fun hideKeyboard(view: View) {
        ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
            ?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}