package org.nevertouchgrass.smartpertzalka.controller

import org.nevertouchgrass.smartpertzalka.dto.LoginDTO
import org.nevertouchgrass.smartpertzalka.dto.RegisterDTO
import org.nevertouchgrass.smartpertzalka.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun login(@RequestBody request: RegisterDTO): ResponseEntity<String> {
        if (request.email.isBlank() || request.password.isBlank()) {
            return ResponseEntity.badRequest().body("Username and password must not be empty")
        }
        val jwt = authService.register(request)
        if (jwt.isBlank()) {
            return ResponseEntity.badRequest().body("User already exists")
        }
        return ResponseEntity.ok(jwt)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginDTO): ResponseEntity<String> {
        if (request.email.isBlank() || request.password.isBlank()) {
            return ResponseEntity.badRequest().body("Username and password must not be empty")
        }
        val jwt = authService.login(request.email, request.password)
        if (jwt.isBlank()) {
            return ResponseEntity.badRequest().body("Invalid username or password")
        }
        return ResponseEntity.ok(jwt)
    }
}