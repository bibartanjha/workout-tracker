package com.example.workout_tracker.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workout_tracker.utils.DatePickerUtils.getCurrentDate
import com.example.workout_tracker.utils.ErrorMessages.ERROR_FETCHING_EXERCISES_FOR_DATE
import com.example.workout_tracker.utils.ErrorMessages.ERROR_FETCHING_EXERCISES_FOR_SPLIT_DAY
import com.example.workout_tracker.utils.ErrorMessages.ERROR_FETCHING_RECENT_EXERCISES
import com.example.workout_tracker.utils.ErrorMessages.ERROR_SAVING_WORKOUT
import com.example.workout_tracker.utils.ErrorMessages.NO_EXERCISES_FOUND_FOR_DATE
import com.example.workout_tracker.utils.ErrorMessages.NO_EXERCISES_FOUND_FOR_SPLIT_DAY
import com.example.workout_tracker.utils.ErrorMessages.NO_RECENT_WORKOUTS_FOUND_FOR_EXERCISE
import com.example.workout_tracker.utils.ErrorMessages.WORKOUT_SUBMISSION_SUCCESSFUL
import com.example.workout_tracker.utils.ExerciseSetUserInput
import com.example.workout_tracker.utils.OverlayDatePicker
import com.example.workout_tracker.utils.OverlayPopUpScreen
import com.example.workout_tracker.utils.OverlayPopUpScreenLoading
import com.example.workout_tracker.view.components.ExerciseInputRow
import com.example.workout_tracker.view.components.ExerciseListDropdown
import com.example.workout_tracker.view.components.WorkoutRecordsDisplay
import com.example.workout_tracker.viewmodel.TrackerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(
    trackerViewModel: TrackerViewModel
) {
    val trackerState by trackerViewModel.trackerState.collectAsState()

    val minSets = 3
    val maxSets = 5
    val notesMaxCharacters = 100
    val notesMaxLines = 3

    var showDatePicker by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(getCurrentDate()) }

    // user inputs:
    var selectedExercise by remember { mutableStateOf<String?>(null) }
    val exerciseSets = remember { mutableStateListOf(*Array(minSets) { ExerciseSetUserInput() }) }
    var notes by remember { mutableStateOf("") }

    var scrollToTop by remember { mutableStateOf(false) }

    var setShowDateInExerciseRecordDisplay by remember { mutableStateOf(false) }

    fun resetInputs() {
        selectedExercise = trackerState.exercisesForSplitDay.firstOrNull()
        exerciseSets.clear()
        repeat(minSets) { exerciseSets.add(ExerciseSetUserInput()) }
        notes = ""
    }

    LaunchedEffect(Unit) {
        trackerViewModel.retrieveExercisesForSplitDay(trackerState.splitDay)
    }

    LaunchedEffect(trackerState.exercisesForSplitDay) {
        if (trackerState.exercisesForSplitDay.isNotEmpty()) {
            selectedExercise = trackerState.exercisesForSplitDay.firstOrNull()
        }
    }

    val listState = rememberLazyListState()

    if (scrollToTop) {
        LaunchedEffect(Unit) {
            listState.animateScrollToItem(0)
            scrollToTop = false
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                titleContentColor = Color.White,
            ), title = {
                Text(text = trackerState.splitDay + " Day")
            }, actions = {
                IconButton(onClick = {
                    showDatePicker = true
                }) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Select Date"
                    )
                }
            })
    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Color(red = 14, green = 81, blue = 114, alpha = 255)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Button(
                            onClick = {
                                setShowDateInExerciseRecordDisplay = false
                                trackerViewModel.retrieveExercisesByDate(date.toString())
                            },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("See all exercises done today")
                        }

                        Button(
                            onClick = {
                                selectedExercise?.let {
                                    setShowDateInExerciseRecordDisplay = true
                                    trackerViewModel.retrieveMostRecentWorkouts(it, 1)
                                }
                            },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("See last record for selected exercise")
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    ExerciseListDropdown(
                        exerciseList = trackerState.exercisesForSplitDay,
                        onExerciseSelected = { exercise ->
                            selectedExercise = exercise
                        },
                        initialSelectedOption = selectedExercise ?: ""
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                itemsIndexed(exerciseSets) { index, set ->
                    val setNumber = index + 1
                    ExerciseInputRow(
                        label = "Set $setNumber",
                        weight = set.weight,
                        onWeightChange = { set.weight = it },
                        reps = set.reps,
                        onRepsChange = { set.reps = it })
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    if (exerciseSets.size < maxSets) {
                        Button(
                            onClick = {
                                exerciseSets.add(ExerciseSetUserInput())
                            },
                            modifier = Modifier
                                .padding(
                                    start = 60.dp,
                                    end = 60.dp
                                )
                                .fillMaxWidth()
                        ) {
                            Text("Add Set")
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }

                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { text ->
                            if (text.length <= notesMaxCharacters) {
                                notes = text
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        maxLines = notesMaxLines,
                        label = {
                            Text(
                                color = Color.Black, text = "Notes"
                            )
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }

                item {
                    Button(
                        onClick = {
                            selectedExercise?.let {
                                trackerViewModel.addWorkout(
                                    exercise = it,
                                    date = date.toString(),
                                    sets = exerciseSets,
                                    notes = notes,
                                )
                            }
                        },
                        enabled = (!trackerState.apiResponseLoading),
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Submit")
                    }
                }
            }

            if (trackerState.apiResponseLoading) {
                OverlayPopUpScreenLoading()
            } else if (showDatePicker) {
                OverlayDatePicker(onDateSelected = { selectedDate ->
                    date = selectedDate
                    showDatePicker = false
                }, onDismiss = {
                    showDatePicker = false
                }, initialDate = date
                )
            } else if (trackerState.apiRequestMessage.isNotEmpty()) {
                val message = trackerState.apiRequestMessage
                if (message.contains(NO_EXERCISES_FOUND_FOR_SPLIT_DAY) ||
                    message.contains(NO_EXERCISES_FOUND_FOR_DATE) ||
                    message.contains(NO_RECENT_WORKOUTS_FOUND_FOR_EXERCISE) ||
                    message.contains(ERROR_SAVING_WORKOUT
                    )
                ) {
                    OverlayPopUpScreen(text = message,
                        showCloseButton = true,
                        onCloseButtonClick = {
                            trackerViewModel.clearDisplayValuesFromApi()
                        })
                } else {
                    val actionPair: Pair<String?, (() -> Unit)?> = when {
                        message.contains(ERROR_FETCHING_EXERCISES_FOR_SPLIT_DAY) -> "Retry" to {
                            trackerViewModel.retrieveExercisesForSplitDay(
                                trackerState.splitDay
                            )
                        }

                        message.contains(ERROR_FETCHING_EXERCISES_FOR_DATE) -> "Retry" to {
                            trackerViewModel.retrieveExercisesByDate(
                                date.toString()
                            )
                        }

                        message.contains(ERROR_FETCHING_RECENT_EXERCISES) -> "Retry" to {
                            selectedExercise?.let {
                                trackerViewModel.retrieveMostRecentWorkouts(it, 1)
                            }
                        }

                        message.contains(WORKOUT_SUBMISSION_SUCCESSFUL) -> "Submit another exercise" to {
                            resetInputs()
                            trackerViewModel.clearDisplayValuesFromApi()
                            scrollToTop = true
                        }

                        else -> null to null
                    }

                    val (buttonText, onClickAction) = actionPair

                    if (buttonText != null && onClickAction != null) {
                        OverlayPopUpScreen(text = message) {
                            Button(onClick = onClickAction) {
                                Text(buttonText)
                            }
                        }
                    }
                }
            } else if (trackerState.exerciseRecordsFromApi.isNotEmpty()) {
                OverlayPopUpScreen(
                    showCloseButton = true,
                    onCloseButtonClick = {
                        trackerViewModel.clearDisplayValuesFromApi()
                    }
                ) {
                    WorkoutRecordsDisplay(
                        trackerState.exerciseRecordsFromApi,
                        setShowDateInExerciseRecordDisplay
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ExercisesScreenPreview() {
    ExercisesScreen(
        trackerViewModel = viewModel()
    )
}