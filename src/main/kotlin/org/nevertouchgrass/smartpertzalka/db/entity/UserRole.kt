package org.nevertouchgrass.smartpertzalka.db.entity

import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*

enum class UserRole {
    ADMIN,
    USER;

    companion object {
        fun fromString(role: String): UserRole {
            return valueOf(role.uppercase(Locale.getDefault()))
        }
    }

    fun toGrantedAuthority(): SimpleGrantedAuthority {
        return SimpleGrantedAuthority("ROLE_${this}")
    }
}

