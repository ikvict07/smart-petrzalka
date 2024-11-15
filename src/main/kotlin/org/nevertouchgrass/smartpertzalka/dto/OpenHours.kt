package org.nevertouchgrass.smartpertzalka.dto

import java.time.LocalTime

data class OpenHours(
    val from: LocalTime,
    val to: LocalTime,
    val price: Double? = null
)
