package org.nevertouchgrass.smartpertzalka.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne


@Entity
class   CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @OneToOne
    var user: User? = null

    @Column(name = "card_number", nullable = false)
    var cardNumber: String? = null

    @Column(name = "expiration_date", nullable = false)
    var expirationDate: String? = null

    @Column(name = "cvv", nullable = false)
    var cvv: String? = null
}