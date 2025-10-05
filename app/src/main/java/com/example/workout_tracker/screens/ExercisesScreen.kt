package com.example.workout_tracker.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workout_tracker.data.APIRequestState
import com.example.workout_tracker.utils.ExerciseSetUserInput
import com.example.workout_tracker.utils.OverlayMenuScreenLoading
import com.example.workout_tracker.utils.OverlayMenuScreenWithPopupScreen
import com.example.workout_tracker.viewmodel.TrackerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen (
    trackerViewModel: TrackerViewModel
) {
    val trackerState by trackerViewModel.trackerState.collectAsState()

    // constants:
    val minSets = 3
    val maxSets = 5
    val notesMaxCharacters = 100



    // user inputs:
    var selectedExercise by remember { mutableStateOf<String?>(null) }
    val exerciseSets = remember { mutableStateListOf(*Array(minSets) { ExerciseSetUserInput() }) }
    var notes by remember { mutableStateOf("") }

    // state specific to display:
    var dropdownExpanded by remember { mutableStateOf(false) }
    var scrollToTop by remember { mutableStateOf(false) }



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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = trackerState.date.toString() + " - " + trackerState.splitDay)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    titleContentColor = Color.White,
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Color(red = 14, green = 81, blue = 114, alpha = 255)
        ) {
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("See all exercises done today")
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Text(
                        text = "Select Exercise:",
                        fontSize = (screenWidth / 20).sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                }

                item {
                    ExposedDropdownMenuBox(
                        expanded = dropdownExpanded,
                        onExpandedChange = { dropdownExpanded = !dropdownExpanded },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedExercise ?: "",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            trackerState.exercisesForSplitDay.forEach { exercise ->
                                DropdownMenuItem(
                                    text = { Text(exercise) },
                                    onClick = {
                                        selectedExercise = exercise
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }

                itemsIndexed(exerciseSets) { index, set ->
                    val setNumber = index + 1
                    ExerciseInputRow(
                        label = "Set $setNumber:",
                        weight = set.weight,
                        onWeightChange = { set.weight = it },
                        reps = set.reps,
                        onRepsChange = { set.reps = it }
                    )
                }

                item {
                    if (exerciseSets.size < maxSets) {
                        Button(
                            onClick = {
                                exerciseSets.add(ExerciseSetUserInput())
                            },
                            modifier = Modifier
                                .padding(40.dp)
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
                    Text(
                        text = "Notes:",
                        fontSize = (screenWidth / 20).sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                }

                item {
                    TextField(
                        value = notes,
                        onValueChange = { newText ->
                            if (newText.length <= notesMaxCharacters) {
                                notes = newText
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        maxLines = 3
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
                                    date = trackerState.date.toString(),
                                    sets = exerciseSets,
                                    notes = notes,
                                )
                            }
                        },
                        enabled = (trackerState.apiRequestState == APIRequestState.Idle),
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Submit")
                    }
                }
            }

            when (trackerState.apiRequestState) {
                APIRequestState.Loading -> {
                    OverlayMenuScreenLoading()
                }

                APIRequestState.Success -> {
                    OverlayMenuScreenWithPopupScreen(
                        text = "Workout submission successful",
                        buttonTexts = listOf("Submit another exercise"),
                        onButtonSelection = {
                            resetInputs()
                            trackerViewModel.resetPostRequestState()
                            scrollToTop = true
                        }
                    )
                }

                APIRequestState.Error -> {
                    OverlayMenuScreenWithPopupScreen(
                        text = "Could not save workout submission",
                        buttonTexts = listOf("Close"),
                        onButtonSelection = {
                            trackerViewModel.resetPostRequestState()
                        }
                    )
                }

                APIRequestState.Idle -> {
                    // No overlay
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