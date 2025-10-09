package com.example.workout_tracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.workout_tracker.view.screens.ExercisesScreen
import com.example.workout_tracker.view.screens.HomeScreen
import com.example.workout_tracker.view.screens.SplitDayScreen
import com.example.workout_tracker.viewmodel.TrackerViewModel


@Composable
fun NavigationGraph(
    trackerViewModel: TrackerViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME_SCREEN) {
        composable(Routes.HOME_SCREEN) {
            HomeScreen(
                onOptionSelected = {
                    navController.navigate(it)
                }
            )
        }

        composable(Routes.SPLIT_DAY_SCREEN) {
            SplitDayScreen(
                trackerViewModel = trackerViewModel, splitDaySelected = {
                    navController.navigate(Routes.EXERCISES_SCREEN)
                }
            )
        }

        composable(Routes.EXERCISES_SCREEN) {
            ExercisesScreen(
                trackerViewModel = trackerViewModel
            )
        }
    }
}