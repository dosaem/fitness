package com.fitness.dto

import java.time.LocalDate

data class OverviewStatsResponse(
    val totalWorkouts: Long,
    val totalWorkoutDays: Long,
    val currentStreak: Int,
    val longestStreak: Int,
    val thisWeekWorkouts: Long,
    val thisMonthWorkouts: Long,
    val averageWorkoutsPerWeek: Double
)

data class StreakResponse(
    val currentStreak: Int,
    val longestStreak: Int,
    val lastWorkoutDate: LocalDate?
)

data class WeightHistoryResponse(
    val date: LocalDate,
    val weightKg: Double
)

data class WorkoutFrequencyResponse(
    val date: LocalDate,
    val count: Int
)
