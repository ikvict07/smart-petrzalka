package org.nevertouchgrass.smartpertzalka.db.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne


@Entity(name = "playground_user")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "username", nullable = false)
    var name: String? = null

    @Column(name = "surname", nullable = false)
    var surname: String? = null

    @Column(name = "password", nullable = false)
    var password: String? = null

    @Column(name = "email", nullable = false)
    var email: String? = null

    @ManyToMany(cascade = [CascadeType.ALL], targetEntity = UserRolesEntity::class)
    @JoinTable(
        name = "user_to_role",
        joinColumns = [JoinColumn(name = "id")],
        inverseJoinColumns = [JoinColumn(name = "playground_userid")]
    )
    var roles: List<UserRolesEntity> = listOf()

    @Column(name = "phone_number", nullable = false)
    var phoneNumber: String? = null


    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var reservations: MutableList<Reservation>? = null

    @Column(name = "balance", nullable = false)
    var balance: Double = 0.0


    @OneToOne
    var creditCard: CreditCard? = null
}