package com.example.workout_tracker.model

import com.example.workout_tracker.api.Workout

data class TrackerState(
    val splitDay: String = "",
    val exercisesForSplitDay: List<String> = emptyList(),
    val splitCategories: List<String> = emptyList(),

    val apiResponseLoading: Boolean = false,
    val apiRequestMessage: String = "",
    val exerciseRecordsFromApi: List<Workout> = emptyList(),
)
