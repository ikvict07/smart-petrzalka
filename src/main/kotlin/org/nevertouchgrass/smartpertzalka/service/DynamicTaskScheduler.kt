package org.nevertouchgrass.smartpertzalka.service

import org.nevertouchgrass.smartpertzalka.db.entity.Reservation
import org.nevertouchgrass.smartpertzalka.db.entity.ReservationStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.*


@Component
@EnableScheduling
class DynamicTaskScheduler {
    @Autowired
    private val taskScheduler: TaskScheduler? = null

    fun scheduleMessage(date: LocalDate, time: LocalTime, reservation: Reservation, task: () -> Unit) {
        val dateTime = date.atTime(time)

        val instant = dateTime.atZone(ZoneId.systemDefault()).toInstant()

        taskScheduler?.schedule(task, instant)
    }

}