package org.nevertouchgrass.smartpertzalka.dto

import org.nevertouchgrass.smartpertzalka.db.entity.UserRole

data class RegisterDTO(
    val name: String,
    val surname: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
)
