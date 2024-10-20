package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.delay

class LandingPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                LandingPage {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Close LandingPageActivity so user can't return to it
                }
            }
        }
    }
}

@Composable
fun LandingPage(onStartClicked: () -> Unit) {
    var isButtonClicked by remember { mutableStateOf(false) }

    // Animate the button scale when clicked
    val buttonScale by animateFloatAsState(if (isButtonClicked) 0.9f else 1.0f)

    // Background with more vibrant gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(
                    Color(0xFF673AB7),  // Top purple gradient
                    Color(0xFF512DA8)   // Bottom darker purple
                )
                )
            )
    ) {
        // Central weather icon with padding
        Image(
            painter = painterResource(id = R.drawable.weather),
            contentDescription = "Weather Icon",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(300.dp)
                .padding(top = 100.dp)
        )

        // Text section with shadows for a glowing effect
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Weather",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                )
            )
            Text(
                text = "ForeCasts",
                fontSize = 34.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFFD700),
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                )
            )
        }

        // Animated "Get Start" button with scaling effect on press
        Button(
            onClick = {
                isButtonClicked = true
                onStartClicked()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .scale(buttonScale)
                .width(220.dp)
                .height(60.dp)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),  // Gold color
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text(
                text = "Get Start",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
