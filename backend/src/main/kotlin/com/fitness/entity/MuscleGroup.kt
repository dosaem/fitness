package com.fitness.entity

import jakarta.persistence.*

enum class BodyPart {
    UPPER_BODY,
    CORE,
    LOWER_BODY,
    FULL_BODY
}

@Entity
@Table(name = "muscle_groups")
data class MuscleGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val nameKo: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val bodyPart: BodyPart
)
