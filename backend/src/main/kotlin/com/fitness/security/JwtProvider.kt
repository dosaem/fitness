package com.fitness.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @Value("\${jwt.secret}") private val jwtSecret: String,
    @Value("\${jwt.access-token-validity}") private val accessTokenValidity: Long,
    @Value("\${jwt.refresh-token-validity}") private val refreshTokenValidity: Long
) {
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    fun generateAccessToken(userId: Long, email: String): String {
        return generateToken(userId, email, accessTokenValidity)
    }

    fun generateRefreshToken(userId: Long, email: String): String {
        return generateToken(userId, email, refreshTokenValidity)
    }

    private fun generateToken(userId: Long, email: String, validity: Long): String {
        val now = Date()
        val expiry = Date(now.time + validity)

        return Jwts.builder()
            .subject(userId.toString())
            .claim("email", email)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact()
    }

    fun generatePasswordResetToken(userId: Long, email: String): String {
        val now = Date()
        val expiry = Date(now.time + 900_000) // 15분

        return Jwts.builder()
            .subject(userId.toString())
            .claim("email", email)
            .claim("purpose", "password-reset")
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact()
    }

    fun isPasswordResetToken(token: String): Boolean {
        return try {
            val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
            claims["purpose"] == "password-reset"
        } catch (e: Exception) {
            false
        }
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUserIdFromToken(token: String): Long {
        val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
        return claims.subject.toLong()
    }

    fun getEmailFromToken(token: String): String {
        val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
        return claims["email"] as String
    }
}
