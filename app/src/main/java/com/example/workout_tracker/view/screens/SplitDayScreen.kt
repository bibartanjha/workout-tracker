package com.example.workout_tracker.view.screens

import PopUpScreen
import PopUpScreenButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workout_tracker.utils.ErrorMessages.ERROR_FETCHING_SPLIT_CATEGORIES
import com.example.workout_tracker.utils.ErrorMessages.NO_SPLIT_CATEGORIES_FOUND
import com.example.workout_tracker.utils.OverlayPopUpScreen
import com.example.workout_tracker.utils.OverlayPopUpScreenLoading
import com.example.workout_tracker.viewmodel.TrackerViewModel


@Composable
fun SplitDayScreen(
    trackerViewModel: TrackerViewModel,
    splitDaySelected: () -> Unit
) {
    val trackerState by trackerViewModel.trackerState.collectAsState()

    LaunchedEffect(Unit) {
        if (trackerState.splitCategories.isEmpty()) {
            trackerViewModel.retrieveSplitCategories()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(red = 255, green = 203, blue = 90)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PopUpScreen(
                text = "Choose Split Day",
                remainingPopUpScreenContent = {
                    trackerState.splitCategories.forEach { buttonText ->
                        Spacer(modifier = Modifier.height(20.dp))
                        PopUpScreenButton(
                            text = buttonText,
                            bgColor = Color(red = 24, green = 95, blue = 150, alpha = 255),
                            onButtonSelection = {
                                trackerViewModel.setSplitDay(buttonText)
                                splitDaySelected()
                            }
                        )
                    }
                }
            )
        }

        if (trackerState.apiResponseLoading) {
            OverlayPopUpScreenLoading()
        } else if (trackerState.apiRequestMessage.isNotEmpty()) {
            OverlayPopUpScreen(text = trackerState.apiRequestMessage) {
                if (trackerState.apiRequestMessage.contains(ERROR_FETCHING_SPLIT_CATEGORIES)) {
                    Button(
                        onClick = {
                            trackerViewModel.retrieveSplitCategories()
                        }
                    ) {
                        Text("Retry")
                    }
                } else if (trackerState.apiRequestMessage.contains(NO_SPLIT_CATEGORIES_FOUND)) {
                    Button(
                        onClick = {
                            trackerViewModel.clearDisplayValuesFromApi()
                        }
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SplitDayScreenPreview() {
    SplitDayScreen(
        trackerViewModel = viewModel(),
        splitDaySelected = {}
    )
}