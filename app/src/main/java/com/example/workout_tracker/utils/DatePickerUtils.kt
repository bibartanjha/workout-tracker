package com.example.workout_tracker.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

object DatePickerUtils {
    val TIME_ZONE: ZoneId = ZoneId.of("America/Chicago")

    fun getCurrentDate(): LocalDate {
        return LocalDate.now(TIME_ZONE)
    }

    fun convertDatePickerSelectedDateToLocalDate(dateMillis: Long): LocalDate {
        return dateMillis.let { millis ->
            Instant.ofEpochMilli(millis)
                .atZone(ZoneOffset.UTC) // note to self: this is not a mistype, the date picker returns UTC millis
                .toLocalDate()
        }
    }
}