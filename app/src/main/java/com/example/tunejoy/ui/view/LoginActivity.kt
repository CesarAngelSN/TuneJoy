package com.example.tunejoy.ui.view

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tunejoy.R
import com.example.tunejoy.ui.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginActivity(navController: NavController, applicationContext: Context, googleLoginAuth: GoogleSignInClient, loginViewModel: LoginViewModel = viewModel()) {
    val user = loginViewModel.user.collectAsState().value
    val password = loginViewModel.password.collectAsState().value
    val taskComplete = loginViewModel.taskCompleted.collectAsState().value
    val scope = rememberCoroutineScope()
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    if (handleSignInResult(task)) {
                        loginViewModel.changeTaskCompleted()
                    }
                }
            }
        }
    var active by remember {
        mutableStateOf(false)
    }
    var alpha by remember {
        mutableStateOf(1f)
    }

    Column (
        Modifier
            .fillMaxSize()
            .alpha(alpha), Arrangement.SpaceEvenly, Alignment.CenterHorizontally){
        Image(painter = painterResource(id = R.drawable.logotunejoydef),
            contentDescription = "brain",
            Modifier.scale(1.3f, 1.3f))
        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                async {
                    startForResult.launch(googleLoginAuth.signInIntent)
                }.await()
            }
            if (taskComplete) {
                println("entra en segundo if")
                active = true
                navController.navigate("homeactivity") {
                    popUpTo("loginactivity") {
                        inclusive = true
                    }
                }
            }
            else {
                println("errorcito")
            }
        },
            Modifier.fillMaxWidth(0.8f)) {
            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp), Arrangement.SpaceAround, Alignment.CenterVertically){
                Image(painter = painterResource(id = R.drawable.googleicon),
                    contentDescription = "icon",
                    modifier = Modifier.size(30.dp))
                Text(text = "Sign in with Google", fontSize = 20.sp)
            }
        }
    }

    if (active) {
        alpha = 0.3f
        Column (Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally){
            Text(text = "Waiting for Firebase...", fontWeight = FontWeight.Bold, fontSize = 20.sp,
                textAlign = TextAlign.Center)
        }
    }
}


fun handleSignInResult(task: Task<GoogleSignInAccount>): Boolean {
    return task.isComplete
}
