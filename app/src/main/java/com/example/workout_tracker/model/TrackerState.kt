package com.example.workout_tracker.model

import com.example.workout_tracker.api.Workout
import java.time.LocalDate

data class TrackerState(
    val date: LocalDate = LocalDate.now(),
    val splitDay: String = "",
    val exercisesForSplitDay: List<String> = emptyList(),
    val splitCategories: List<String> = emptyList(),

    val showLoading: Boolean = false,
    val apiRequestMessage: String = "",
    val exerciseRecordsFromApi: List<Workout> = emptyList(),
    val showDateInExerciseRecordDisplay: Boolean = false
)
