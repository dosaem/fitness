package com.fitness.repository

import com.fitness.entity.WorkoutLog
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface WorkoutLogRepository : JpaRepository<WorkoutLog, Long> {

    fun findByUserIdOrderByWorkoutDateDescCreatedAtDesc(userId: Long, pageable: Pageable): Page<WorkoutLog>

    fun findByUserIdAndWorkoutDate(userId: Long, workoutDate: LocalDate): List<WorkoutLog>

    fun findByUserIdAndWorkoutDateBetween(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<WorkoutLog>

    @Query("""
        SELECT w FROM WorkoutLog w
        WHERE w.user.id = :userId
          AND w.exercise.id = :exerciseId
        ORDER BY w.workoutDate DESC, w.createdAt DESC
    """)
    fun findByUserIdAndExerciseId(
        @Param("userId") userId: Long,
        @Param("exerciseId") exerciseId: Long,
        pageable: Pageable
    ): Page<WorkoutLog>

    @Query("""
        SELECT COUNT(DISTINCT w.workoutDate)
        FROM WorkoutLog w
        WHERE w.user.id = :userId
          AND w.workoutDate BETWEEN :startDate AND :endDate
    """)
    fun countDistinctWorkoutDays(
        @Param("userId") userId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Long
}
