package org.nevertouchgrass.smartpertzalka.dto.request

import java.time.LocalDate
import java.time.LocalTime

data class IsTimeAvailableDTO(
    val playgroundName: String,
    val date: LocalDate,
    val start: LocalTime,
    val end: LocalTime
)
