package org.nevertouchgrass.smartpertzalka.service

import org.nevertouchgrass.smartpertzalka.db.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class BalanceService(
    private val userRepository: UserRepository,
    private val userService: UserService
) {

    fun getBalance(): Double {
        val email = SecurityContextHolder.getContext().authentication.principal as String
        return getBalanceByEmail(email)
    }

    fun getBalanceByEmail(email: String): Double {
        val user = userService.getUserByEmail(email) ?: return 0.0
        return user.balance!!
    }

    fun addBalance(amount: Double): Double {
        val email = SecurityContextHolder.getContext().authentication.principal as String
        return addBalanceByEmail(email, amount)
    }

    fun addBalanceByEmail(email: String, amount: Double): Double {
        val user = userService.getUserByEmail(email) ?: return 0.0
        user.balance = user.balance.plus(amount)
        userRepository.save(user)
        return user.balance
    }

    fun subtractBalance(amount: Double): Double {
        val email = SecurityContextHolder.getContext().authentication.principal as String
        val user = userService.getUserByEmail(email) ?: return 0.0
        if (user.balance < amount) {
            return 0.0
        }
        user.balance = user.balance.minus(amount)
        userRepository.save(user)
        return user.balance
    }
}