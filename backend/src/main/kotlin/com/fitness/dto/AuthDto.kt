package com.fitness.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "이메일을 입력해주세요")
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    val email: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요")
    @field:Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다")
    val password: String,

    @field:NotBlank(message = "닉네임을 입력해주세요")
    @field:Size(min = 2, max = 20, message = "닉네임은 2-20자 사이여야 합니다")
    val nickname: String
)

data class LoginRequest(
    @field:NotBlank(message = "이메일을 입력해주세요")
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    val email: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요")
    val password: String
)

data class RefreshTokenRequest(
    @field:NotBlank(message = "리프레시 토큰을 입력해주세요")
    val refreshToken: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserResponse
)

data class ForgotPasswordRequest(
    @field:NotBlank(message = "이메일을 입력해주세요")
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    val email: String
)

data class ResetPasswordRequest(
    @field:NotBlank(message = "토큰이 필요합니다")
    val token: String,

    @field:NotBlank(message = "새 비밀번호를 입력해주세요")
    @field:Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다")
    val newPassword: String
)

data class MessageResponse(
    val message: String
)

data class UserResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val profileImage: String?,
    val timezone: String,
    val locale: String,
    val heightCm: Double?,
    val weightKg: Double?
)
