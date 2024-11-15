package org.nevertouchgrass.smartpertzalka.db.entity

import java.time.DayOfWeek

enum class WeekDayEnum {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

fun WeekDayEnum.toDayOfWeek(): DayOfWeek {
    return when (this) {
        WeekDayEnum.MONDAY -> DayOfWeek.MONDAY
        WeekDayEnum.TUESDAY -> DayOfWeek.TUESDAY
        WeekDayEnum.WEDNESDAY -> DayOfWeek.WEDNESDAY
        WeekDayEnum.THURSDAY -> DayOfWeek.THURSDAY
        WeekDayEnum.FRIDAY -> DayOfWeek.FRIDAY
        WeekDayEnum.SATURDAY -> DayOfWeek.SATURDAY
        WeekDayEnum.SUNDAY -> DayOfWeek.SUNDAY
    }
}
fun DayOfWeek.toWeekDayEnum(): WeekDayEnum {
    return when (this) {
        DayOfWeek.MONDAY -> WeekDayEnum.MONDAY
        DayOfWeek.TUESDAY -> WeekDayEnum.TUESDAY
        DayOfWeek.WEDNESDAY -> WeekDayEnum.WEDNESDAY
        DayOfWeek.THURSDAY -> WeekDayEnum.THURSDAY
        DayOfWeek.FRIDAY -> WeekDayEnum.FRIDAY
        DayOfWeek.SATURDAY -> WeekDayEnum.SATURDAY
        DayOfWeek.SUNDAY -> WeekDayEnum.SUNDAY
    }
}
