package org.nevertouchgrass.smartpertzalka.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class WeekDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name= "day", nullable = false)
    @Enumerated(
        value = EnumType.STRING
    )
    var day: WeekDayEnum? = null

    @ManyToOne
    var price: Price? = null
}
