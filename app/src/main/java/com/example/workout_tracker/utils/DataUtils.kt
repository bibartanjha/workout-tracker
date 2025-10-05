package com.example.workout_tracker.utils

fun String.isFloatInput(): Boolean {
    val floatRegex = "^-?\\d*\\.?\\d*".toRegex()
    return this.matches(floatRegex)
}

fun String.isDigitsOnly(): Boolean {
    return this.all { it.isDigit() }
}