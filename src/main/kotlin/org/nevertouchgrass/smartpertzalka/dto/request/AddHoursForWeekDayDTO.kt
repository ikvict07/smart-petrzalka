package org.nevertouchgrass.smartpertzalka.dto.request

import org.nevertouchgrass.smartpertzalka.db.entity.WeekDayEnum
import java.time.LocalTime

data class AddHoursForWeekDayDTO(
    val playgroundName: String,
    val weekDays: List<WeekDayEnum>,
    val from: LocalTime,
    val to: LocalTime,
    val priceMultiplier: Double
)
