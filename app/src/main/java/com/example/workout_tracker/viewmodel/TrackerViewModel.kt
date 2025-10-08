package com.example.workout_tracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_tracker.api.Workout
import com.example.workout_tracker.api.WorkoutTrackerApiObject
import com.example.workout_tracker.utils.ExerciseSetUserInput
import com.example.workout_tracker.model.TrackerState
import com.example.workout_tracker.utils.ErrorMessages.ERROR_FETCHING_EXERCISES_FOR_DATE
import com.example.workout_tracker.utils.ErrorMessages.ERROR_FETCHING_EXERCISES_FOR_SPLIT_DAY
import com.example.workout_tracker.utils.ErrorMessages.ERROR_FETCHING_RECENT_EXERCISES
import com.example.workout_tracker.utils.ErrorMessages.ERROR_FETCHING_SPLIT_CATEGORIES
import com.example.workout_tracker.utils.ErrorMessages.ERROR_SAVING_WORKOUT
import com.example.workout_tracker.utils.ErrorMessages.NO_EXERCISES_FOUND_FOR_DATE
import com.example.workout_tracker.utils.ErrorMessages.NO_EXERCISES_FOUND_FOR_SPLIT_DAY
import com.example.workout_tracker.utils.ErrorMessages.NO_RECENT_WORKOUTS_FOUND_FOR_EXERCISE
import com.example.workout_tracker.utils.ErrorMessages.NO_SPLIT_CATEGORIES_FOUND
import com.example.workout_tracker.utils.ErrorMessages.WORKOUT_SUBMISSION_SUCCESSFUL
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

    fun setShowDateInExerciseRecordDisplay(showDate: Boolean) {
        _trackerState.value = _trackerState.value.copy(showDateInExerciseRecordDisplay = showDate)
    }

    fun clearDisplayValuesFromApi() {
        _trackerState.value = _trackerState.value.copy(
            apiRequestMessage = "",
            exerciseRecordsFromApi = emptyList()
        )
    }

    fun retrieveSplitCategories() {
        viewModelScope.launch {
            _trackerState.value = _trackerState.value.copy(showLoading = true)

            clearDisplayValuesFromApi()

            val splitCategoriesResponse = WorkoutTrackerApiObject.getSplitCategories()

            if (splitCategoriesResponse.errorMessage.isNotEmpty()) {
                _trackerState.value = _trackerState.value.copy(
                    apiRequestMessage = "$ERROR_FETCHING_SPLIT_CATEGORIES ${splitCategoriesResponse.errorMessage}"
                )
            } else {
                if (splitCategoriesResponse.value.isNullOrEmpty()) {
                    _trackerState.value =
                        _trackerState.value.copy(apiRequestMessage = NO_SPLIT_CATEGORIES_FOUND)
                } else {
                    _trackerState.value =
                        _trackerState.value.copy(splitCategories = splitCategoriesResponse.value)
                }
            }

            _trackerState.value = _trackerState.value.copy(showLoading = false)
        }
    }

    fun retrieveExercisesForSplitDay(selectedSplitDay: String) {
        viewModelScope.launch {
            _trackerState.value = _trackerState.value.copy(showLoading = true)

            clearDisplayValuesFromApi()

            val exercisesForSplitDayResponse = WorkoutTrackerApiObject.getExercisesForSplitDay(selectedSplitDay)

            if (exercisesForSplitDayResponse.errorMessage.isNotEmpty()) {
                _trackerState.value = _trackerState.value.copy(
                    apiRequestMessage = "$ERROR_FETCHING_EXERCISES_FOR_SPLIT_DAY $selectedSplitDay: ${exercisesForSplitDayResponse.errorMessage}"
                )
            } else {
                if (exercisesForSplitDayResponse.value.isNullOrEmpty()) {
                    _trackerState.value =
                        _trackerState.value.copy(apiRequestMessage = "$NO_EXERCISES_FOUND_FOR_SPLIT_DAY: $selectedSplitDay")
                } else {
                    _trackerState.value = _trackerState.value.copy(exercisesForSplitDay = exercisesForSplitDayResponse.value)
                }
            }

            _trackerState.value = _trackerState.value.copy(showLoading = false)
        }
    }

    fun retrieveExercisesByDate(date: String) {
        viewModelScope.launch {
            _trackerState.value = _trackerState.value.copy(showLoading = true)

            clearDisplayValuesFromApi()

            val exercisesByDateResponse = WorkoutTrackerApiObject.getExercisesByDate(date)

            if (exercisesByDateResponse.errorMessage.isNotEmpty()) {
                _trackerState.value = _trackerState.value.copy(
                    apiRequestMessage = "$ERROR_FETCHING_EXERCISES_FOR_DATE $date: ${exercisesByDateResponse.errorMessage}"
                )
            } else {
                if (exercisesByDateResponse.value.isNullOrEmpty()) {
                    _trackerState.value =
                        _trackerState.value.copy(apiRequestMessage = "$NO_EXERCISES_FOUND_FOR_DATE: $date")
                } else {
                    _trackerState.value = _trackerState.value.copy(exerciseRecordsFromApi = exercisesByDateResponse.value)
                }
            }

            _trackerState.value = _trackerState.value.copy(showLoading = false)
        }
    }

    fun retrieveMostRecentWorkouts(exercise: String, numWorkouts: Int) {
        viewModelScope.launch {
            _trackerState.value = _trackerState.value.copy(showLoading = true)

            clearDisplayValuesFromApi()

            val mostRecentWorkoutsResponse = WorkoutTrackerApiObject.getMostRecentWorkouts(exercise, numWorkouts)

            if (mostRecentWorkoutsResponse.errorMessage.isNotEmpty()) {
                _trackerState.value = _trackerState.value.copy(
                    apiRequestMessage = "$ERROR_FETCHING_RECENT_EXERCISES for $exercise: ${mostRecentWorkoutsResponse.errorMessage}"
                )
            } else {
                if (mostRecentWorkoutsResponse.value.isNullOrEmpty()) {
                    _trackerState.value =
                        _trackerState.value.copy(apiRequestMessage = "$NO_RECENT_WORKOUTS_FOUND_FOR_EXERCISE: $exercise")
                } else {
                    _trackerState.value = _trackerState.value.copy(exerciseRecordsFromApi = mostRecentWorkoutsResponse.value)
                }
            }

            _trackerState.value = _trackerState.value.copy(showLoading = false)
        }
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
            _trackerState.value = _trackerState.value.copy(showLoading = true)

            clearDisplayValuesFromApi()

            val addResultResponse = WorkoutTrackerApiObject.addWorkout(workout)

            if (addResultResponse.errorMessage.isNotEmpty()) {
                _trackerState.value = _trackerState.value.copy(apiRequestMessage = "$ERROR_SAVING_WORKOUT: ${addResultResponse.errorMessage}")
            } else {
                _trackerState.value = _trackerState.value.copy(apiRequestMessage = WORKOUT_SUBMISSION_SUCCESSFUL)
            }

            _trackerState.value = _trackerState.value.copy(showLoading = false)
        }
    }
}