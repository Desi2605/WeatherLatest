package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpScreen()
        }
    }
}

@Composable
fun SignUpScreen() {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    val weatherService = WeatherApiService.create()

    var inputUsername by remember { mutableStateOf("") }
    var inputAge by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var inputPostcode by remember { mutableStateOf("") }
    var inputCity by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Background gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6A5ACD), // Dark blue
                        Color(0xFF7B68EE)  // Lighter blue
                    )
                )
            )
    ) {
        // Content with the sign-up form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cloudy image above the card
            Image(
                painter = painterResource(id = R.drawable.cloudy),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card for the form fields
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
                    .offset(y = (50).dp),  // Offset to make the card float over the background
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White // Set the card color to white
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp), // Padding inside the card
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title text
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Username input field
                    OutlinedTextField(
                        value = inputUsername,
                        onValueChange = { inputUsername = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password input field
                    OutlinedTextField(
                        value = inputPassword,
                        onValueChange = { inputPassword = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Age input field
                    OutlinedTextField(
                        value = inputAge,
                        onValueChange = { inputAge = it },
                        label = { Text("Age") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Postcode input field
                    OutlinedTextField(
                        value = inputPostcode,
                        onValueChange = { inputPostcode = it },
                        label = { Text("Postcode") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Automatically fetch city based on postal code
                    LaunchedEffect(inputPostcode) {
                        if (inputPostcode.isNotEmpty()) {
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    val response = weatherService.getCurrentWeatherByPostcode(
                                        inputPostcode,
                                        "8cf835bceafa4da2840e4fb00599c21f"
                                    )
                                    if (response.data.isNotEmpty()) {
                                        inputCity = response.data.first().city_name
                                    } else {
                                        errorMessage = "Invalid postcode"
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Failed to fetch city data"
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    }

                    // City input field (auto-filled)
                    OutlinedTextField(
                        value = inputCity,
                        onValueChange = { inputCity = it },
                        label = { Text("City (Auto-filled)") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLoading) {
                        CircularProgressIndicator()
                    }

                    // Sign Up button
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (inputUsername.isEmpty() || inputPassword.isEmpty() || inputAge.isEmpty()) {
                                    errorMessage = "Please fill all fields"
                                    return@launch
                                }

                                val age = inputAge.toIntOrNull() ?: 0
                                val user = User(
                                    id = 0,
                                    username = inputUsername,
                                    password = inputPassword,
                                    age = age,
                                    postcode = inputPostcode,
                                    city = inputCity,
                                    country = "Malaysia"
                                )

                                val userId = userDao.insertUser(user)

                                if (userId > 0) {
                                    // Navigate to sign-in screen after successful sign-up
                                    val intent = Intent(context, SignInActivity::class.java)
                                    context.startActivity(intent)
                                } else {
                                    errorMessage = "Sign up failed, try again"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign Up")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (errorMessage.isNotEmpty()) {
                        Text(errorMessage, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
