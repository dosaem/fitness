package com.fitness.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class DailyCheckinRequest(
    @field:NotNull(message = "체크인 날짜를 입력해주세요")
    val checkinDate: LocalDate,

    val weightKg: BigDecimal? = null,

    val bodyFatPercentage: BigDecimal? = null,

    val sleepHours: BigDecimal? = null,

    @field:Min(1, message = "에너지 레벨은 1-5 사이여야 합니다")
    @field:Max(5, message = "에너지 레벨은 1-5 사이여야 합니다")
    val energyLevel: Int? = null,

    @field:Min(1, message = "기분은 1-5 사이여야 합니다")
    @field:Max(5, message = "기분은 1-5 사이여야 합니다")
    val mood: Int? = null,

    val workoutCompleted: Boolean = false,

    val notes: String? = null
)

data class DailyCheckinResponse(
    val id: Long,
    val checkinDate: LocalDate,
    val weightKg: Double?,
    val bodyFatPercentage: Double?,
    val sleepHours: Double?,
    val energyLevel: Int?,
    val mood: Int?,
    val workoutCompleted: Boolean,
    val notes: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class CalendarDayResponse(
    val date: LocalDate,
    val hasCheckin: Boolean,
    val workoutCompleted: Boolean,
    val weightKg: Double?
)

data class MonthlyCalendarResponse(
    val year: Int,
    val month: Int,
    val days: List<CalendarDayResponse>
)
