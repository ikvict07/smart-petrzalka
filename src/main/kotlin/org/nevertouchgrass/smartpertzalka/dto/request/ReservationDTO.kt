package org.nevertouchgrass.smartpertzalka.dto.request

import java.time.LocalDate
import java.time.LocalTime

data class ReservationDTO(
    val playgroundName: String,
    val day: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)
