package com.example.workout_tracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_tracker.api.Workout
import com.example.workout_tracker.api.WorkoutTrackerApiObject
import com.example.workout_tracker.data.APIRequestState
import com.example.workout_tracker.utils.ExerciseSetUserInput
import com.example.workout_tracker.data.TrackerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class TrackerViewModel: ViewModel() {

    private val _trackerState = MutableStateFlow(TrackerState())
    val trackerState: StateFlow<TrackerState> = _trackerState.asStateFlow()

    fun setDate(selectedDate: LocalDate) {
        _trackerState.value = _trackerState.value.copy(date = selectedDate)
    }

    fun setSplitDay(selectedSplitDay: String) {
        _trackerState.value = _trackerState.value.copy(splitDay = selectedSplitDay)
    }

    fun initializeSplitCategories() {
        viewModelScope.launch {
            _trackerState.value = _trackerState.value.copy(apiRequestState = APIRequestState.Loading)
            val splitCategories = WorkoutTrackerApiObject.getSplitCategories()

            if (splitCategories.isEmpty()) {
                _trackerState.value = _trackerState.value.copy(apiRequestState = APIRequestState.Error)
            } else {
                _trackerState.value = _trackerState.value.copy(splitCategories = splitCategories)
                resetPostRequestState()
            }
        }
    }

    fun retrieveExercisesForSplitDay(selectedSplitDay: String) {
        viewModelScope.launch {
            _trackerState.value = _trackerState.value.copy(apiRequestState = APIRequestState.Loading)
            val exercises = WorkoutTrackerApiObject.getExercisesForSplitDay(selectedSplitDay)
            if (exercises.isEmpty()) {
                _trackerState.value = _trackerState.value.copy(apiRequestState = APIRequestState.Error)
            } else {
                _trackerState.value = _trackerState.value.copy(exercisesForSplitDay = exercises)
                resetPostRequestState()
            }
        }
    }

    fun resetPostRequestState() {
        _trackerState.value = _trackerState.value.copy(
            apiRequestState = APIRequestState.Idle
        )
    }

    fun addWorkout(
        exercise: String,
        date: String,
        notes: String,
        sets: List<ExerciseSetUserInput>
    ) {
        val workout = Workout(
            exercise = exercise,
            date = date,
            sets = sets.mapNotNull { set -> set.convertToApiModel() },
            notes = notes.ifEmpty { null }
        )

        viewModelScope.launch {
            _trackerState.value = _trackerState.value.copy(
                apiRequestState = APIRequestState.Loading
            )
            val addResultSuccess = WorkoutTrackerApiObject.addWorkout(workout)
            _trackerState.value = _trackerState.value.copy(
                apiRequestState =
                    if (addResultSuccess) {
                        APIRequestState.Success
                    } else {
                        APIRequestState.Error
                    }
            )
        }
    }
}