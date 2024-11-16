package org.nevertouchgrass.smartpertzalka.controller

import org.nevertouchgrass.smartpertzalka.service.EmailService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HelloController(private val emailService: EmailService) {

    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> {
        emailService.sendEmail("antongorobec101@gmail.com", "Hello", "Hello, Anton!")
        return ResponseEntity.ok("Hello, Anton!")
    }
}