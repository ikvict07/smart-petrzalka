package org.nevertouchgrass.smartpertzalka.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "price_multiplier", nullable = false)
    var priceMultiplier: Double? = null

    @Column(name = "start_time", nullable = false)
    var startTime: LocalDateTime? = null

    @Column(name = "end_time", nullable = false)
    var endTime: LocalDateTime? = null

    @Column(name = "week_days", nullable = true)
    @OneToMany(mappedBy = "price")
    var weekDays: MutableList<WeekDay>? = null

    @Column(name= "day", nullable = true)
    var day: LocalDate? = null

    @Column(name = "is_closed", nullable = false)
    var isClosed: Boolean? = null

    @ManyToOne
    var playground: Playground? = null
}