package com.fitness.service

import com.fitness.dto.*
import com.fitness.entity.User
import com.fitness.repository.UserRepository
import com.fitness.security.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider,
    private val emailService: EmailService
) {

    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 사용중인 이메일입니다")
        }

        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            nickname = request.nickname
        )

        val savedUser = userRepository.save(user)
        return generateAuthResponse(savedUser)
    }

    @Transactional(readOnly = true)
    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다") }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다")
        }

        return generateAuthResponse(user)
    }

    fun refreshToken(request: RefreshTokenRequest): AuthResponse {
        if (!jwtProvider.validateToken(request.refreshToken)) {
            throw IllegalArgumentException("유효하지 않은 리프레시 토큰입니다")
        }

        val userId = jwtProvider.getUserIdFromToken(request.refreshToken)
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        return generateAuthResponse(user)
    }

    fun forgotPassword(request: ForgotPasswordRequest): MessageResponse {
        val user = userRepository.findByEmail(request.email).orElse(null)
        if (user != null) {
            try {
                val token = jwtProvider.generatePasswordResetToken(user.id, user.email)
                emailService.sendPasswordResetEmail(user.email, token)
            } catch (e: Exception) {
                // 이메일 발송 실패해도 동일 응답 (보안)
            }
        }
        return MessageResponse("비밀번호 재설정 링크가 이메일로 전송되었습니다")
    }

    @Transactional
    fun resetPassword(request: ResetPasswordRequest): MessageResponse {
        if (!jwtProvider.validateToken(request.token) || !jwtProvider.isPasswordResetToken(request.token)) {
            throw IllegalArgumentException("유효하지 않거나 만료된 토큰입니다")
        }

        val userId = jwtProvider.getUserIdFromToken(request.token)
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        user.password = passwordEncoder.encode(request.newPassword)
        userRepository.save(user)

        return MessageResponse("비밀번호가 성공적으로 변경되었습니다")
    }

    private fun generateAuthResponse(user: User): AuthResponse {
        val accessToken = jwtProvider.generateAccessToken(user.id, user.email)
        val refreshToken = jwtProvider.generateRefreshToken(user.id, user.email)

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            user = user.toResponse()
        )
    }

    private fun User.toResponse() = UserResponse(
        id = id,
        email = email,
        nickname = nickname,
        profileImage = profileImage,
        timezone = timezone,
        locale = locale,
        heightCm = heightCm?.toDouble(),
        weightKg = weightKg?.toDouble()
    )
}
