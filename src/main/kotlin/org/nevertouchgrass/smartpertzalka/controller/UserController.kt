package org.nevertouchgrass.smartpertzalka.controller

import org.nevertouchgrass.smartpertzalka.dto.request.AddCreditCardDTO
import org.nevertouchgrass.smartpertzalka.dto.responce.UserInfoDTO
import org.nevertouchgrass.smartpertzalka.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    @GetMapping("/info")
    fun getUserInfo(): ResponseEntity<UserInfoDTO> {
        val user = userService.getUser()
        return ResponseEntity.ok(
            UserInfoDTO(
                name = user.name!!,
                email = user.email!!,
                surname = user.surname!!,
                balance = user.balance!!,
                phoneNumber = user.phoneNumber!!,
                creditCard = user.creditCard?.apply { this.user=null }
            )
        )
    }

    @PostMapping("/add-credit-card")
    fun addCreditCard(@RequestBody addCreditCardDTO: AddCreditCardDTO): ResponseEntity<Boolean> {
        return ResponseEntity.ok(userService.addCreditCard(addCreditCardDTO.cardNumber, addCreditCardDTO.expirationDate, addCreditCardDTO.cvv))
    }


    @PostMapping("/top-up-balance")
    fun topUpBalance(@RequestBody amount: Double): ResponseEntity<Boolean> {
        return ResponseEntity.ok(userService.topUpBalance(amount))
    }

}