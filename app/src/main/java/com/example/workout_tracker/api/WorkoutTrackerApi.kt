package com.example.workout_tracker.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WorkoutTrackerApi {
    @GET("/categories")
    suspend fun getSplitCategories(): List<String>

    @GET("/exercises_in_category")
    suspend fun getExercisesForSplitDay(
        @Query("split_category") splitDay: String
    ): List<String>

    @GET("workouts_by_date")
    suspend fun getWorkoutsByDate(
        @Query("date") date: String
    ): List<Workout>

    @GET("most_recent_workouts")
    suspend fun getMostRecentWorkouts(
        @Query("exercise") exercise: String,
        @Query("num_workouts") numWorkouts: Int
    ): List<Workout>

    @POST("/add_workout")
    suspend fun addWorkout(
        @Body workout: Workout
    ): Workout
}