package org.nevertouchgrass.smartpertzalka.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "smart.jwt")
class JwtConfigurationProperties {
    var secret: String = ""
    var validityTimeMin: Long = 0
}