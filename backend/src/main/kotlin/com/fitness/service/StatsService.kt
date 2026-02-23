package com.fitness.service

import com.fitness.dto.*
import com.fitness.repository.DailyCheckinRepository
import com.fitness.repository.WorkoutLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Service
class StatsService(
    private val workoutLogRepository: WorkoutLogRepository,
    private val dailyCheckinRepository: DailyCheckinRepository
) {

    @Transactional(readOnly = true)
    fun getOverviewStats(userId: Long): OverviewStatsResponse {
        val today = LocalDate.now()
        val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val startOfMonth = today.withDayOfMonth(1)
        val thirtyDaysAgo = today.minusDays(30)
        val oneYearAgo = today.minusYears(1)

        val totalWorkoutDays = workoutLogRepository.countDistinctWorkoutDays(userId, oneYearAgo, today)
        val thisWeekWorkouts = workoutLogRepository.countDistinctWorkoutDays(userId, startOfWeek, today)
        val thisMonthWorkouts = workoutLogRepository.countDistinctWorkoutDays(userId, startOfMonth, today)

        val streak = calculateStreak(userId)

        val weeksInPeriod = 4.0
        val last4WeeksWorkouts = workoutLogRepository.countDistinctWorkoutDays(
            userId,
            today.minusWeeks(4),
            today
        )
        val averageWorkoutsPerWeek = last4WeeksWorkouts / weeksInPeriod

        return OverviewStatsResponse(
            totalWorkouts = totalWorkoutDays,
            totalWorkoutDays = totalWorkoutDays,
            currentStreak = streak.currentStreak,
            longestStreak = streak.longestStreak,
            thisWeekWorkouts = thisWeekWorkouts,
            thisMonthWorkouts = thisMonthWorkouts,
            averageWorkoutsPerWeek = averageWorkoutsPerWeek
        )
    }

    @Transactional(readOnly = true)
    fun getStreak(userId: Long): StreakResponse {
        return calculateStreak(userId)
    }

    @Transactional(readOnly = true)
    fun getWeightHistory(userId: Long, days: Int): List<WeightHistoryResponse> {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(days.toLong())

        return dailyCheckinRepository
            .findByUserIdAndCheckinDateBetweenOrderByCheckinDateAsc(userId, startDate, endDate)
            .filter { it.weightKg != null }
            .map {
                WeightHistoryResponse(
                    date = it.checkinDate,
                    weightKg = it.weightKg!!.toDouble()
                )
            }
    }

    @Transactional(readOnly = true)
    fun getWorkoutFrequency(userId: Long, days: Int): List<WorkoutFrequencyResponse> {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(days.toLong())

        val workouts = workoutLogRepository.findByUserIdAndWorkoutDateBetween(userId, startDate, endDate)

        return workouts
            .groupBy { it.workoutDate }
            .map { (date, logs) ->
                WorkoutFrequencyResponse(
                    date = date,
                    count = logs.size
                )
            }
            .sortedBy { it.date }
    }

    private fun calculateStreak(userId: Long): StreakResponse {
        val today = LocalDate.now()
        val ninetyDaysAgo = today.minusDays(90)

        val checkins = dailyCheckinRepository
            .findByUserIdAndCheckinDateBetweenOrderByCheckinDateAsc(userId, ninetyDaysAgo, today)
            .filter { it.workoutCompleted }
            .map { it.checkinDate }
            .toSet()

        if (checkins.isEmpty()) {
            return StreakResponse(
                currentStreak = 0,
                longestStreak = 0,
                lastWorkoutDate = null
            )
        }

        var currentStreak = 0
        var longestStreak = 0
        var tempStreak = 0
        var lastWorkoutDate: LocalDate? = null

        var checkDate = today
        while (checkDate >= ninetyDaysAgo) {
            if (checkins.contains(checkDate)) {
                tempStreak++
                if (lastWorkoutDate == null) {
                    lastWorkoutDate = checkDate
                }
            } else {
                if (currentStreak == 0 && tempStreak > 0) {
                    currentStreak = tempStreak
                }
                longestStreak = maxOf(longestStreak, tempStreak)
                tempStreak = 0
            }
            checkDate = checkDate.minusDays(1)
        }

        longestStreak = maxOf(longestStreak, tempStreak)
        if (currentStreak == 0) {
            currentStreak = tempStreak
        }

        // 오늘 운동을 안 했으면 현재 스트릭 확인
        if (!checkins.contains(today) && !checkins.contains(today.minusDays(1))) {
            currentStreak = 0
        }

        return StreakResponse(
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            lastWorkoutDate = lastWorkoutDate
        )
    }
}
