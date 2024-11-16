package org.nevertouchgrass.smartpertzalka.dto.request

import java.time.LocalDate

data class ShowFreeTimeSlotsDTO(
    val playgroundName: String,
    val date: LocalDate
)
