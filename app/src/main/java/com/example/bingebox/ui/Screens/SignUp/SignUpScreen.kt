package com.example.bingebox.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bingebox.CommonComponents.*
import com.example.bingebox.source.local.UserDao
import com.example.bingebox.Navigation.Routes
import com.example.bingebox.R
import com.example.bingebox.model.User
import com.example.bingebox.viewModel.LoadingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@Composable
fun SignUpScreen(navController: NavController, userDao: UserDao, loadingViewModel: LoadingViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isLoading by loadingViewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val emailPattern = Pattern.compile(
        "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+$"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                colorResource(id = R.color.red) ,
                                colorResource(id = R.color.appicon_background)
                            )
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                HeaderTextComponent(value = stringResource(id = R.string.create_account))
                Spacer(modifier = Modifier.height(40.dp))
                RoundedTextFieldEmail(
                    text = email,
                    labelvalue = stringResource(id = R.string.email),
                    painterResource = painterResource(id = R.drawable.profile),
                    onTextChanged = { email = it },
                    keyboardType = KeyboardType.Email
                )
                Spacer(modifier = Modifier.height(16.dp))
                RoundedTextFieldPassword(
                    text = password,
                    labelvalue = stringResource(id = R.string.password),
                    painterResource = painterResource(id = R.drawable.password),
                    onTextChanged = { password = it }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            Log.d("SignUpScreen", "Attempting to sign up: email=$email, password=$password")
                            loadingViewModel.showLoading()
                            delay(2000)
                            loadingViewModel.hideLoading()
                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                errorMessage = "Invalid email format"
                                Log.d("SignUpScreen", "Invalid email format")
                            } else if (password.length < 6) {  // Corrected password length check to 6 characters
                                errorMessage = "Password must be at least 6 characters"
                                Log.d("SignUpScreen", "Password too short")
                            } else {
                                try {
                                    val existingUser = userDao.getUserByEmail(email)
                                    if (existingUser != null) {
                                        errorMessage = "Email already exists"
                                        Log.d("SignUpScreen", "Email already exists")
                                    } else {
                                        Log.d("SignUpScreen", "New user")
                                        val user = User(username = email, password = password)
                                        userDao.insertUser(user)
                                        Log.d("SignUpScreen", "User inserted")
                                        navController.navigate(Routes.loginScreen)
                                    }
                                } catch (e: Exception) {
                                    Log.e("SignUpScreen", "Error during sign up", e)
                                    errorMessage = "An error occurred. Please try again."
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = colorResource(id = R.color.appicon_background)
                    )
                ) {
                    Text(text = stringResource(id = R.string.sign_up))
                }

                Spacer(modifier = Modifier.height(16.dp))

                errorMessage?.let {
                    Text(text = it, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    SimpleTextComponent(value = stringResource(id = R.string.account))
                    Spacer(modifier = Modifier.width(8.dp))
                    SimpleTextComponentClickable(value = stringResource(id = R.string.login_now), navController, Routes.loginScreen)
                }
            }
        }

        if (isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Blue)
            }
        }
    }
}

