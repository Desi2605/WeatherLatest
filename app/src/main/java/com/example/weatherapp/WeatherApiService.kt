import com.example.weatherapp.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit interface to make API calls
interface WeatherApiService {

    // Fetch current weather by postcode and country
    @GET("v2.0/current")
    suspend fun getCurrentWeatherByPostcode(
        @Query("postal_code") postcode: String,
        @Query("key") apiKey: String,
        @Query("country") country: String = "MY"  // Country parameter set to Malaysia (MY)
    ): WeatherResponse

    companion object {
        private const val BASE_URL = "https://api.weatherbit.io/"

        // Create Retrofit instance
        fun create(): WeatherApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }

}
