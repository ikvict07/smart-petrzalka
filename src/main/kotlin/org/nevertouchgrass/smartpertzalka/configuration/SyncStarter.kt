package org.nevertouchgrass.smartpertzalka.configuration

import org.nevertouchgrass.smartpertzalka.service.SyncReservationsService
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class SyncStarter(
    private val syncReservationsService: SyncReservationsService
) {

    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationEvent() {
        syncReservationsService.activate()
        syncReservationsService.sync()
    }
}