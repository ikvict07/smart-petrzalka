package org.nevertouchgrass.smartpertzalka.db.repository

import org.nevertouchgrass.smartpertzalka.db.entity.Playground
import org.nevertouchgrass.smartpertzalka.dto.responce.PlaygroundDTO
import org.springframework.data.repository.CrudRepository

interface PlaygroundRepository: CrudRepository<Playground, Long> {
    fun findByName(name: String): Playground?
}