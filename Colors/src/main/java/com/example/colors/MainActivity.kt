package com.example.colors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colors.ui.theme.ColorsTheme

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Content()
                }
            }
        }
    }
}

fun isValid(value: String): Boolean {

    val regex = Regex(pattern = "[0-9a-fA-F]*")

    return value.length in 1..2 && value.all { regex.matches(it.toString()) }
}

@ExperimentalComposeUiApi
@Composable
fun Content() {
    val (red, setRed) = remember { mutableStateOf("ff") }
    val (green, setGreen) = remember { mutableStateOf("00") }
    val (blue, setBlue) = remember { mutableStateOf("ff") }


    val (color, setColor) = remember { mutableStateOf(Color(255, 0, 255)) }
    Scaffold(topBar = {
        TopAppBar() {
            Text(
                text = "Colors",
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.h6
            )
        }
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Create an RGB Color",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                //.border(border = BorderStroke(1.dp, Color.Black))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Add two hexadecimal characters between 0-9, A-F or a-f without the '#' for each channel")
            Spacer(modifier = Modifier.height(8.dp))
            ColorTextField(
                value = red,
                onValueChange = setRed,
                label = "Red",
                valid = isValid(red)
            )

            Spacer(modifier = Modifier.height(8.dp))
            ColorTextField(
                value = green,
                onValueChange = setGreen,
                label = "Green",
                valid = isValid(green)
            )

            Spacer(modifier = Modifier.height(8.dp))
            ColorTextField(
                value = blue,
                onValueChange = setBlue,
                label = "Blue",
                valid = isValid(blue)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    setColor(Color(red.toInt(16), green.toInt(16), blue.toInt(16)))
                },
                Modifier.fillMaxWidth(),
                enabled = listOf(red, green, blue).all { isValid(it) }
            ) {
                Text(text = "CREATE RGB COLOR")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = color,
            ) {

                Text(
                    text = "Created color display panel",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

    }

}

@ExperimentalComposeUiApi
@Composable
fun ColorTextField(value: String, onValueChange: (String) -> Unit, label: String, valid: Boolean) {
    val color: Color =
        when (label) {
            "Blue" -> Color.Blue
            "Green" -> Color.Green
            "Red" -> Color.Red
            else -> Color.Unspecified
        }
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value,
        onValueChange,
        singleLine = true,
        label = { Text(text = "$label Channel") },
        isError = !valid,
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .border(
                1.dp, if (!valid) {
                    Color.Unspecified
                } else {
                    color
                }
            ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = color,
            focusedLabelColor = color
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        })
    )
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ColorsTheme {
        Content()
    }
}