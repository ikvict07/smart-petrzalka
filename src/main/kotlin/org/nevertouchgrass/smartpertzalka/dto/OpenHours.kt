package org.nevertouchgrass.smartpertzalka.dto

import java.time.LocalDateTime

data class OpenHours(
    val from: LocalDateTime,
    val to: LocalDateTime,
    val price: Double? = null
)
