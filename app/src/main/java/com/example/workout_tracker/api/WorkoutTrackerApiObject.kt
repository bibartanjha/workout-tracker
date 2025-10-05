package com.example.workout_tracker.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object WorkoutTrackerApiObject {
    private const val BASE_URL = "https://workout-tracker-api-ntq0.onrender.com/"

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)  // Allow slow startup
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val apiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WorkoutTrackerApi::class.java)

    suspend fun getSplitCategories(): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSplitCategories()
                if (response.isEmpty()) {
                    Log.i("WorkoutTrackerAPI", "No split categories found")
                }
                response
            } catch (e: Exception) {
                Log.e("WorkoutTrackerAPI", "Error fetching split categories", e)
                emptyList()
            }
        }
    }

    suspend fun getExercisesForSplitDay(splitDay: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getExercisesForSplitDay(splitDay)
                if (response.isEmpty()) {
                    Log.i("WorkoutTrackerAPI", "No exercises found for split day: $splitDay")
                }
                response
            } catch (e: Exception) {
                Log.e("WorkoutTrackerAPI", "Error fetching exercises", e)
                emptyList()
            }
        }
    }

    suspend fun addWorkout(workout: Workout): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                apiService.addWorkout(workout)
                true
            } catch (e: Exception) {
                Log.e("WorkoutTrackerAPI", "Error adding workout", e)
                false
            }
        }
    }
}