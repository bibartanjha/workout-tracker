package com.example.workout_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workout_tracker.navigation.NavigationGraph
import com.example.workout_tracker.viewmodel.TrackerViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            WorkoutTrackerApp()
        }
    }

    @Composable
    fun WorkoutTrackerApp() {
        val trackerViewModel: TrackerViewModel = viewModel()
        NavigationGraph(trackerViewModel)
    }

}