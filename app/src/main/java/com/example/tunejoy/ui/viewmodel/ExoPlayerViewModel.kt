package com.example.tunejoy.ui.viewmodel

import android.content.Context
import androidx.compose.animation.core.RepeatMode
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem.fromUri
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.tunejoy.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class ExoPlayerViewModel: ViewModel() {

   // val storageService = StorageService()
    //val firestoreService = FirestoreService()

    private val _exoPlayer : MutableStateFlow<ExoPlayer?> = MutableStateFlow(null)
    val exoPlayer = _exoPlayer.asStateFlow()
    //_exoPlayer.value!!.addListener()
    fun createExoPlayer(context: Context) {
        _exoPlayer.value = ExoPlayer.Builder(context).build()
        _exoPlayer.value?.prepare()
        _exoPlayer.value?.playWhenReady = true
    }

    fun updateProgress() {
        viewModelScope.launch {
            while(isActive){
                _progress.value = _exoPlayer.value!!.currentPosition.toInt().toLong()
                delay(1000)
            }
        }
    }

    private val _songList = MutableStateFlow(mutableStateListOf<Song>())
    val songList = _songList.asStateFlow()

    fun playMusic(context: Context, songs: List<Song>, song: Song) {
       // viewModelScope.launch {
            _exoPlayer.value!!.stop()
            _exoPlayer.value!!.clearMediaItems()
            _exoPlayer.value?.prepare()
            _songList.value.clear()
            _songList.value.addAll(songs)
            _currentSong.value = song
            println("Link: " + (_currentSong.value as Song).getLink())
            val mediaItem = fromUri(((_currentSong.value as Song).getLink()))
            _exoPlayer.value?.setMediaItem(mediaItem)
            _exoPlayer.value!!.playWhenReady = true
            _exoPlayer.value!!.addListener(object : Player.Listener{
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            // El Player está preparado para empezar la reproducción.
                            // Si playWhenReady es true, empezará a sonar la música.
                            updateProgress()
                            _duration.value = _exoPlayer.value!!.duration
                            println("Duracion: " + _duration.value)
                        }
                        Player.STATE_BUFFERING -> {
                            // El Player está cargando el archivo, preparando la reproducción.
                            // No está listo, pero está en ello.
                        }
                        Player.STATE_ENDED -> {
                            // El Player ha terminado de reproducir el archivo.
                            changeSong(context, "normal", "next")

                        }
                        Player.STATE_IDLE -> {
                            // El player se ha creado, pero no se ha lanzado la operación prepared.
                        }
                    }

                }
            })
        //}

    }

    fun changeSong(context: Context, type: String, direction: String) {
        val random = Random
        _exoPlayer.value!!.stop()
        _exoPlayer.value!!.clearMediaItems()
        //_progress.value = 0
        //_exoPlayer.value!!.prepare()

        if (type == "normal") {
            if (direction == "next") {
                if (_songList.value.indexOf(_currentSong.value) == _songList.value.size - 1) {
                    _currentSong.value = _songList.value[0]
                }
                else {
                    _currentSong.value = _songList.value[_songList.value.indexOf(_currentSong.value) + 1]
                }
            }
            else {
                if (_songList.value.indexOf(_currentSong.value) == 0) {
                    _currentSong.value = _songList.value[_songList.value.size - 1]
                }
                _currentSong.value = _songList.value[_songList.value.indexOf(_currentSong.value) - 1]
            }
            val mediaItem = fromUri((_currentSong.value as Song).getLink())
            _exoPlayer.value!!.setMediaItem(mediaItem)
        }
        else {
            _currentSong.value = _songList.value[random.nextInt(0, _songList.value.size)]
            val mediaItem = fromUri((_currentSong.value as Song).getLink())
            _exoPlayer.value!!.setMediaItem(mediaItem)
        }

        _exoPlayer.value!!.prepare()
        _exoPlayer.value!!.playWhenReady = true

    }

    private val _currentSong = MutableStateFlow(mutableStateOf<Song?>(null))
    val currentSong = _currentSong.asStateFlow()
    fun getCurrentSong(): MutableState<Song?> {
        return _currentSong.value
    }

    private val _duration  = MutableStateFlow<Long>(0)
    val duration = _duration.asStateFlow()

    private val _progress = MutableStateFlow<Long>(0)
    val progress = _progress.asStateFlow()
    fun setNewProgress(newProgress: Long) {
        _progress.value = newProgress
        //_progress.value = _exoPlayer.value!!.currentPosition.toInt().toLong()
        _exoPlayer.value?.seekTo(_progress.value * 1000)

    }

    fun setLoopMode(repeat: Boolean) {
        if (repeat) {
            _exoPlayer.value?.repeatMode = Player.REPEAT_MODE_ONE
        }
        else {
            _exoPlayer.value?.repeatMode = Player.REPEAT_MODE_OFF
        }
    }

    fun playOrPause() {
        if (!_exoPlayer.value!!.isPlaying) {
            _exoPlayer.value!!.play()
        } else {
            _exoPlayer.value!!.pause()
        }
    }
}