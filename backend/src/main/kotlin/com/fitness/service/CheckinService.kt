package com.fitness.service

import com.fitness.dto.*
import com.fitness.entity.DailyCheckin
import com.fitness.repository.DailyCheckinRepository
import com.fitness.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.YearMonth

@Service
class CheckinService(
    private val dailyCheckinRepository: DailyCheckinRepository,
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun getTodayCheckin(userId: Long): DailyCheckinResponse? {
        val today = LocalDate.now()
        return dailyCheckinRepository.findByUserIdAndCheckinDate(userId, today)
            .map { it.toResponse() }
            .orElse(null)
    }

    @Transactional(readOnly = true)
    fun getCheckin(userId: Long, date: LocalDate): DailyCheckinResponse? {
        return dailyCheckinRepository.findByUserIdAndCheckinDate(userId, date)
            .map { it.toResponse() }
            .orElse(null)
    }

    @Transactional
    fun createOrUpdateCheckin(userId: Long, request: DailyCheckinRequest): DailyCheckinResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        val existingCheckin = dailyCheckinRepository
            .findByUserIdAndCheckinDate(userId, request.checkinDate)
            .orElse(null)

        val checkin = if (existingCheckin != null) {
            existingCheckin.copy(
                weightKg = request.weightKg,
                bodyFatPercentage = request.bodyFatPercentage,
                sleepHours = request.sleepHours,
                energyLevel = request.energyLevel,
                mood = request.mood,
                workoutCompleted = request.workoutCompleted,
                notes = request.notes
            )
        } else {
            DailyCheckin(
                user = user,
                checkinDate = request.checkinDate,
                weightKg = request.weightKg,
                bodyFatPercentage = request.bodyFatPercentage,
                sleepHours = request.sleepHours,
                energyLevel = request.energyLevel,
                mood = request.mood,
                workoutCompleted = request.workoutCompleted,
                notes = request.notes
            )
        }

        val saved = dailyCheckinRepository.save(checkin)
        return saved.toResponse()
    }

    @Transactional(readOnly = true)
    fun getMonthlyCalendar(userId: Long, year: Int, month: Int): MonthlyCalendarResponse {
        val yearMonth = YearMonth.of(year, month)
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()

        val checkins = dailyCheckinRepository
            .findByUserIdAndCheckinDateBetweenOrderByCheckinDateAsc(userId, startDate, endDate)

        val checkinMap = checkins.associateBy { it.checkinDate }

        val days = (1..yearMonth.lengthOfMonth()).map { day ->
            val date = yearMonth.atDay(day)
            val checkin = checkinMap[date]
            CalendarDayResponse(
                date = date,
                hasCheckin = checkin != null,
                workoutCompleted = checkin?.workoutCompleted ?: false,
                weightKg = checkin?.weightKg?.toDouble()
            )
        }

        return MonthlyCalendarResponse(
            year = year,
            month = month,
            days = days
        )
    }

    @Transactional(readOnly = true)
    fun getRecentCheckins(userId: Long, limit: Int): List<DailyCheckinResponse> {
        return dailyCheckinRepository.findRecentByUserId(userId, limit)
            .map { it.toResponse() }
    }

    private fun DailyCheckin.toResponse() = DailyCheckinResponse(
        id = id,
        checkinDate = checkinDate,
        weightKg = weightKg?.toDouble(),
        bodyFatPercentage = bodyFatPercentage?.toDouble(),
        sleepHours = sleepHours?.toDouble(),
        energyLevel = energyLevel,
        mood = mood,
        workoutCompleted = workoutCompleted,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
