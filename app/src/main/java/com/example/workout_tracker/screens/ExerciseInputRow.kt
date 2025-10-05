package com.example.workout_tracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workout_tracker.utils.isDigitsOnly
import com.example.workout_tracker.utils.isFloatInput

@Composable
fun ExerciseInputRow(
    label: String,
    weight: String,
    onWeightChange: (String) -> Unit,
    reps: String,
    onRepsChange: (String) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp

    Column {
        Text(
            text = label,
            fontSize = (screenWidth / 20).sp,
            color = Color.White,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = weight,
                onValueChange = {
                        newText -> if (newText.isFloatInput()) onWeightChange(newText)
                },
                trailingIcon = {
                    Text(
                        text = "lbs",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.padding(8.dp).weight(1f)
            )

            TextField(
                value = reps,
                onValueChange = {
                        newText -> if (newText.isDigitsOnly()) onRepsChange(newText)
                },
                trailingIcon = {
                    Text(
                        text = "reps",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.padding(8.dp).weight(1f)
            )
        }
    }
}
