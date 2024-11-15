package org.nevertouchgrass.smartpertzalka.dto.request

import java.time.LocalDateTime

data class AddDefaultPriceDTO(
    val playgroundName: String,
    val price: Double,
    val from: LocalDateTime,
    val to: LocalDateTime
)
