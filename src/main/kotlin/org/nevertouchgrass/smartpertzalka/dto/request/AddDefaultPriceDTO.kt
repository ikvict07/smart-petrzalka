package org.nevertouchgrass.smartpertzalka.dto.request

import java.time.LocalTime

data class AddDefaultPriceDTO(
    val playgroundName: String,
    val price: Double,
    val from: LocalTime,
    val to: LocalTime
)
