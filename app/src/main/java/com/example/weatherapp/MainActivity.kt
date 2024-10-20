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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                LandingPage()
            }
        }
    }
}

@Composable
fun LandingPage() {
    val context = LocalContext.current

    // Background gradient colors for the entire page
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF6C63FF), Color(0xFFB4AEE8)),  // Custom purple gradient
        startY = 0f,
        endY = 1000f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient) // Apply the gradient to the entire page background
    ) {
        // Top section with sun/cloud icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.sun),
                contentDescription = "Weather Icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(200.dp)
            )
        }

        // Floating card section for buttons, offset to overlap with the top section
        Card(
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .offset(y = (350).dp),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.elevatedCardElevation(20.dp),
            colors = CardDefaults.cardColors( // Set the card color to white with some transparency
                containerColor = Color.White.copy(alpha = 0.9f) // Adding slight transparency for a softer effect
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Text for "Welcome Back"
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Sign In button
                Button(
                    onClick = {
                        val intent = Intent(context, SignInActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "Sign In")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sign Up button
                Button(
                    onClick = {
                        val intent = Intent(context, SignUpActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))  // Green color for Sign Up
                ) {
                    Text(text = "Sign Up")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    WeatherAppTheme {
        LandingPage()
    }
}
