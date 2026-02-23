package com.fitness.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "workout_logs")
@EntityListeners(AuditingEntityListener::class)
data class WorkoutLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    val exercise: Exercise,

    @Column(nullable = false)
    val workoutDate: LocalDate,

    // 근력 운동용
    @Column
    var sets: Int? = null,

    @Column
    var reps: Int? = null,

    @Column(precision = 6, scale = 2)
    var weightKg: BigDecimal? = null,

    // 유산소 운동용
    @Column
    var durationMinutes: Int? = null,

    @Column(precision = 8, scale = 2)
    var distanceKm: BigDecimal? = null,

    @Column(columnDefinition = "TEXT")
    var notes: String? = null,

    @Column
    var startedAt: LocalDateTime? = null,

    @Column
    var completedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
