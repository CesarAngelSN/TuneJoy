package com.example.tunejoy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.tunejoy.ui.navigation.NavigationGraph
import com.example.tunejoy.ui.theme.TuneJoyTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContent {
            var isDarkTheme by remember {
                mutableStateOf(true)
            }
            TuneJoyTheme(
                darkTheme = isDarkTheme
            ) {
                NavigationGraph(applicationContext, getGoogleLoginAuth()) { newTheme ->
                    isDarkTheme = newTheme
                }
            }
        }
    }

    private fun getGoogleLoginAuth(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.web_client_id))
            .requestId()
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }
}
