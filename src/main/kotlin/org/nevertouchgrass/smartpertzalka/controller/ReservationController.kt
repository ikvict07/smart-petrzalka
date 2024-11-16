package org.nevertouchgrass.smartpertzalka.controller

import org.nevertouchgrass.smartpertzalka.dto.OpenHours
import org.nevertouchgrass.smartpertzalka.dto.request.IsTimeAvailableDTO
import org.nevertouchgrass.smartpertzalka.dto.request.ReservationDTO
import org.nevertouchgrass.smartpertzalka.dto.request.ShowFreeTimeSlotsDTO
import org.nevertouchgrass.smartpertzalka.dto.responce.ReservationResponseDTO
import org.nevertouchgrass.smartpertzalka.service.ReservationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reservation")
class ReservationController(private val reservationService: ReservationService) {

    @PostMapping("/is-available")
    fun isTimeAvailable(@RequestBody request: IsTimeAvailableDTO): ResponseEntity<Boolean> {
        return ResponseEntity.ok(
            reservationService.isTimeAvailable(
                request.playgroundName,
                request.date,
                request.start,
                request.end
            )
        )
    }

    @PostMapping("/show-free-time-slots")
    fun showFreeTimeSlots(@RequestBody request: ShowFreeTimeSlotsDTO): ResponseEntity<List<OpenHours>> {
        return ResponseEntity.ok(reservationService.getFreeTimes(request.playgroundName, request.date))
    }

    @PostMapping("/reserve")
    fun reserve(@RequestBody request: ReservationDTO): ResponseEntity<ReservationResponseDTO> {
        reservationService.makeReservation(
            request.playgroundName,
            request.day,
            request.startTime,
            request.endTime
        )?.let {
            return ResponseEntity.ok(
                ReservationResponseDTO(
                    it.playground!!.name!!,
                    it.day!!,
                    it.startTime!!,
                    it.endTime!!,
                    it.price!!,
                    it.uuid
                )
            )
        } ?: run {
            return ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/get-reservations")
    fun getReservations(): ResponseEntity<List<ReservationResponseDTO>> {
        return ResponseEntity.ok(reservationService.getReservationByUserEmail().map {
            ReservationResponseDTO(
                it.playground!!.name!!,
                it.day!!,
                it.startTime!!,
                it.endTime!!,
                it.price!!,
                it.uuid
            )
        })
    }
}