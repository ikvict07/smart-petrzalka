package org.nevertouchgrass.smartpertzalka.dto.responce

import org.nevertouchgrass.smartpertzalka.db.entity.CreditCard

data class UserInfoDTO(
    val name: String,
    val email: String,
    val surname: String,
    val balance: Double,
    val phoneNumber: String,
    val creditCard: CreditCard?
)
