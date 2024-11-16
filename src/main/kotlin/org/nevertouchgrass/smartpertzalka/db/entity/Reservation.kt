package org.nevertouchgrass.smartpertzalka.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Entity
class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "start_time", nullable = false)
    var startTime: LocalTime? = null

    @Column(name = "end_time", nullable = false)
    var endTime: LocalTime? = null

    @Column(name = "day", nullable = false)
    var day: LocalDate? = null

    @ManyToOne
    @JoinColumn(name = "playground_id", nullable = false)
    var playground: Playground? = null

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null


    @Column(name = "price", nullable = false)
    var price: Double? = null

    @Column(name = "status", nullable = false)
    @Enumerated(
        value = EnumType.STRING
    )
    var status: ReservationStatus? = null

    @Column(name = "uuid", nullable = true)
    var uuid: String = UUID.randomUUID().toString()
}