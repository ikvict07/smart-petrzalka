package org.nevertouchgrass.smartpertzalka.db.repository

import org.nevertouchgrass.smartpertzalka.db.entity.CreditCard
import org.springframework.data.repository.CrudRepository

interface CreditCardRepository : CrudRepository<CreditCard, Long> {
}