package com.example.workout_tracker.utils

import PopUpScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.workout_tracker.view.components.ExerciseRecordDatePicker
import java.time.LocalDate

@Composable
fun OverlayPopUpScreen(
    text: String = "",
    cardBGColor: Color = Color.LightGray,
    textColor: Color = Color.Black,
    showCloseButton: Boolean = false,
    onCloseButtonClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    OverlayBackground {
        PopUpScreen(text, cardBGColor, textColor, showCloseButton, onCloseButtonClick) {
            content()
        }
    }
}

@Composable
fun OverlayPopUpScreenLoading() {
    OverlayBackground {
        CircularProgressIndicator(
            color = Color.White, strokeWidth = 4.dp
        )
    }
}

@Composable
fun OverlayDatePicker(
    onDateSelected: (LocalDate) -> Unit, onDismiss: () -> Unit, initialDate: LocalDate
) {
    OverlayBackground {
        ExerciseRecordDatePicker(onDateSelected, onDismiss, initialDate)
    }
}

@Composable
fun OverlayBackground(
    screenColor: Color = Color.Black,
    screenShape: Shape = RectangleShape,
    screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp,
    screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(screenWidth)
            .height(screenHeight)
            .background(
                color = screenColor.copy(alpha = 0.5f),
                shape = screenShape
            ),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        content()
    }
}