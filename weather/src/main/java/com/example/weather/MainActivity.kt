package com.example.weather

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.ui.theme.BuildingComposeTheme
import com.google.accompanist.glide.rememberGlidePainter

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Main", "onCreate")


        setContent {
            BuildingComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Content()
                }
            }
        }
    }

}

@Composable
fun Content() {
    val viewModel: WeatherViewModel = viewModel()
    val weather = viewModel.weather

    Scaffold(topBar = {
        TopAppBar() {
            Text(
                if (weather is Weather.OnSuccess) weather.locationName else "Weather",
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.h5
            )
        }
    }) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            when (weather) {
                is Weather.OnSuccess -> {
                    Column() {
                        Text(
                            "${weather.locationName} weather",
                            style = MaterialTheme.typography.body1
                        )
                        Text(weather.status, style = MaterialTheme.typography.body2)
                        Text(weather.description, style = MaterialTheme.typography.caption)
                    }
                    Box(modifier = Modifier.padding(end = 8.dp).height(48.dp)) {
                        Image(
                            painter = rememberGlidePainter(
                                request = "https://openweathermap.org/img/wn/${weather.icon}@2x.png"
                            ), contentDescription = null
                        )
                    }
                }
                Weather.OnLoading -> Text(text = "Loading")
                is Weather.OnError -> Text(text = weather.e)
            }

        }
    }
}

@Preview
@Composable
fun ContentPreview() {
    Content()
}