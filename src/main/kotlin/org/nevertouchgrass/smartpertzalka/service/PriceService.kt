package org.nevertouchgrass.smartpertzalka.service

import org.nevertouchgrass.smartpertzalka.db.entity.Playground
import org.nevertouchgrass.smartpertzalka.db.entity.Price
import org.nevertouchgrass.smartpertzalka.db.entity.WeekDay
import org.nevertouchgrass.smartpertzalka.db.entity.WeekDayEnum
import org.nevertouchgrass.smartpertzalka.db.repository.PriceRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class PriceService(
    private val priceRepository: PriceRepository,
) {
    fun getPriceForDay(day: LocalDate, playground: Playground): List<Price> {
        return priceRepository.findByDayAndPlayground(day, playground)
    }

    fun getPriceForWeekDay(dayOfWeek: WeekDayEnum, playground: Playground): List<Price> {
        return priceRepository.findByPlayground(playground)
            .filter { price -> price.weekDays?.map { it.day }?.contains(dayOfWeek) ?: false }

    }

    fun getDefaultPrices(playground: Playground): List<Price> {
        return priceRepository.findByPlayground(playground).filter { price -> price.weekDays?.isEmpty() ?: true }
            .filter { price -> price.day == null }
    }


    fun addPriceForDay(
        day: LocalDate,
        playground: Playground,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        priceMultiplier: Double
    ): Price {
        val price = Price()
        price.day = day
        price.playground = playground
        price.startTime = startTime
        price.endTime = endTime
        price.priceMultiplier = priceMultiplier
        price.isClosed = false
        return priceRepository.save(price)
    }

    fun addPriceForWeekDay(
        dayOfWeeks: List<WeekDayEnum>,
        playground: Playground,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        priceMultiplier: Double
    ): Price {
        val price = Price()
        price.playground = playground
        price.startTime = startTime
        price.endTime = endTime
        price.priceMultiplier = priceMultiplier
        price.isClosed = false
        price.weekDays = mutableListOf()

        val priceFromDb = priceRepository.save(price)
        price.weekDays!!.addAll(
            dayOfWeeks.map {
                val weekDay = WeekDay()
                weekDay.day = it
                weekDay.price = priceFromDb
                weekDay
            }
        )
        return priceRepository.save(price)
    }

    fun addDefaultPrice(
        playground: Playground, startTime: LocalDateTime, endTime: LocalDateTime, priceMultiplier: Double
    ): Price {
        val price = Price()
        price.playground = playground
        price.startTime = startTime
        price.endTime = endTime
        price.priceMultiplier = priceMultiplier
        price.isClosed = false
        return priceRepository.save(price)
    }
}