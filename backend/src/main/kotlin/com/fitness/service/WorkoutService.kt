package com.fitness.service

import com.fitness.dto.*
import com.fitness.entity.WorkoutLog
import com.fitness.repository.ExerciseRepository
import com.fitness.repository.UserRepository
import com.fitness.repository.WorkoutLogRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class WorkoutService(
    private val workoutLogRepository: WorkoutLogRepository,
    private val userRepository: UserRepository,
    private val exerciseRepository: ExerciseRepository
) {

    @Transactional(readOnly = true)
    fun getWorkouts(userId: Long, page: Int, size: Int): Page<WorkoutLogResponse> {
        val pageable = PageRequest.of(page, size)
        return workoutLogRepository.findByUserIdOrderByWorkoutDateDescCreatedAtDesc(userId, pageable)
            .map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getWorkoutsByDate(userId: Long, date: LocalDate): List<WorkoutLogResponse> {
        return workoutLogRepository.findByUserIdAndWorkoutDate(userId, date)
            .map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getWorkoutsByDateRange(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DailyWorkoutSummary> {
        val workouts = workoutLogRepository.findByUserIdAndWorkoutDateBetween(userId, startDate, endDate)

        return workouts
            .groupBy { it.workoutDate }
            .map { (date, logs) ->
                DailyWorkoutSummary(
                    date = date,
                    workouts = logs.map { it.toResponse() },
                    totalExercises = logs.size,
                    totalSets = logs.mapNotNull { it.sets }.sum(),
                    totalDurationMinutes = logs.mapNotNull { it.durationMinutes }.sum()
                )
            }
            .sortedByDescending { it.date }
    }

    @Transactional
    fun createWorkout(userId: Long, request: WorkoutLogRequest): WorkoutLogResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        val exercise = exerciseRepository.findById(request.exerciseId)
            .orElseThrow { IllegalArgumentException("운동을 찾을 수 없습니다") }

        val workoutLog = WorkoutLog(
            user = user,
            exercise = exercise,
            workoutDate = request.workoutDate,
            sets = request.sets,
            reps = request.reps,
            weightKg = request.weightKg,
            durationMinutes = request.durationMinutes,
            distanceKm = request.distanceKm,
            notes = request.notes,
            startedAt = request.startedAt,
            completedAt = request.completedAt
        )

        val saved = workoutLogRepository.save(workoutLog)
        return saved.toResponse()
    }

    @Transactional
    fun updateWorkout(userId: Long, workoutId: Long, request: WorkoutLogRequest): WorkoutLogResponse {
        val workoutLog = workoutLogRepository.findById(workoutId)
            .orElseThrow { IllegalArgumentException("운동 기록을 찾을 수 없습니다") }

        if (workoutLog.user.id != userId) {
            throw IllegalArgumentException("권한이 없습니다")
        }

        val exercise = if (request.exerciseId != workoutLog.exercise.id) {
            exerciseRepository.findById(request.exerciseId)
                .orElseThrow { IllegalArgumentException("운동을 찾을 수 없습니다") }
        } else {
            workoutLog.exercise
        }

        val updated = workoutLog.copy(
            exercise = exercise,
            workoutDate = request.workoutDate,
            sets = request.sets,
            reps = request.reps,
            weightKg = request.weightKg,
            durationMinutes = request.durationMinutes,
            distanceKm = request.distanceKm,
            notes = request.notes,
            startedAt = request.startedAt,
            completedAt = request.completedAt
        )

        return workoutLogRepository.save(updated).toResponse()
    }

    @Transactional
    fun deleteWorkout(userId: Long, workoutId: Long) {
        val workoutLog = workoutLogRepository.findById(workoutId)
            .orElseThrow { IllegalArgumentException("운동 기록을 찾을 수 없습니다") }

        if (workoutLog.user.id != userId) {
            throw IllegalArgumentException("권한이 없습니다")
        }

        workoutLogRepository.delete(workoutLog)
    }

    private fun WorkoutLog.toResponse() = WorkoutLogResponse(
        id = id,
        exerciseId = exercise.id,
        exerciseName = exercise.name,
        exerciseNameKo = exercise.nameKo,
        exerciseCategory = exercise.category,
        workoutDate = workoutDate,
        sets = sets,
        reps = reps,
        weightKg = weightKg?.toDouble(),
        durationMinutes = durationMinutes,
        distanceKm = distanceKm?.toDouble(),
        notes = notes,
        startedAt = startedAt,
        completedAt = completedAt,
        createdAt = createdAt
    )
}
