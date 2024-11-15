package org.nevertouchgrass.smartpertzalka.db.repository

import org.nevertouchgrass.smartpertzalka.db.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
@Repository
interface UserRepository: CrudRepository<User, Long> {
    fun findUserByEmail(username: String): User?
}