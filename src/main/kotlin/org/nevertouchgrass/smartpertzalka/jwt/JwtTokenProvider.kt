package org.nevertouchgrass.smartpertzalka.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import org.nevertouchgrass.smartpertzalka.configuration.JwtConfigurationProperties
import org.nevertouchgrass.smartpertzalka.db.entity.UserRole
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*


@Component
class JwtTokenProvider(
    private val jwtConfigurationProperties: JwtConfigurationProperties,
) {
    private val decodedKey: ByteArray = Base64.getDecoder().decode(jwtConfigurationProperties.secret.toByteArray(StandardCharsets.UTF_8))
    private final val algorithm: Algorithm = Algorithm.HMAC256(decodedKey)
    val verifier: JWTVerifier = JWT.require(algorithm).build()

    fun createToken(username: String, roles: List<String>): String {

        val now = Date()
        val validity = Date(now.time + jwtConfigurationProperties.validityTimeMin * 60 * 1000)

        return JWT.create().withIssuer("main").withSubject(username).withClaim("roles", roles).withIssuedAt(now).withExpiresAt(validity).sign(algorithm)
    }

    fun validateToken(token: String): Boolean {
        try {
            val decodedJWT: DecodedJWT = verifier.verify(token)
        } catch (e: JWTVerificationException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun getEmail(token: String): String {
        return JWT.require(algorithm).build().verify(token).subject
    }


    fun getRoles(token: String): List<UserRole> {
        return JWT.require(algorithm).build().verify(token).claims["roles"]?.asList(String::class.java)?.map { UserRole.valueOf(it) } ?: emptyList()
    }

}