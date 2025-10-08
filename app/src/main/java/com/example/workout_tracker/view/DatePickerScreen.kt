package com.example.workout_tracker.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workout_tracker.viewmodel.TrackerViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerScreen(
    trackerViewModel: TrackerViewModel,
    onDateSelected: () -> Unit
) {
    val selectedZone = ZoneId.of("America/Chicago")
    val todayMilli = remember {
        LocalDate.now()
            .atStartOfDay(selectedZone)
            .toInstant()
            .toEpochMilli()
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todayMilli
    )
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
                DatePickerDialog(
                    onDismissRequest = { },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val selectedDate = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneOffset.UTC)
                                        .toLocalDate()
                                    trackerViewModel.setDate(selectedDate)
                                    onDateSelected()
                                }
                            }
                        ) {
                            Text("OK")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
        }
    }

}

@Preview
@Composable
fun DatePickerScreenPreview() {
    DatePickerScreen (
        trackerViewModel = viewModel(),
        onDateSelected = {}
    )
}