package org.nevertouchgrass.smartpertzalka

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class SmartPertzalkaApplicationTests {

    @Test
    fun contextLoads() {
        val t = LocalTime.now()
        println(t.isBefore(t))
    }

}
