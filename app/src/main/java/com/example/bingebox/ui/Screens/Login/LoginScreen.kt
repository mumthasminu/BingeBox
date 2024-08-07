package com.example.bingebox.Screens

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
import com.example.bingebox.viewModel.LoadingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, userDao: UserDao, loadingViewModel: LoadingViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isLoading by loadingViewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize() .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    colorResource(id = R.color.red) ,
                    colorResource(id = R.color.appicon_background)
                )
            )
        ), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(16.dp))
            HeaderTextComponent(value = stringResource(id = R.string.login))
            Spacer(modifier = Modifier.height(40.dp))
            RoundedTextFieldEmail(text =username, labelvalue = stringResource(id = R.string.email), painterResource(id = R.drawable.profile), onTextChanged = { username = it}, keyboardType = KeyboardType.Email,)
            Spacer(modifier = Modifier.height(16.dp))
            RoundedTextFieldPassword( text = password,labelvalue = stringResource(id = R.string.password),
                painterResource = painterResource(id = R.drawable.password),onTextChanged = { password = it })
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                coroutineScope.launch {
                    loadingViewModel.showLoading()
                    delay(2000)
                    loadingViewModel.hideLoading()
                    when {
                        username.isBlank() -> errorMessage = "Email cannot be empty"
                        password.isBlank() -> errorMessage = "Password cannot be empty"
                        else -> {
                            val user = userDao.getUser(username, password)
                            if (user != null) {
                                navController.navigate(Routes.homeScreen)
                            } else {
                                errorMessage = "Invalid email or password"
                            }
                        }
                    }
                }
            }, colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = colorResource(id = R.color.appicon_background)
            )) {
                Text("Log In")
            }
            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row ( horizontalArrangement = Arrangement.SpaceBetween){
                SimpleTextComponent(value = stringResource(id = R.string.register))
                Spacer(modifier = Modifier.width(8.dp)) // Add spacing between the two components
                SimpleTextComponentClickable(value = stringResource(id = R.string.register_now),navController,Routes.signUpScreen)
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
            CircularProgressIndicator(color = Color.White)
        }
    }
    }

