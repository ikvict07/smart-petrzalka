package org.nevertouchgrass.smartpertzalka.service

import org.nevertouchgrass.smartpertzalka.db.entity.Playground
import org.nevertouchgrass.smartpertzalka.db.entity.WeekDayEnum
import org.nevertouchgrass.smartpertzalka.db.entity.toWeekDayEnum
import org.nevertouchgrass.smartpertzalka.db.repository.PlaygroundRepository
import org.nevertouchgrass.smartpertzalka.dto.OpenHours
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PlaygroundService(
    val playgroundRepository: PlaygroundRepository,
    val priceService: PriceService
) {

    fun getPlaygroundOpenHoursForDay(playgroundName: String, day: LocalDate): List<OpenHours> {
        val playground = playgroundRepository.findByName(playgroundName) ?: return emptyList()
        val pricesForDay = priceService.getPriceForDay(day, playground)
        val result = mutableListOf<OpenHours>()
        playground.defaultPrice!!
        if (pricesForDay.isNotEmpty()) {
            if (pricesForDay.any { it.isClosed == true }) {
                return emptyList()
            }
            result.addAll(pricesForDay.filter { it.startTime != null }.filter { it.endTime != null }
                .map {
                    OpenHours(
                        it.startTime!!, it.endTime!!,
                        playground.defaultPrice!! * it.priceMultiplier!!
                    )
                })
        }
        val dayOfWeek = day.dayOfWeek
        val pricesForWeekDay = priceService.getPriceForWeekDay(dayOfWeek.toWeekDayEnum(), playground)
        val additional = mutableListOf<OpenHours>()
        if (result.isEmpty()) {
            if (pricesForWeekDay.isNotEmpty()) {
                additional.add(
                    OpenHours(
                        pricesForWeekDay.first().startTime!!,
                        pricesForWeekDay.first().endTime!!,
                        playground.defaultPrice!! * pricesForWeekDay.first().priceMultiplier!!
                    )
                )
            }
        }
        for (resultHour in result) {
            for (price in pricesForWeekDay) {
                if (price.startTime != null && price.endTime != null) {
                    if (price.startTime!!.isBefore(resultHour.from) && price.endTime!!.isAfter(resultHour.to)) {
                        additional.add(
                            OpenHours(
                                price.startTime!!,
                                resultHour.from,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                        additional.add(
                            OpenHours(
                                resultHour.to,
                                price.endTime!!,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                    } else if (price.startTime!!.isBefore(resultHour.from) && price.endTime!!.isBefore(resultHour.to)) {
                        additional.add(
                            OpenHours(
                                price.startTime!!,
                                resultHour.from,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                    } else if (price.startTime!!.isBefore(resultHour.to) && price.endTime!!.isAfter(resultHour.to)) {
                        additional.add(
                            OpenHours(
                                resultHour.to,
                                price.endTime!!,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                    } else if (price.startTime!!.isAfter(resultHour.to)) {
                        additional.add(
                            OpenHours(
                                price.startTime!!,
                                price.endTime!!,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                    } else if (price.endTime!!.isBefore(resultHour.from)) {
                        additional.add(
                            OpenHours(
                                price.startTime!!,
                                price.endTime!!,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                    }
                }
            }
        }
        result.addAll(additional)

        val defaultPrices = priceService.getDefaultPrices(playground)
        val defaultHours = mutableListOf<OpenHours>()
        if (result.isEmpty()) {
            if (defaultPrices.isNotEmpty()) {
                defaultHours.add(
                    OpenHours(
                        defaultPrices.first().startTime!!,
                        defaultPrices.first().endTime!!,
                        playground.defaultPrice!! * defaultPrices.first().priceMultiplier!!
                    )
                )
            }
        }
        for (resultHour in result) {
            for (price in defaultPrices) {
                if (price.startTime != null && price.endTime != null) {
                    if (price.startTime!!.isBefore(resultHour.from) && price.endTime!!.isAfter(resultHour.to)) {
                        defaultHours.add(
                            OpenHours(
                                price.startTime!!,
                                resultHour.from,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                        defaultHours.add(
                            OpenHours(
                                resultHour.to,
                                price.endTime!!,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                    } else if (price.startTime!!.isBefore(resultHour.from) && price.endTime!!.isBefore(resultHour.to)) {
                        defaultHours.add(
                            OpenHours(
                                price.startTime!!,
                                resultHour.from,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                    } else if (price.startTime!!.isBefore(resultHour.to) && price.endTime!!.isAfter(resultHour.to)) {
                        defaultHours.add(
                            OpenHours(
                                resultHour.to,
                                price.endTime!!,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                    } else if (price.startTime!!.isAfter(resultHour.to)) {
                        defaultHours.add(
                            OpenHours(
                                price.startTime!!,
                                price.endTime!!,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                    } else if (price.endTime!!.isBefore(resultHour.from)) {
                        defaultHours.add(
                            OpenHours(
                                price.startTime!!,
                                price.endTime!!,
                                playground.defaultPrice!! * price.priceMultiplier!!
                            )
                        )
                    }
                }
            }
        }
        println(additional)
        println(defaultHours)
        result.addAll(defaultHours)
        return result
    }

    fun addOpenHoursForDay(playgroundName: String, day: LocalDate, openHours: OpenHours, priceMultiplier: Double, isClosed: Boolean): Playground? {
        val playground = playgroundRepository.findByName(playgroundName) ?: return null
        priceService.addPriceForDay(day, playground, openHours.from, openHours.to, priceMultiplier, isClosed)
        return playground
    }

    fun addOpenHoursForWeekDays(
        playgroundName: String,
        weekDays: List<WeekDayEnum>,
        openHours: OpenHours,
        priceMultiplier: Double
    ) {
        val playground = playgroundRepository.findByName(playgroundName) ?: return
        priceService.addPriceForWeekDay(weekDays, playground, openHours.from, openHours.to, priceMultiplier)
    }

    fun addDefaultOpenHours(playgroundName: String, openHours: OpenHours, priceMultiplier: Double): Playground? {
        val playground = playgroundRepository.findByName(playgroundName) ?: return null
        priceService.addDefaultPrice(playground, openHours.from, openHours.to, priceMultiplier)
        return playground
    }
}