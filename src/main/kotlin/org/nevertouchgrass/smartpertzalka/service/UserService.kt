package org.nevertouchgrass.smartpertzalka.service

import jakarta.mail.internet.MimeMessage
import org.nevertouchgrass.smartpertzalka.db.entity.CreditCard
import org.nevertouchgrass.smartpertzalka.db.entity.User
import org.nevertouchgrass.smartpertzalka.db.repository.CreditCardRepository
import org.nevertouchgrass.smartpertzalka.db.repository.UserRepository
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val paymentService: PaymentService, private val creditCardRepository: CreditCardRepository
) {

    fun getUserByEmail(email: String) = userRepository.findUserByEmail(email)

    fun getUser(): User {
        val email = SecurityContextHolder.getContext().authentication.principal as String
        return getUserByEmail(email)!!
    }

    fun addCreditCard(cardNumber: String, expirationDate: String, cvv: String): Boolean {
        val email = SecurityContextHolder.getContext().authentication.principal as String
        val user = getUserByEmail(email) ?: return false

        val creditCard = CreditCard().apply {
            this.cardNumber = cardNumber
            this.expirationDate = expirationDate
            this.cvv = cvv
            this.user = user
        }
        user.creditCard = creditCard
        creditCardRepository.save(creditCard)
        userRepository.save(user)
        return true
    }

    fun topUpBalance(amount: Double): Boolean {
        val email = SecurityContextHolder.getContext().authentication.principal as String
        val user = getUserByEmail(email) ?: return false
        val creditCard = user.creditCard ?: return false
        val transaction = paymentService.processPayment(
            cardNumber = creditCard.cardNumber!!,
            expirationDate = creditCard.expirationDate!!,
            cvv = creditCard.cvv!!,
            amount = amount.toBigDecimal()
        )
        if (!transaction) {
            return false
        }
        user.balance = user.balance.plus(amount)

        userRepository.save(user)
        return true
    }
}
