package com.fitness.repository

import com.fitness.entity.DailyCheckin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
interface DailyCheckinRepository : JpaRepository<DailyCheckin, Long> {

    fun findByUserIdAndCheckinDate(userId: Long, checkinDate: LocalDate): Optional<DailyCheckin>

    fun findByUserIdAndCheckinDateBetweenOrderByCheckinDateAsc(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DailyCheckin>

    @Query("""
        SELECT c FROM DailyCheckin c
        WHERE c.user.id = :userId
        ORDER BY c.checkinDate DESC
        LIMIT :limit
    """)
    fun findRecentByUserId(@Param("userId") userId: Long, @Param("limit") limit: Int): List<DailyCheckin>

    @Query("""
        SELECT COUNT(c) FROM DailyCheckin c
        WHERE c.user.id = :userId
          AND c.workoutCompleted = true
          AND c.checkinDate BETWEEN :startDate AND :endDate
    """)
    fun countWorkoutCompletedDays(
        @Param("userId") userId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Long
}
