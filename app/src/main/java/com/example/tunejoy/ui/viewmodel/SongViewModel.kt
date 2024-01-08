package com.example.tunejoy.ui.viewmodel

import android.content.Context
import android.media.browse.MediaBrowser.MediaItem
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SongViewModel: ViewModel() {
    private val _albumImage = MutableStateFlow(0)
    val albumImage = _albumImage.asStateFlow()
    fun setImage(newImage: Int) {
        _albumImage.value = newImage
    }

    private val _songTitle = MutableStateFlow("")
    val songTitle = _songTitle.asStateFlow()
    fun setNewSongTitle(newSongTitle: String) {
        _songTitle.value = newSongTitle
    }

    private val _artist = MutableStateFlow("")
    val artist = _artist.asStateFlow()
    fun setNewArtist(newArtist: String) {
        _artist.value = newArtist
    }

    private val _heartState = MutableStateFlow(false)
    val heartState = _heartState.asStateFlow()
    fun setNewHeartState(newHeartState: Boolean) {
        _heartState.value = newHeartState
    }

    private val _microphoneState = MutableStateFlow(false)
    val microphoneState = _microphoneState.asStateFlow()
    fun setNewMicrophoneState(newMicrophoneState: Boolean) {
        _microphoneState.value = newMicrophoneState
        if (_microphoneState.value) {
            _infoBarOffset.value = 150
            _boxHeight.value = 380.dp
        }
        else {
            _infoBarOffset.value = 0
            _boxHeight.value = 320.dp
        }
    }

    private val _currentSongTime = MutableStateFlow("")
    val currentSongTime = _currentSongTime.asStateFlow()
    fun setNewCurrentSongTime(newCurrentSongTime: String) {
        _currentSongTime.value = newCurrentSongTime
    }

    private val _songDuration = MutableStateFlow("")
    val songDuration = _songDuration.asStateFlow()
    fun setNewSongDuration(newSongDuration: String) {
        _songDuration.value = newSongDuration
    }

    private val _playState = MutableStateFlow(true)
    val playState = _playState.asStateFlow()
    fun setNewPlayState(newPlayState: Boolean) {
        _playState.value = newPlayState
    }

    private val _boxHeight = MutableStateFlow(320.dp)
    val boxHeight = _boxHeight.asStateFlow()


    private val _infoBarOffset = MutableStateFlow(0)
    val infoBarOffset = _infoBarOffset.asStateFlow()

    private val _shuffleState = MutableStateFlow(false)
    val shuffleState = _shuffleState.asStateFlow()
    fun setNewShuffleState(newShuffleState: Boolean) {
        _shuffleState.value = newShuffleState
    }

    private val _loopState = MutableStateFlow(false)
    val loopState = _loopState.asStateFlow()
    fun setNewLoopState(newLoopState: Boolean) {
        _loopState.value = newLoopState
    }

    private val _sliderValue = MutableStateFlow(0f)
    val sliderValue = _sliderValue.asStateFlow()
    fun setNewSliderValue(newSliderValue: Float) {
        _sliderValue.value = newSliderValue
    }

    private val _currentTimePassed = MutableStateFlow<Long>(0)
    val currentTimePassed = _currentTimePassed.asStateFlow()
    fun updateTimePassed(timePassed: Long) {
        _currentTimePassed.value = timePassed
    }

    fun formatDuration(duration: Long): String {
        val totalSeconds = duration / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        return String.format("%02d:%02d", minutes, seconds)
    }

    fun formatLyrics(lyrics: String): String {
        return lyrics.replace(Regex("\\."), "\n")
    }


    //fun initializePlayer

}