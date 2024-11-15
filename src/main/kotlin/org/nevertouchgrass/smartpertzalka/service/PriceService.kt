package org.nevertouchgrass.smartpertzalka.service

import org.nevertouchgrass.smartpertzalka.db.entity.Playground
import org.nevertouchgrass.smartpertzalka.db.entity.Price
import org.nevertouchgrass.smartpertzalka.db.entity.WeekDay
import org.nevertouchgrass.smartpertzalka.db.entity.WeekDayEnum
import org.nevertouchgrass.smartpertzalka.db.repository.PriceRepository
import org.nevertouchgrass.smartpertzalka.db.repository.WeekDayRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class PriceService(
    private val priceRepository: PriceRepository,
    private val weekDayRepository: WeekDayRepository,
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
        startTime: LocalTime,
        endTime: LocalTime,
        priceMultiplier: Double,
        isClosed: Boolean
    ): Price {
        val price = Price()
        price.day = day
        price.playground = playground
        price.startTime = startTime
        price.endTime = endTime
        price.priceMultiplier = priceMultiplier
        price.isClosed = isClosed
        return priceRepository.save(price)
    }

    fun addPriceForWeekDay(
        dayOfWeeks: List<WeekDayEnum>,
        playground: Playground,
        startTime: LocalTime,
        endTime: LocalTime,
        priceMultiplier: Double
    ): Price {
        // Создаем объект Price
        val price = Price().apply {
            this.playground = playground
            this.startTime = startTime
            this.endTime = endTime
            this.priceMultiplier = priceMultiplier
            this.isClosed = false
        }

        val savedPrice = priceRepository.save(price)

        val weekDays = dayOfWeeks.map { dayOfWeek ->
            WeekDay().apply {
                this.day = dayOfWeek
                this.price = savedPrice
            }
        }
        weekDayRepository.saveAll(weekDays)

        savedPrice.weekDays = weekDays.toMutableList()

        return priceRepository.save(savedPrice)
    }

    fun addDefaultPrice(
        playground: Playground, startTime: LocalTime, endTime: LocalTime, priceMultiplier: Double
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