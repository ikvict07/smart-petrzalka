package org.nevertouchgrass.smartpertzalka.service

import okhttp3.OkHttpClient
import okhttp3.Request
import org.nevertouchgrass.smartpertzalka.db.entity.Reservation
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class RaspService {

    fun turnOnLight() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://192.168.4.1:8080?light=1")
            .build()
        client.newCall(request).execute().use {
            println(it.isSuccessful)
        }
        println("Light is on")
    }

    fun turnOffLight() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://192.168.4.1:8080?light=0")
            .build()
        client.newCall(request).execute().use {
            println(it.isSuccessful)
        }
        println("Light is off")
    }


    fun receiveReservation(reservation: Reservation) {
        println("Received reservation: $reservation")
    }
}

fun main() {
    val raspService = RaspService()
    raspService.turnOffLight()
}