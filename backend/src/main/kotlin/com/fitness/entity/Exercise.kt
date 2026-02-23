package com.fitness.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDateTime

enum class ExerciseCategory {
    STRENGTH,   // 근력 운동
    CARDIO,     // 유산소 운동
    FLEXIBILITY // 유연성 운동
}

enum class Difficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}

@Entity
@Table(name = "exercises")
@EntityListeners(AuditingEntityListener::class)
data class Exercise(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val nameKo: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(columnDefinition = "TEXT")
    var instructions: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val category: ExerciseCategory,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val difficulty: Difficulty = Difficulty.BEGINNER,

    @Column
    var primaryImageUrl: String? = null,

    @Column
    var videoUrl: String? = null,

    @Column(precision = 5, scale = 2)
    var caloriesPerMinute: BigDecimal? = null,

    @ManyToMany
    @JoinTable(
        name = "exercise_muscle_groups",
        joinColumns = [JoinColumn(name = "exercise_id")],
        inverseJoinColumns = [JoinColumn(name = "muscle_group_id")]
    )
    val muscleGroups: MutableSet<MuscleGroup> = mutableSetOf(),

    @ManyToMany
    @JoinTable(
        name = "exercise_equipment",
        joinColumns = [JoinColumn(name = "exercise_id")],
        inverseJoinColumns = [JoinColumn(name = "equipment_id")]
    )
    val equipment: MutableSet<Equipment> = mutableSetOf(),

    @CreatedDate
    @Column(updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
