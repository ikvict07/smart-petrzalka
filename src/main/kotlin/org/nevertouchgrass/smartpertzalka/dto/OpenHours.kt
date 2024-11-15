package org.nevertouchgrass.smartpertzalka.dto

import java.time.LocalTime

data class OpenHours(
    var from: LocalTime,
    var to: LocalTime,
    val price: Double? = null,
    var isAvailable: Boolean = true
)
