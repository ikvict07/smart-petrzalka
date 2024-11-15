package org.nevertouchgrass.smartpertzalka.db.repository

import org.nevertouchgrass.smartpertzalka.db.entity.WeekDay
import org.springframework.data.repository.CrudRepository

interface WeekDayRepository: CrudRepository<WeekDay, Long> {
}