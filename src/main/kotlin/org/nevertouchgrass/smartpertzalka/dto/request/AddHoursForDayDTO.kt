package org.nevertouchgrass.smartpertzalka.dto.request

import java.time.LocalDate
import java.time.LocalTime

data class AddHoursForDayDTO(
    val playgroundName: String,
    val from: LocalTime,
    val to: LocalTime,
    val price: Double,
    val day: LocalDate,
    val isClosed: Boolean
)
