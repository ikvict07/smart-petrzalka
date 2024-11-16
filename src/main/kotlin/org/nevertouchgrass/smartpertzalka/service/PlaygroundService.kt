package org.nevertouchgrass.smartpertzalka.service

import org.nevertouchgrass.smartpertzalka.db.entity.Playground
import org.nevertouchgrass.smartpertzalka.db.entity.WeekDayEnum
import org.nevertouchgrass.smartpertzalka.db.entity.toWeekDayEnum
import org.nevertouchgrass.smartpertzalka.db.repository.PlaygroundRepository
import org.nevertouchgrass.smartpertzalka.dto.OpenHours
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class PlaygroundService(
    private val playgroundRepository: PlaygroundRepository,
    private val priceService: PriceService
) {

    fun getPlaygroundOpenHoursForDay(playgroundName: String, day: LocalDate): List<OpenHours> {
        val playground = playgroundRepository.findByName(playgroundName) ?: return emptyList()

        val result = mutableListOf<OpenHours>()

        val defaultPrices = priceService.getDefaultPrices(playground)

        val pricesForWeekDay = priceService.getPriceForWeekDay(day.dayOfWeek.toWeekDayEnum(), playground)

        val pricesForDay = priceService.getPriceForDay(day, playground)


        result.addAll(defaultPrices.map {
            OpenHours(
                it.startTime!!,
                it.endTime!!,
                it.priceMultiplier!! * playground.defaultPrice!!
            )
        })

        pricesForWeekDay.forEach {
            add(
                result,
                OpenHours(
                    it.startTime!!,
                    it.endTime!!,
                    it.priceMultiplier!! * playground.defaultPrice!!
                )
            )
        }
        pricesForDay.forEach {
            add(
                result,
                OpenHours(
                    it.startTime!!,
                    it.endTime!!,
                    it.priceMultiplier!! * playground.defaultPrice!!
                )
            )
        }

        return result.distinct().sortedBy { it.from }.filter { !it.from.isAfterE(it.to) }
    }


    fun addOpenHoursForDay(
        playgroundName: String, day: LocalDate, openHours: OpenHours, priceMultiplier: Double, isClosed: Boolean
    ): Playground? {
        val playground = playgroundRepository.findByName(playgroundName) ?: return null
        priceService.addPriceForDay(day, playground, openHours.from, openHours.to, priceMultiplier, isClosed)
        return playground
    }

    fun addOpenHoursForWeekDays(
        playgroundName: String, weekDays: List<WeekDayEnum>, openHours: OpenHours, priceMultiplier: Double
    ): Playground? {
        val playground = playgroundRepository.findByName(playgroundName) ?: return null
        priceService.addPriceForWeekDay(weekDays, playground, openHours.from, openHours.to, priceMultiplier)
        return playground
    }

    fun addDefaultOpenHours(playgroundName: String, openHours: OpenHours, priceMultiplier: Double): Playground? {
        val playground = playgroundRepository.findByName(playgroundName) ?: return null
        priceService.addDefaultPrice(playground, openHours.from, openHours.to, priceMultiplier)
        return playground
    }

    fun add(existing: MutableList<OpenHours>, toAdd: OpenHours) {
        var isAdded = false

        var i = 0
        while (i < existing.size) {
            val price = existing[i]
            val priceStart = price.from
            val priceEnd = price.to

            if (isAdded) {
                break
            }

            if (!price.isAvailable) {
                i++
                continue
            }

            if (!existing.any {
                    val from = it.from
                    val to = it.to
                    toAdd.from.isBeforeE(from) && toAdd.to.isAfterE(to) || toAdd.from.isAfterE(from) && toAdd.to.isBeforeE(
                        to
                    ) || toAdd.from.isBeforeE(from) && toAdd.to.isAfterE(from) || toAdd.from.isBeforeE(to) && toAdd.to.isAfterE(
                        to
                    )
                }) {
                existing.add(toAdd)
                isAdded = true
                break
            }

            if (toAdd.to.isBeforeE(priceStart)) { // new before old
            } else if (toAdd.to.isAfterE(priceEnd)) { // new after old
            } else if (toAdd.from.isBeforeE(priceStart) && toAdd.to.isAfterE(priceEnd)) { // new contains old
                isAdded = true
                existing.add(toAdd)
                price.isAvailable = false
            } else if (toAdd.from.isAfterE(priceStart) && toAdd.to.isBeforeE(priceEnd)) { // old contains new
                isAdded = true
                price.to = toAdd.from
                existing.add(toAdd)
                existing.add(OpenHours(toAdd.to, priceEnd, price.price))

            } else if (toAdd.from.isAfterE(priceStart) && toAdd.from.isBeforeE(priceEnd) && toAdd.to.isAfterE(priceEnd)
            ) { // new starts in old
                isAdded = true
                price.to = toAdd.from
                existing.add(OpenHours(toAdd.from, priceEnd, price.price))
                add(existing, OpenHours(priceEnd, toAdd.to, price.price))
            } else if (toAdd.from.isBeforeE(priceStart) && toAdd.to.isAfterE(priceStart) && toAdd.to.isBeforeE(priceEnd)
            ) { // new ends in old
                isAdded = true
                add(existing, OpenHours(toAdd.from, priceStart, price.price))
                price.from = toAdd.to
            } else {
                add(existing, toAdd)
            }
            i++

        }
        if (!isAdded) {
            existing.add(toAdd)
        }
    }

    fun getPlaygroundByName(playgroundName: String): Playground? {
        return playgroundRepository.findByName(playgroundName)
    }

}

fun LocalTime.isBeforeE(time: LocalTime): Boolean {
    return this.toSecondOfDay() <= time.toSecondOfDay()
}
fun LocalTime.isAfterE(time: LocalTime): Boolean {
    return this.toSecondOfDay() >= time.toSecondOfDay()
}