package org.nevertouchgrass.smartpertzalka.dto.responce

import java.time.LocalDate
import java.time.LocalTime

data class ReservationResponseDTO (
    val playgroundName: String,
    val day: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val price: Double,
    val uuid: String
)