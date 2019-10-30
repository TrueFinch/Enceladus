package com.truefinch.enceladus.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    val check = MutableLiveData<Boolean>()

    fun setCheck(value: Boolean) {
        check.value = value
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is auth Fragment"
    }
    val text: LiveData<String> = _text
    // TODO: Implement the ViewModel
}
