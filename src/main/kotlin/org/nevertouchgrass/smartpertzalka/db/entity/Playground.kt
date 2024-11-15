package org.nevertouchgrass.smartpertzalka.db.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Playground {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "name", nullable = false)
    var name: String? = null

    @OneToMany(mappedBy = "playground", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var reservations: MutableList<Reservation>? = null

    @Column(name = "max_capacity", nullable = false)
    var maxCapacity: Int? = null


}