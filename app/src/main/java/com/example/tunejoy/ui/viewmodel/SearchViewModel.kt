package com.example.tunejoy.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel: ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()
    fun queryChange(text: String) {
        _query.value = text
    }

    private val _active = MutableStateFlow(false)
    val active = _active.asStateFlow()
    fun activeChange() {
        _active.value = !_active.value
    }
    fun setActive(value: Boolean) {
        _active.value = value
    }
    fun setActiveFalse() {
        _active.value = false
    }
}