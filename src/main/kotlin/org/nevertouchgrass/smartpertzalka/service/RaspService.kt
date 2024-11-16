package org.nevertouchgrass.smartpertzalka.service

import org.nevertouchgrass.smartpertzalka.db.entity.Reservation
import org.springframework.stereotype.Service

@Service
class RaspService {

    fun turnOnLight() {
        println("Light is on")
    }

    fun turnOffLight() {
        println("Light is off")
    }


    fun receiveReservation(reservation: Reservation) {
        println("Received reservation: $reservation")
    }
}