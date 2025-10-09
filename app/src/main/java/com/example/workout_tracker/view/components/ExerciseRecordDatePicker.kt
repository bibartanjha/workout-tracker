package com.example.workout_tracker.view.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.example.workout_tracker.utils.DatePickerUtils.TIME_ZONE
import com.example.workout_tracker.utils.DatePickerUtils.convertDatePickerSelectedDateToLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseRecordDatePicker(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit, initialDate: LocalDate
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(TIME_ZONE).toInstant().toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        convertDatePickerSelectedDateToLocalDate(it)
                    }?.let {
                        onDateSelected(it)
                    }
                    onDismiss()
                },
            ) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}