package com.fitness.dto

import com.fitness.entity.ExerciseCategory
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkoutLogRequest(
    @field:NotNull(message = "운동 ID를 입력해주세요")
    val exerciseId: Long,

    @field:NotNull(message = "운동 날짜를 입력해주세요")
    val workoutDate: LocalDate,

    // 근력 운동용
    @field:Positive(message = "세트 수는 양수여야 합니다")
    val sets: Int? = null,

    @field:Positive(message = "반복 수는 양수여야 합니다")
    val reps: Int? = null,

    val weightKg: BigDecimal? = null,

    // 유산소 운동용
    @field:Positive(message = "운동 시간은 양수여야 합니다")
    val durationMinutes: Int? = null,

    val distanceKm: BigDecimal? = null,

    val notes: String? = null,
    val startedAt: LocalDateTime? = null,
    val completedAt: LocalDateTime? = null
)

data class WorkoutLogResponse(
    val id: Long,
    val exerciseId: Long,
    val exerciseName: String,
    val exerciseNameKo: String,
    val exerciseCategory: ExerciseCategory,
    val workoutDate: LocalDate,
    val sets: Int?,
    val reps: Int?,
    val weightKg: Double?,
    val durationMinutes: Int?,
    val distanceKm: Double?,
    val notes: String?,
    val startedAt: LocalDateTime?,
    val completedAt: LocalDateTime?,
    val createdAt: LocalDateTime
)

data class DailyWorkoutSummary(
    val date: LocalDate,
    val workouts: List<WorkoutLogResponse>,
    val totalExercises: Int,
    val totalSets: Int,
    val totalDurationMinutes: Int
)
