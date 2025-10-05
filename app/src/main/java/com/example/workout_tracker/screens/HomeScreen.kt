package com.example.workout_tracker.screens

import PopUpScreen
import PopUpScreenButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workout_tracker.navigation.Routes

@Composable
fun HomeScreen(
    onOptionSelected: (optionName: String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(red = 255, green = 203, blue = 90)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val buttonTexts = listOf("New Day", "See Past Records")

            PopUpScreen("Main Menu", Color.LightGray, Color.Black) {
                for (buttonText in buttonTexts) {
                    Spacer(modifier = Modifier.height(20.dp))
                    PopUpScreenButton(
                        text = buttonText,
                        bgColor = Color(red = 24, green = 95, blue = 150, alpha = 255),
                        onButtonSelection = {
                            if (it == "New Day") {
                                onOptionSelected(Routes.DATE_PICKER_SCREEN)
                            } else {
                            }
                        }
                    )
                }
            }
        }

    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onOptionSelected = {}
    )
}