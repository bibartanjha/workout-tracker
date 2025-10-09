package com.example.workout_tracker.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object WorkoutTrackerApiObject {
    private const val BASE_URL = "https://workout-tracker-api-ntq0.onrender.com/"

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val apiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WorkoutTrackerApi::class.java)

    suspend fun getSplitCategories(): ApiResponseValue<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSplitCategories()
                ApiResponseValue(value = response)
            } catch (e: Exception) {
                ApiResponseValue(errorMessage = e.toString())
            }
        }
    }

    suspend fun getExercisesForSplitDay(splitDay: String): ApiResponseValue<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getExercisesForSplitDay(splitDay)
                ApiResponseValue(value = response)
            } catch (e: Exception) {
                ApiResponseValue(errorMessage = e.toString())
            }
        }
    }

    suspend fun getExercisesByDate(date: String): ApiResponseValue<List<Workout>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getWorkoutsByDate(date)
                ApiResponseValue(value = response)
            } catch (e: Exception) {
                ApiResponseValue(errorMessage = e.toString())
            }
        }
    }

    suspend fun getMostRecentWorkouts(
        exercise: String,
        numWorkouts: Int
    ): ApiResponseValue<List<Workout>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMostRecentWorkouts(exercise, numWorkouts)
                ApiResponseValue(value = response)
            } catch (e: Exception) {
                ApiResponseValue(errorMessage = e.toString())
            }
        }
    }

    suspend fun addWorkout(workout: Workout): ApiResponseValue<Workout?> {
        return withContext(Dispatchers.IO) {
            try {
                val addedWorkout = apiService.addWorkout(workout)
                ApiResponseValue(value = addedWorkout)
            } catch (e: Exception) {
                ApiResponseValue(errorMessage = e.toString())
            }
        }
    }
}