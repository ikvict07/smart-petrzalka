package org.nevertouchgrass.smartpertzalka.service

import org.nevertouchgrass.smartpertzalka.db.entity.Reservation
import org.nevertouchgrass.smartpertzalka.db.entity.ReservationStatus
import org.nevertouchgrass.smartpertzalka.db.repository.ReservationRepository
import org.nevertouchgrass.smartpertzalka.dto.OpenHours
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val playgroundService: PlaygroundService,
    private val userService: UserService
) {

    fun isTimeAvailable(playgroundName: String, day: LocalDate, startTime: LocalTime, endTime: LocalTime): Boolean {
        val freeHours = getFreeTimes(playgroundName, day)
        return freeHours.any { it.from.isAfterE(startTime) && it.to.isBeforeE(endTime) }

    }

    fun makeReservation(playgroundName: String, day: LocalDate, startTime: LocalTime, endTime: LocalTime): Reservation? {
        if (!isTimeAvailable(playgroundName, day, startTime, endTime)) {
            return null
        }
        val playground = playgroundService.getPlaygroundByName(playgroundName) ?: return null

        val reservation = Reservation()
        reservation.day = day
        reservation.startTime = startTime
        reservation.endTime = endTime
        reservation.playground = playground
        reservation.status = ReservationStatus.ACTIVE
        reservation.price = getPriceForTime(playgroundName, day, startTime, endTime) ?: return null

        val email = SecurityContextHolder.getContext().authentication.principal as String
        val user = userService.getUserByEmail(email) ?: return null
        reservation.user = user
        reservationRepository.save(reservation)
        return reservation
    }

    fun getPriceForTime(playgroundName: String, day: LocalDate, startTime: LocalTime, endTime: LocalTime): Double? {
        val playground = playgroundService.getPlaygroundByName(playgroundName) ?: return null
        val freeHours = getFreeTimes(playgroundName, day)
        val freeHour = freeHours.find { it.from.isAfterE(startTime) && it.to.isBeforeE(endTime) } ?: return null
        return freeHour.price
    }

    fun getAllReservationsForDay(playgroundName: String, day: LocalDate): List<Reservation> {
        return reservationRepository.findByDay(day).filter { it.playground?.name == playgroundName }
    }

    fun getFreeTimes(playgroundName: String, day: LocalDate): List<OpenHours> {
        val allHours = playgroundService.getPlaygroundOpenHoursForDay(playgroundName, day).toMutableList()
        val reservations = getAllReservationsForDay(playgroundName, day)

        for (reservation in reservations) {
            var i = 0
            while (i < allHours.size) {
                val hour = allHours[i]
                val reservationStart = reservation.startTime!!
                val reservationEnd = reservation.endTime!!
                val hourStart = hour.from
                val hourEnd = hour.to
                if (reservationStart.isBeforeE(hourStart) && reservationEnd.isAfterE(hourEnd)) {
                    hour.isAvailable = false
                }else if (reservationStart.isBeforeE(hourStart) && reservationEnd.isBeforeE(hourEnd) && reservationEnd.isAfterE(hourStart)) {
                    hour.from = reservationEnd
                }else if (reservationStart.isAfterE(hourStart) && reservationEnd.isAfterE(hourEnd) && reservationStart.isBeforeE(hourEnd)) {
                    hour.to = reservationStart
                } else if (reservationStart.isAfterE(hourStart) && reservationEnd.isBeforeE(hourEnd)) {
                    hour.to = reservationStart
                    allHours.add(
                        OpenHours(
                            reservationEnd,
                            hourEnd,
                            hour.price
                        )
                    )
                }
                i++
            }
        }
        return allHours.filter { it.isAvailable }
    }
}