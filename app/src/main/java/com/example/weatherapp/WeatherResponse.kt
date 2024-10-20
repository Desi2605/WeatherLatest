package com.example.weatherapp

// Data class to store weather information for both current and forecast
data class WeatherResponse(
    val data: List<WeatherData>
)

data class WeatherData(
    val app_temp: Double,        // Apparent temperature
    val city_name: String,       // City Name
    val weather: WeatherDescription,  // Nested WeatherDescription
    val rh: Double,              // Humidity
    val wind_spd: Double, // Wind speed
    val ob_time: String,
    val sunrise: String,   // Sunrise time
    val sunset: String,    // Sunset time
    val clouds: Int,       // Cloud coverage percentage
    val aqi: Int           // Air Quality Index
)

data class WeatherDescription(
    val description: String,     // Weather description (e.g., Thunderstorm)
    val code: Int,
    val data: List<WeatherData>// Weather code (this is the field for icons)
)



