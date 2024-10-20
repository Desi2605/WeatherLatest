@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username") ?: "Guest"  // Fetch only the username

        setContent {
            MainScreen(username = username)  // Pass only the username
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(username: String) {
    var selectedTab by remember { mutableStateOf(0) }
    var postcode by remember { mutableStateOf("57000") }  // Default postcode

    Scaffold(
        bottomBar = {
            NavigationBar(selectedTab) { selectedTab = it }
        }
    ) {
        when (selectedTab) {
            0 -> WeatherScreen(postcode = postcode)  // Weather Screen
            1 -> ModernProfileScreen(username = username) { fetchedPostcode ->
                postcode = fetchedPostcode
            }
        }
    }
}

@Composable
fun NavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Weather") },
            label = { Text("Weather") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
    }
}

@Composable
fun WeatherScreen(postcode: String) {
    val weatherService = WeatherApiService.create()
    val coroutineScope = rememberCoroutineScope()

    // State variables to hold the fetched data
    var appTemp by remember { mutableStateOf<Double?>(null) }  // Apparent temperature
    var city by remember { mutableStateOf("") }  // City name
    var description by remember { mutableStateOf("") }  // Weather description
    var humidity by remember { mutableStateOf<Double?>(null) }  // Humidity
    var windSpeed by remember { mutableStateOf<Double?>(null) }  // Wind speed
    var errorMessage by remember { mutableStateOf<String?>(null) }  // Error message, if any
    var weatherCode by remember { mutableStateOf<Int?>(null) }
    var obTime by remember { mutableStateOf("") }
    var sunrise by remember { mutableStateOf("") }
    var sunset by remember { mutableStateOf("") }
    var clouds by remember { mutableStateOf(0) }
    var aqi by remember { mutableStateOf(0) }

    LaunchedEffect(postcode) {
        coroutineScope.launch {
            try {
                val weatherResponse = weatherService.getCurrentWeatherByPostcode(
                    postcode = postcode,
                    apiKey = "31fa14f4b6184504819f16366c3f7b90"
                )
                val currentWeatherData = weatherResponse.data.first()

                appTemp = currentWeatherData.app_temp
                city = currentWeatherData.city_name
                description = currentWeatherData.weather.description
                humidity = currentWeatherData.rh
                windSpeed = currentWeatherData.wind_spd
                obTime = currentWeatherData.ob_time
                weatherCode = currentWeatherData.weather.code
                sunrise = currentWeatherData.sunrise
                sunset = currentWeatherData.sunset
                clouds = currentWeatherData.clouds
                aqi = currentWeatherData.aqi
            } catch (e: Exception) {
                errorMessage = "Error fetching weather data: ${e.message}"
            }
        }
    }

    //   gradient
    val weatherGradient = getWeatherGradientByCode(weatherCode ?: 800)

    // Apply the gradient to both Surface and Card
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = weatherGradient)
            .padding(16.dp)
            // Padding for entire screen
    ) {
        if (errorMessage != null) {
            Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.padding(16.dp))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Card that contains the top section, applying the gradient
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp)
                        .padding(vertical = 16.dp),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.elevatedCardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent  // Set container color to transparent
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(brush = weatherGradient)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // City Name
                        Text(
                            text = city,
                            color = Color.Black,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        // Weather Icon
                        Image(
                            painter = painterResource(id = getWeatherIconByCode(weatherCode ?: 0)),
                            contentDescription = "Weather Icon",
                            modifier = Modifier.size(150.dp).padding(top = 8.dp)
                        )

                        // Temperature
                        Text(
                            text = "${appTemp ?: "Loading..."}°C",
                            color = Color.Black,
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        // Weather description and date
                        Text(
                            text = description,
                            color = Color.Black,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = formatObTime(obTime),
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        // Humidity and Wind Speed inside the card
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            WeatherInfoBox("${humidity ?: "Loading..."}%", "Humidity", R.drawable.humidity)
                            WeatherInfoBox("${windSpeed ?: "Loading..."} km/h", "Wind Speed", R.drawable.wind)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                WeatherDetailsRow(sunrise, sunset, clouds, aqi)
            }
        }
    }
}



fun formatObTime(obTime: String): String {
    return try {
        // Parse the API date string to a Date object
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date: Date = parser.parse(obTime) ?: return "Unknown Date"

        // Format the Date object to a readable string like "Wednesday, 28 Aug, 16:45"
        val formatter = SimpleDateFormat("EEEE, dd MMM, HH:mm", Locale.getDefault())
        formatter.format(date)
    } catch (e: Exception) {
        "Invalid Date"
    }
}

@Composable
fun WeatherInfoBox(value: String, label: String, iconId: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Text(text = value, color = Color.Black, fontSize = 18.sp)
        Text(text = label, color = Color.Black.copy(alpha = 0.7f), fontSize = 14.sp)
    }
}

@Composable
fun WeatherDetailsRow(sunrise: String, sunset: String, clouds: Int, aqi: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),  // Enable vertical scrolling
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // First row: Sunrise and Sunset with icons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherDetailBoxWithIcon(iconResId = R.drawable.sunrise, value = sunrise, Modifier.weight(1f))
            WeatherDetailBoxWithIcon(iconResId = R.drawable.sunset, value = sunset, Modifier.weight(1f))
        }

        // Second row: Cloud Coverage and AQI with icons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherDetailBoxWithIcon(iconResId = R.drawable.clouds, value = "$clouds%", Modifier.weight(1f))
            WeatherDetailBoxWithIcon(iconResId = R.drawable.air, value = "$aqi", Modifier.weight(1f))
        }
    }
}

@Composable
fun WeatherDetailBoxWithIcon(iconResId: Int, value: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color(0x33FFFFFF), shape = MaterialTheme.shapes.medium) // Transparent white background to blend with gradient
            .border(1.dp, Color.White, shape = MaterialTheme.shapes.medium) // White border
            .padding(16.dp)
    ) {
        // Icon
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Value displayed under the icon
        Text(text = value, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}




@Composable
fun ForecastBox(temp: String, iconId: Int, time: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(temp, color = Color.White, fontSize = 18.sp)
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Text(time, color = Color.Black.copy(alpha = 0.7f), fontSize = 14.sp)
    }
}

fun getWeatherIconByCode(code: Int): Int {
    return when (code) {
        200, 201, 202, 230, 231, 232 -> R.drawable.thunderstorm   // Thunderstorm codes
        300, 301, 302, 310, 311, 312, 313, 314, 321 -> R.drawable.drizzle   // Drizzle codes
        500, 501, 502, 503, 504 -> R.drawable.rain   // Rain codes
        600, 601, 602, 611, 612, 613, 615, 616 -> R.drawable.snow   // Snow codes
        800 -> R.drawable.sun  // Clear sky
        801, 802, 803, 804 -> R.drawable.cloudy   // Cloudy codes
        else -> R.drawable.sun  // Default/fallback icon
    }
}

fun getWeatherGradientByCode(code: Int): Brush {
    return when (code) {
        200, 201, 202, 230, 231, 232 -> Brush.verticalGradient(
            colors = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
        )
        300, 301, 302, 310, 311, 312, 313, 314, 321 -> Brush.verticalGradient(
            colors = listOf(Color(0xFF4CA1AF), Color(0xFFC4E0E5))
        )
        500, 501, 502, 503, 504 -> Brush.verticalGradient(
            colors = listOf(Color(0xFF000046), Color(0xFF1CB5E0))
        )
        600, 601, 602, 611, 612, 613, 615, 616 -> Brush.verticalGradient(
            colors = listOf(Color(0xFF83A4D4), Color(0xFFB6FBFF))
        )
        800 -> Brush.verticalGradient(
            colors = listOf(Color(0xFF2980B9), Color(0xFF6DD5FA), Color(0xFFFFFFFF))
        )
        801, 802, 803, 804 -> Brush.verticalGradient(
            colors = listOf(Color(0xFF757F9A), Color(0xFFD7DDE8))
        )
        else -> Brush.verticalGradient(
            colors = listOf(Color(0xFF2980B9), Color(0xFF6DD5FA))
        )
    }
}

@Composable
fun ModernProfileScreen(username: String, onPostcodeFetched: (String) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var userProfile by remember { mutableStateOf<User?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(username) {
        coroutineScope.launch {
            try {
                val userDao = AppDatabase.getDatabase(context).userDao()
                val user = userDao.getUserByUsername(username)
                userProfile = user

                onPostcodeFetched(user?.postcode ?: "57000")  // Use "57000" if postcode is null
            } catch (e: Exception) {
                errorMessage = "Error fetching user profile: ${e.message}"
            }
        }
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF)) // Gradient background
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
            .padding(16.dp)
    ) {
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = "USER PROFILE",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.padding(16.dp))
            } else if (userProfile != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    ModernProfileField(label = "Username", value = userProfile?.username ?: "N/A", labelColor = Color.Black)
                    ModernPasswordField(label = "Password", value = userProfile?.password ?: "N/A", labelColor = Color.Black)
                    ModernProfileField(label = "Age", value = userProfile?.age?.toString() ?: "N/A", labelColor = Color.Black)
                    ModernProfileField(label = "Postcode", value = userProfile?.postcode ?: "N/A", labelColor = Color.Black)
                    ModernProfileField(label = "City", value = userProfile?.city ?: "N/A", labelColor = Color.Black)
                    ModernProfileField(label = "Country", value = userProfile?.country ?: "Malaysia", labelColor = Color.Black)

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { /* Handle logout action */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 40.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))  // Button color
                    ) {
                        Text("Logout", color = Color.White)
                    }
                }
            } else {
                Text(text = "Loading profile...", color = Color.White, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun ModernProfileField(label: String, value: String, labelColor: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = labelColor,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .shadow(2.dp, shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Text(text = value, color = Color.Black, fontSize = 16.sp) // Text color updated to black
        }
    }
}

@Composable
fun ModernPasswordField(label: String, value: String, labelColor: Color) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = labelColor,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .shadow(2.dp, shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isPasswordVisible) value else "••••••••",
                    color = Color.Black, // Text color updated to black
                    fontSize = 16.sp
                )
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                        contentDescription = "Toggle Password Visibility",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
