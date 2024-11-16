package org.nevertouchgrass.smartpertzalka.service

import org.nevertouchgrass.smartpertzalka.db.entity.ReservationStatus
import org.nevertouchgrass.smartpertzalka.db.repository.ReservationRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SyncReservationsService(
    private val scheduler: DynamicTaskScheduler,
    private val reservationRepository: ReservationRepository,
    private val raspService: RaspService
) {

    fun activate() {
        val reservations = reservationRepository.findAll().filter { it.status == ReservationStatus.ACTIVE }
        reservations.forEach { reservation ->
            if (reservation.day!!.atTime(reservation.endTime).isAfter(LocalDateTime.now())) {
                reservation.status = ReservationStatus.FINISHED
            }
        }
        reservationRepository.saveAll(reservations)

        val updatedReservations = reservations.filter { it.status == ReservationStatus.ACTIVE }

        updatedReservations.forEach { reservation ->
            scheduler.scheduleMessage(
                reservation.day!!,
                reservation.startTime!!,
                reservation
            ) {
                raspService.turnOnLight()
            }
            scheduler.scheduleMessage(
                reservation.day!!,
                reservation.endTime!!,
                reservation
            ) {
                raspService.turnOffLight()
                reservation.status = ReservationStatus.FINISHED
                reservationRepository.save(reservation)
            }
        }
    }

    fun sync() {
        val reservations = reservationRepository.findAll().filter { it.status == ReservationStatus.ACTIVE }
        reservations.forEach { raspService.receiveReservation(it) }
    }
}