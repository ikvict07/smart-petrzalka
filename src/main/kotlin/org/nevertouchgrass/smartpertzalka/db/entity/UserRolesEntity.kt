package org.nevertouchgrass.smartpertzalka.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany


@Entity(name = "playground_user_roles")
class UserRolesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: UserRole? = null

    @ManyToMany(mappedBy = "roles", targetEntity = User::class)
    private val users: Set<User> = setOf()
}