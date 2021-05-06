package com.example.planetquiz

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.planetquiz.ui.theme.PlanetQuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanetQuizTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavComposeApp()
                }
            }
        }
    }
}

@Composable
fun Content(title: String, list: List<String>, answer: String?, onClick: (String) -> Unit) {
    Column() {
        Text(
            text = title,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        list.forEach {
            CardButton(text = it) { onClick(it) }
        }
        if (!answer.isNullOrBlank()) {
            Text(
                text = answer,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun CardButton(text: String, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(text) },
        backgroundColor = Color.LightGray,

        ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1
        )
    }
}


@Preview
@Composable
fun ContentPreview() {
    Content("Preview", listOf("A", "B", "C"), null) {}
}