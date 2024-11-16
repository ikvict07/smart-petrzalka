package org.nevertouchgrass.smartpertzalka.db.repository

import org.nevertouchgrass.smartpertzalka.db.entity.Reservation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ReservationRepository: CrudRepository<Reservation, Long> {

    fun findByDay(day: LocalDate): List<Reservation>
}