package org.nevertouchgrass.smartpertzalka.dto.request

data class AddCreditCardDTO(
    val cardNumber: String, val expirationDate: String, val cvv: String
)
