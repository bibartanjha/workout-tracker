package com.example.workout_tracker.data

import java.time.LocalDate

data class TrackerState(
    val date: LocalDate = LocalDate.now(),
    val splitDay: String = "",
    val exercisesForSplitDay: List<String> = emptyList(),
    val splitCategories: List<String> = emptyList(),
    val apiRequestState: APIRequestState = APIRequestState.Idle
)
