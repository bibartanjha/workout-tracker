package com.example.workout_tracker.view.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workout_tracker.api.Workout

@Composable
fun WorkoutRecordsDisplay(
    listOfWorkouts: List<Workout>, showDate: Boolean
) {
    LazyColumn {
        for (workout in listOfWorkouts) {
            item {
                Text(
                    text = workout.exercise,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
            }

            if (showDate) {
                item {
                    Text(
                        text = "Date: ${workout.date}",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp, bottom = 4.dp)
                            .fillMaxWidth()
                    )
                }
            }

            workout.sets.forEachIndexed { index, set ->
                val setNumber = index + 1
                item {
                    Text(
                        text = "Set $setNumber: ${set.weight} lbs - ${set.reps} reps",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp, bottom = 4.dp)
                            .fillMaxWidth()
                    )
                }
            }

            if (workout.notes?.isNotEmpty() == true) {
                item {
                    Text(
                        text = "Notes: ${workout.notes}",
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp, bottom = 8.dp)
                            .fillMaxWidth()
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

}

