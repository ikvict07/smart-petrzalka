package org.nevertouchgrass.smartpertzalka.service

import org.nevertouchgrass.smartpertzalka.db.entity.User
import org.nevertouchgrass.smartpertzalka.db.entity.UserRole
import org.nevertouchgrass.smartpertzalka.db.entity.UserRolesEntity
import org.nevertouchgrass.smartpertzalka.db.repository.UserRepository
import org.nevertouchgrass.smartpertzalka.dto.request.RegisterDTO
import org.nevertouchgrass.smartpertzalka.jwt.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(email: String, password: String): String {
        userRepository.findUserByEmail(email)?.let { user ->
            if (passwordEncoder.matches(password, user.password)) {
                return user.roles.mapNotNull { it -> it.role }.map { it.name }
                    .let { jwtTokenProvider.createToken(email, it) }
            }
        }
        return ""
    }

    fun register(data: RegisterDTO): String {
        val exist = userRepository.findUserByEmail(data.email)
        if (exist != null) {
            return ""
        }
        val user = User().apply {
            name = data.name
            surname = data.surname
            password = passwordEncoder.encode(data.password)
            email = data.email
            phoneNumber = data.phoneNumber
            roles = listOf(UserRolesEntity().apply { role = UserRole.USER })
        }
        userRepository.save(user)
        return jwtTokenProvider.createToken(data.email, listOf("USER"))
    }
}