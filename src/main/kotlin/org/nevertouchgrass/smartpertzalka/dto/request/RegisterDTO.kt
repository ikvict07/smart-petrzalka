package org.nevertouchgrass.smartpertzalka.dto.request

data class RegisterDTO(
    val name: String,
    val surname: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
)
