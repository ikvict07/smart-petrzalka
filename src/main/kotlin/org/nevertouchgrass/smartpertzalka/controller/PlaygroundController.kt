package org.nevertouchgrass.smartpertzalka.controller

import org.nevertouchgrass.smartpertzalka.db.entity.Playground
import org.nevertouchgrass.smartpertzalka.db.repository.PlaygroundRepository
import org.nevertouchgrass.smartpertzalka.dto.OpenHours
import org.nevertouchgrass.smartpertzalka.dto.request.AddDefaultPriceDTO
import org.nevertouchgrass.smartpertzalka.dto.request.AddHoursForDayDTO
import org.nevertouchgrass.smartpertzalka.dto.request.GetHoursForDayDTO
import org.nevertouchgrass.smartpertzalka.dto.responce.PlaygroundDTO
import org.nevertouchgrass.smartpertzalka.service.PlaygroundService
import org.nevertouchgrass.smartpertzalka.service.PriceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/playground")
class PlaygroundController(
    val playgroundRepository: PlaygroundRepository,
    private val playgroundService: PlaygroundService,
    private val priceService: PriceService
) {

    @GetMapping("/get-all")
    fun getAllPlaygrounds(): ResponseEntity<List<PlaygroundDTO>> {
        return ResponseEntity.ok(playgroundRepository.findAll().map {
            PlaygroundDTO(it.name!!, it.defaultPrice!!, it.maxCapacity!!)
        })
    }

//    @GetMapping("/")
//    fun getFreeHours(@RequestBody playgroundName: String): ResponseEntity<List<LocalTime>> {
//        return ResponseEntity.ok()
//    }


    @PostMapping("/add")
    fun addPlayground(@RequestBody playgroundDTO: PlaygroundDTO): ResponseEntity<PlaygroundDTO> {
        val playground = Playground()
        playground.name = playgroundDTO.name
        playground.defaultPrice = playgroundDTO.minimalPrice
        playground.maxCapacity = playgroundDTO.maxCapacity
        playgroundRepository.save(playground)
        return ResponseEntity.ok(playgroundDTO)
    }

    @PostMapping("/add-time-default")
    fun addDefaultPrice(
        @RequestBody addDefaultPriceDTO: AddDefaultPriceDTO
    ): ResponseEntity<PlaygroundDTO> {
        val playground = playgroundRepository.findByName(addDefaultPriceDTO.playgroundName) ?: return ResponseEntity.notFound().build()
        priceService.addDefaultPrice(
            playground,
            addDefaultPriceDTO.from,
            addDefaultPriceDTO.to,
            addDefaultPriceDTO.price
        )
        return ResponseEntity.ok(PlaygroundDTO(playground.name!!, playground.defaultPrice!!, playground.maxCapacity!!))
    }

    @PostMapping("/add-hours-for-day")
    fun addHoursForDay(@RequestBody requestBody: AddHoursForDayDTO): ResponseEntity<List<OpenHours>> {
        val playground = playgroundService.addOpenHoursForDay(
            requestBody.playgroundName,
            requestBody.day,
            OpenHours(requestBody.from, requestBody.to),
            requestBody.price,
            requestBody.isClosed
        ) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(playgroundService.getPlaygroundOpenHoursForDay(requestBody.playgroundName, requestBody.day))
    }

    @PostMapping("/get-hours-for-day")
    fun getHoursForDay(@RequestBody requestBody: GetHoursForDayDTO): ResponseEntity<List<OpenHours>> {
        return ResponseEntity.ok(playgroundService.getPlaygroundOpenHoursForDay(requestBody.playgroundName, requestBody.date))
    }
}