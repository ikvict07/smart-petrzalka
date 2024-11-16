package org.nevertouchgrass.smartpertzalka.service

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.binder.MeterBinder
import org.nevertouchgrass.smartpertzalka.db.repository.ReservationRepository
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class StatisticsService(
    private val reservationRepository: ReservationRepository,
    private val meterRegistry: MeterRegistry,
) : MeterBinder {

    fun getHourPopularity(): Map<Pair<LocalTime, LocalTime>, Int> {
        val reservations = reservationRepository.findAll()
        val hours = mutableMapOf<Pair<LocalTime, LocalTime>, Int>()

        reservations.forEach { reservation ->
            val startHour = reservation.startTime!!
            val endHour = reservation.endTime!!

            val key = Pair(startHour, endHour)
            if (hours.containsKey(key)) {
                hours[key] = hours[key]!! + 1
            } else {
                hours[key] = 1
            }
        }

        return hours
    }

    override fun bindTo(meterRegistry: MeterRegistry) {
        val results = getHourPopularity()

        results.forEach { (hourInterval, value) ->
            val startEnd = "${hourInterval.first} - ${hourInterval.second}"
            meterRegistry.gauge(
                "health.popularity",
                listOf(Tag.of(startEnd, value.toString())),
                value.toDouble().also {  },

            )
        }
    }


}