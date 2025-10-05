package com.example.workout_tracker.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.workout_tracker.api.ExerciseSet

class ExerciseSetUserInput(
    weight: String = "",
    reps: String = ""
) {
    var weight by mutableStateOf(weight)
    var reps by mutableStateOf(reps)

    fun convertToApiModel(): ExerciseSet? {
        return if (weight.isNotEmpty() && reps.isNotEmpty()) {
            ExerciseSet(
                weight = weight.toFloat(),
                reps = reps.toInt()
            )
        } else {
            null
        }
    }
}