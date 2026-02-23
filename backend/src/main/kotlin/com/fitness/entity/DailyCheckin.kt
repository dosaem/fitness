package com.fitness.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(
    name = "daily_checkins",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "checkin_date"])]
)
@EntityListeners(AuditingEntityListener::class)
data class DailyCheckin(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val checkinDate: LocalDate,

    @Column(precision = 5, scale = 2)
    var weightKg: BigDecimal? = null,

    @Column(precision = 4, scale = 1)
    var bodyFatPercentage: BigDecimal? = null,

    @Column(precision = 3, scale = 1)
    var sleepHours: BigDecimal? = null,

    @Column
    var energyLevel: Int? = null, // 1-5

    @Column
    var mood: Int? = null, // 1-5

    @Column(nullable = false)
    var workoutCompleted: Boolean = false,

    @Column(columnDefinition = "TEXT")
    var notes: String? = null,

    @CreatedDate
    @Column(updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
