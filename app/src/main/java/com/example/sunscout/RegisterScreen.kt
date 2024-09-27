package com.example.sunscout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sunscout.ui.theme.ButtonColor
import com.example.sunscout.ui.theme.LightRed
import com.example.sunscout.ui.theme.Sun
import com.example.sunscout.ui.theme.Wine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    firebaseAuthService: FirebaseAuthService
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightRed, Wine)
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Register",
            fontSize = 50.sp,
            color = ButtonColor,
            modifier = Modifier
                .padding(20.dp)
                .padding(bottom = 32.dp)

        )
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(ButtonColor),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ButtonColor,
                unfocusedContainerColor = ButtonColor,
                disabledContainerColor = ButtonColor,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = Sun,
            )
        )
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ButtonColor,
                unfocusedContainerColor = ButtonColor,
                disabledContainerColor = ButtonColor,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = Sun,
            )
        )
        TextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ButtonColor,
                unfocusedContainerColor = ButtonColor,
                disabledContainerColor = ButtonColor,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = Sun,
            )
        )
        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Button(
            onClick = {
                if (password.value == confirmPassword.value) {
                    scope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                firebaseAuthService.register(email.value, password.value)
                            }
                            withContext(Dispatchers.Main) {
                                navController.navigate("select_skin_type") {
                                    popUpTo("greeting") { inclusive = true }
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                errorMessage.value = e.message ?: "Registration failed"
                            }
                        }
                    }
                } else {
                    errorMessage.value = "Passwords do not match"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            modifier = Modifier
                .size(width = 350.dp, height = 70.dp)
                .padding(10.dp)
        ) {
            Text("Register", style = TextStyle(fontSize = 17.sp), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.sun),
            contentDescription = "image of sunhat",
            modifier = Modifier.size(150.dp)
        )
    }
}
