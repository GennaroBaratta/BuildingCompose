package com.example.weather

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.weather.api.OpenWeatherMapService
import com.example.weather.model.OpenWeatherMapResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class WeatherViewModel : ViewModel() {
    var weather: Weather by mutableStateOf(Weather.OnLoading)
        private set

    private val weatherApiService: OpenWeatherMapService

    private fun handleError(message: String) {
        //Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT).show()
        weather = Weather.OnError(message)
    }

    private fun handleValidResponse(response: OpenWeatherMapResponseData) {
        response.weather.firstOrNull()?.let {
            weather = Weather.OnSuccess(
                locationName = response.locationName,
                status = it.status,
                description = it.description,
                icon = it.icon
            )

        }
    }

    fun handleResponse(response: Response<OpenWeatherMapResponseData>) {
        Log.d("Main", "HandleResponse $response")
        if (response.isSuccessful) {
            response.body()?.let { validResponse ->
                handleValidResponse(validResponse)
            } ?: Unit
        } else {
            handleError("Response was unsuccessful: ${response.errorBody()}")
        }
    }

    init {
        val retrofit: Retrofit =
            Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        weatherApiService =
            retrofit.create(OpenWeatherMapService::class.java)

        weatherApiService
            .getWeather("New York", BuildConfig.OpenWeatherApiKey)
            .enqueue(
                object : Callback<OpenWeatherMapResponseData> {
                    override fun onResponse(
                        call: Call<OpenWeatherMapResponseData>,
                        response: Response<OpenWeatherMapResponseData>
                    ) = handleResponse(response)

                    override fun onFailure(call: Call<OpenWeatherMapResponseData>, t: Throwable) {
                        handleError("Response failed: ${t.message}")
                    }

                })
    }
}


sealed class Weather {
    data class OnSuccess(
        val locationName: String,
        val status: String,
        val description: String,
        val icon: String
    ) : Weather()

    object OnLoading : Weather()

    data class OnError(val e: String) : Weather()
}
