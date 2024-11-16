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
    private val userService: UserService,
    private val balanceService: BalanceService,
    private val qrCodeService: QrCodeService,
    private val emailService: EmailService,
    private val syncReservationsService: SyncReservationsService
) {

    fun isTimeAvailable(playgroundName: String, day: LocalDate, startTime: LocalTime, endTime: LocalTime): Boolean {
        val freeHours = getFreeTimes(playgroundName, day)
        return !freeHours.any { it.from.isAfterE(startTime) && it.to.isBeforeE(endTime) }

    }

    fun getReservationByUserEmail(): List<Reservation> {
        val user = userService.getUser()
        return reservationRepository.findByUser(user).sortedBy { it.day }
    }

    fun makeReservation(
        playgroundName: String,
        day: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ): Reservation? {
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

        val price = reservation.price!!
        val balance = balanceService.getBalance()

        if (balance < price) {
            return null
        }

        balanceService.subtractBalance(price)

        reservationRepository.save(reservation)

        val qr = qrCodeService.generateQRCode(reservation.uuid, 240, 240)
        emailService.sendMessageWithAttachment(
            "antongorobec101@gmail.com",
            "Reservation",
            "You have a reservation at ${reservation.playground?.name} on ${reservation.day} from ${reservation.startTime} to ${reservation.endTime}",
            qr,
            "qr.png"
        )
        syncReservationsService.sync()
        return reservation
    }

    fun getPriceForTime(playgroundName: String, day: LocalDate, startTime: LocalTime, endTime: LocalTime): Double? {
        val playground = playgroundService.getPlaygroundByName(playgroundName) ?: return null
        val freeHours = getFreeTimes(playgroundName, day)
        val freeHour = freeHours.find { it.from.isBeforeE(startTime) && it.to.isAfterE(endTime) } ?: return null
        return freeHour.price
    }

    fun getAllReservationsForDay(playgroundName: String, day: LocalDate): List<Reservation> {
        return reservationRepository.findByDay(day).filter { it.playground?.name == playgroundName }.filter { it.status == ReservationStatus.ACTIVE }
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
                } else if (reservationStart.isBeforeE(hourStart) && reservationEnd.isBeforeE(hourEnd) && reservationEnd.isAfterE(
                        hourStart
                    )
                ) {
                    hour.from = reservationEnd
                } else if (reservationStart.isAfterE(hourStart) && reservationEnd.isAfterE(hourEnd) && reservationStart.isBeforeE(
                        hourEnd
                    )
                ) {
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

    fun getReservationQrCodeById(reservationId: Long): ByteArray? {
        val reservation = reservationRepository.findById(reservationId).orElse(null) ?: return null
        return qrCodeService.generateQRCode(reservation.uuid, 240, 240)
    }

    fun cancelReservation(id: Long): Boolean {
        val email = SecurityContextHolder.getContext().authentication.principal as String
        val user = userService.getUserByEmail(email) ?: return false
        val reservations = reservationRepository.findByUser(user).filter { it.id == id }.forEach{
            it.status = ReservationStatus.CANCELLED
            reservationRepository.save(it)
        }
        return true
    }


    fun getAllReservations(): List<Reservation> {
        return reservationRepository.findAll().toList()
    }
}