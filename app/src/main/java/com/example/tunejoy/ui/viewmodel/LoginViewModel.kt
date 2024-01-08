package com.example.tunejoy.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel() {
    private val _user  = MutableStateFlow("")
    val user = _user.asStateFlow()
    fun setUser(userName: String) {
        _user.value = userName
    }

    private val _password  = MutableStateFlow("")
    val password = _password.asStateFlow()
    fun setPassword(userPasword: String) {
        _password.value = userPasword
    }

    private val _taskCompleted = MutableStateFlow(false)
    val taskCompleted = _taskCompleted.asStateFlow()
    fun changeTaskCompleted() {
        _taskCompleted.value = true
    }
}