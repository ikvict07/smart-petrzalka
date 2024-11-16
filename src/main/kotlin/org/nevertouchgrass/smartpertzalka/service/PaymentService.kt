package org.nevertouchgrass.smartpertzalka.service

import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class PaymentService {
    fun processPayment(
        cardNumber: String,
        expirationDate: String,
        cvv: String,
        amount: BigDecimal,
    ): Boolean {
        return true
    }
}