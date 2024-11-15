package org.nevertouchgrass.smartpertzalka.dto.request

import java.time.LocalDate

data class GetHoursForDayDTO(
    val playgroundName: String,
    val date: LocalDate
)
