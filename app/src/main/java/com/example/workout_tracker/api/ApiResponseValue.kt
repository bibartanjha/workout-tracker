package com.example.workout_tracker.api

class ApiResponseValue<T>(
    val value: T? = null,
    val errorMessage: String = ""
)