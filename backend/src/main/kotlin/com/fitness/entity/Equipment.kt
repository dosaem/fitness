package com.fitness.entity

import jakarta.persistence.*

enum class EquipmentCategory {
    FREE_WEIGHT,   // 덤벨, 바벨 등
    MACHINE,       // 기구
    BODYWEIGHT,    // 맨몸
    CARDIO,        // 유산소 기구
    ACCESSORY      // 밴드, 매트 등
}

@Entity
@Table(name = "equipment")
data class Equipment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val nameKo: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val category: EquipmentCategory
)
