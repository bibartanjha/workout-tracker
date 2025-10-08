package com.example.workout_tracker.api

data class ExerciseSet(
    val weight: Float,
    val reps: Int
)

data class Workout(
    val exercise: String,
    val date: String,
    val notes: String? = null,
    val sets: List<ExerciseSet> = emptyList()
)