package org.nevertouchgrass.smartpertzalka.db.repository

import org.nevertouchgrass.smartpertzalka.db.entity.Playground
import org.nevertouchgrass.smartpertzalka.db.entity.Price
import org.nevertouchgrass.smartpertzalka.db.entity.WeekDay
import org.springframework.data.repository.CrudRepository
import java.time.LocalDate

interface PriceRepository: CrudRepository<Price, Long> {
    fun findByDayAndPlayground(day: LocalDate, playground: Playground): List<Price>

    fun findByPlayground(playground: Playground): List<Price>
}