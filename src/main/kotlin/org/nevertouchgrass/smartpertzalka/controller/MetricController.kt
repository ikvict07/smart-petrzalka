package org.nevertouchgrass.smartpertzalka.controller

import org.nevertouchgrass.smartpertzalka.service.StatisticsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime


@RestController
@RequestMapping("/api/metric")
class MetricController(private val statisticsService: StatisticsService) {

    @GetMapping("/get-hour-popularity")
    fun getHourPopularity(): ResponseEntity<Map<Pair<LocalTime, LocalTime>, Int>> {
        return ResponseEntity.ok(statisticsService.getHourPopularity())
    }
}