package com.fitness.config

import com.fitness.entity.*
import com.fitness.repository.EquipmentRepository
import com.fitness.repository.ExerciseRepository
import com.fitness.repository.MuscleGroupRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.math.BigDecimal

@Configuration
class DataSeeder {

    @Bean
    @Profile("local", "dev", "prod")
    fun seedData(
        muscleGroupRepository: MuscleGroupRepository,
        equipmentRepository: EquipmentRepository,
        exerciseRepository: ExerciseRepository
    ): CommandLineRunner {
        return CommandLineRunner {
            if (muscleGroupRepository.count() > 0) {
                return@CommandLineRunner
            }

            // 근육 그룹 생성
            val muscleGroupsList = listOf(
                MuscleGroup(name = "chest", nameKo = "가슴", bodyPart = BodyPart.UPPER_BODY),
                MuscleGroup(name = "back", nameKo = "등", bodyPart = BodyPart.UPPER_BODY),
                MuscleGroup(name = "shoulders", nameKo = "어깨", bodyPart = BodyPart.UPPER_BODY),
                MuscleGroup(name = "biceps", nameKo = "이두근", bodyPart = BodyPart.UPPER_BODY),
                MuscleGroup(name = "triceps", nameKo = "삼두근", bodyPart = BodyPart.UPPER_BODY),
                MuscleGroup(name = "forearms", nameKo = "전완근", bodyPart = BodyPart.UPPER_BODY),
                MuscleGroup(name = "abs", nameKo = "복근", bodyPart = BodyPart.CORE),
                MuscleGroup(name = "obliques", nameKo = "옆구리", bodyPart = BodyPart.CORE),
                MuscleGroup(name = "lower_back", nameKo = "허리", bodyPart = BodyPart.CORE),
                MuscleGroup(name = "quadriceps", nameKo = "대퇴사두근", bodyPart = BodyPart.LOWER_BODY),
                MuscleGroup(name = "hamstrings", nameKo = "햄스트링", bodyPart = BodyPart.LOWER_BODY),
                MuscleGroup(name = "glutes", nameKo = "둔근", bodyPart = BodyPart.LOWER_BODY),
                MuscleGroup(name = "calves", nameKo = "종아리", bodyPart = BodyPart.LOWER_BODY),
                MuscleGroup(name = "hip_flexors", nameKo = "고관절", bodyPart = BodyPart.LOWER_BODY)
            )
            val savedMuscleGroups = muscleGroupRepository.saveAll(muscleGroupsList)
            val mgMap = savedMuscleGroups.associateBy { it.name }

            // 장비 생성
            val equipmentList = listOf(
                Equipment(name = "barbell", nameKo = "바벨", category = EquipmentCategory.FREE_WEIGHT),
                Equipment(name = "dumbbell", nameKo = "덤벨", category = EquipmentCategory.FREE_WEIGHT),
                Equipment(name = "kettlebell", nameKo = "케틀벨", category = EquipmentCategory.FREE_WEIGHT),
                Equipment(name = "cable_machine", nameKo = "케이블 머신", category = EquipmentCategory.MACHINE),
                Equipment(name = "smith_machine", nameKo = "스미스 머신", category = EquipmentCategory.MACHINE),
                Equipment(name = "leg_press", nameKo = "레그프레스", category = EquipmentCategory.MACHINE),
                Equipment(name = "lat_pulldown", nameKo = "랫풀다운 머신", category = EquipmentCategory.MACHINE),
                Equipment(name = "bodyweight", nameKo = "맨몸", category = EquipmentCategory.BODYWEIGHT),
                Equipment(name = "pull_up_bar", nameKo = "턱걸이 바", category = EquipmentCategory.BODYWEIGHT),
                Equipment(name = "treadmill", nameKo = "런닝머신", category = EquipmentCategory.CARDIO),
                Equipment(name = "stationary_bike", nameKo = "실내자전거", category = EquipmentCategory.CARDIO),
                Equipment(name = "rowing_machine", nameKo = "로잉머신", category = EquipmentCategory.CARDIO),
                Equipment(name = "resistance_band", nameKo = "저항 밴드", category = EquipmentCategory.ACCESSORY),
                Equipment(name = "exercise_mat", nameKo = "운동 매트", category = EquipmentCategory.ACCESSORY),
                Equipment(name = "foam_roller", nameKo = "폼롤러", category = EquipmentCategory.ACCESSORY)
            )
            val savedEquipment = equipmentRepository.saveAll(equipmentList)
            val eqMap = savedEquipment.associateBy { it.name }

            // 운동 생성
            val exercises = mutableListOf<Exercise>()

            // 가슴 운동
            exercises.add(Exercise(
                name = "Bench Press",
                nameKo = "벤치프레스",
                description = "가슴과 삼두근을 단련하는 대표적인 복합 운동",
                instructions = "1. 벤치에 눕고 바벨을 어깨너비보다 넓게 잡습니다.\n2. 가슴 위로 바벨을 내렸다가 밀어 올립니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("8.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["chest"], mgMap["triceps"]))
                this.equipment.addAll(listOfNotNull(eqMap["barbell"]))
            })

            exercises.add(Exercise(
                name = "Push Up",
                nameKo = "푸쉬업",
                description = "가슴, 어깨, 삼두근을 단련하는 맨몸 운동",
                instructions = "1. 양손을 어깨너비로 벌리고 바닥에 짚습니다.\n2. 몸을 일직선으로 유지하며 팔꿈치를 굽혀 내려갑니다.\n3. 팔을 펴며 올라옵니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("7.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1598971639058-fab3c3109a00?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["chest"], mgMap["triceps"], mgMap["shoulders"]))
                this.equipment.addAll(listOfNotNull(eqMap["bodyweight"]))
            })

            exercises.add(Exercise(
                name = "Dumbbell Fly",
                nameKo = "덤벨 플라이",
                description = "가슴 근육을 스트레칭하며 단련하는 운동",
                instructions = "1. 벤치에 누워 덤벨을 양손에 듭니다.\n2. 팔을 옆으로 벌리며 내립니다.\n3. 가슴을 조이며 덤벨을 모읍니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("6.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1534368270820-9de3d8053204?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["chest"]))
                this.equipment.addAll(listOfNotNull(eqMap["dumbbell"]))
            })

            // 등 운동
            exercises.add(Exercise(
                name = "Pull Up",
                nameKo = "턱걸이",
                description = "등과 이두근을 단련하는 대표적인 맨몸 운동",
                instructions = "1. 바를 어깨너비보다 넓게 잡습니다.\n2. 팔을 당겨 턱이 바 위로 올라가도록 합니다.\n3. 천천히 내려옵니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("8.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1598266663439-2056e6900339?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["back"], mgMap["biceps"]))
                this.equipment.addAll(listOfNotNull(eqMap["pull_up_bar"]))
            })

            exercises.add(Exercise(
                name = "Lat Pulldown",
                nameKo = "랫풀다운",
                description = "광배근을 집중적으로 단련하는 머신 운동",
                instructions = "1. 바를 어깨너비보다 넓게 잡습니다.\n2. 바를 가슴 위쪽으로 당깁니다.\n3. 천천히 원위치합니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("6.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["back"], mgMap["biceps"]))
                this.equipment.addAll(listOfNotNull(eqMap["lat_pulldown"]))
            })

            exercises.add(Exercise(
                name = "Barbell Row",
                nameKo = "바벨 로우",
                description = "등 전체를 단련하는 복합 운동",
                instructions = "1. 바벨을 잡고 상체를 앞으로 숙입니다.\n2. 바벨을 배꼽 쪽으로 당깁니다.\n3. 천천히 내립니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("7.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1603287681836-b174ce5074c2?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["back"], mgMap["biceps"]))
                this.equipment.addAll(listOfNotNull(eqMap["barbell"]))
            })

            // 어깨 운동
            exercises.add(Exercise(
                name = "Shoulder Press",
                nameKo = "숄더프레스",
                description = "어깨 전면을 단련하는 운동",
                instructions = "1. 덤벨을 어깨 높이에 듭니다.\n2. 머리 위로 밀어 올립니다.\n3. 천천히 내립니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("6.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1532029837206-abbe2b7620e3?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["shoulders"], mgMap["triceps"]))
                this.equipment.addAll(listOfNotNull(eqMap["dumbbell"]))
            })

            exercises.add(Exercise(
                name = "Lateral Raise",
                nameKo = "레터럴 레이즈",
                description = "어깨 측면을 단련하는 고립 운동",
                instructions = "1. 덤벨을 양손에 들고 섭니다.\n2. 팔을 옆으로 들어 올립니다.\n3. 천천히 내립니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("5.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1581009146145-b5ef050c149a?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["shoulders"]))
                this.equipment.addAll(listOfNotNull(eqMap["dumbbell"]))
            })

            // 하체 운동
            exercises.add(Exercise(
                name = "Squat",
                nameKo = "스쿼트",
                description = "하체 전체를 단련하는 대표적인 복합 운동",
                instructions = "1. 발을 어깨너비로 벌리고 섭니다.\n2. 엉덩이를 뒤로 빼며 앉습니다.\n3. 허벅지가 바닥과 평행이 되면 일어납니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("8.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1574680096145-d05b474e2155?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["quadriceps"], mgMap["glutes"], mgMap["hamstrings"]))
                this.equipment.addAll(listOfNotNull(eqMap["bodyweight"]))
            })

            exercises.add(Exercise(
                name = "Barbell Squat",
                nameKo = "바벨 스쿼트",
                description = "바벨을 이용한 하체 복합 운동",
                instructions = "1. 바벨을 등 위에 올립니다.\n2. 엉덩이를 뒤로 빼며 앉습니다.\n3. 허벅지가 바닥과 평행이 되면 일어납니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("10.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1566241142559-40e1dab266c6?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["quadriceps"], mgMap["glutes"], mgMap["hamstrings"]))
                this.equipment.addAll(listOfNotNull(eqMap["barbell"]))
            })

            exercises.add(Exercise(
                name = "Leg Press",
                nameKo = "레그프레스",
                description = "하체 전면을 단련하는 머신 운동",
                instructions = "1. 머신에 앉아 발판에 발을 올립니다.\n2. 무릎을 펴며 발판을 밀어냅니다.\n3. 천천히 굽힙니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("7.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1623874514711-0f321325f318?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["quadriceps"], mgMap["glutes"]))
                this.equipment.addAll(listOfNotNull(eqMap["leg_press"]))
            })

            exercises.add(Exercise(
                name = "Lunges",
                nameKo = "런지",
                description = "하체와 균형감각을 단련하는 운동",
                instructions = "1. 한 발을 앞으로 내딛습니다.\n2. 뒷무릎이 바닥 가까이 올 때까지 내려갑니다.\n3. 앞발로 밀어 일어납니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("7.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1434608519344-49d77a699e1d?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["quadriceps"], mgMap["glutes"], mgMap["hamstrings"]))
                this.equipment.addAll(listOfNotNull(eqMap["bodyweight"]))
            })

            exercises.add(Exercise(
                name = "Deadlift",
                nameKo = "데드리프트",
                description = "전신을 단련하는 대표적인 복합 운동",
                instructions = "1. 바벨 앞에 서서 바를 잡습니다.\n2. 등을 곧게 펴고 일어납니다.\n3. 천천히 내려놓습니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.ADVANCED,
                caloriesPerMinute = BigDecimal("10.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1517963879433-6ad2b056d712?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["hamstrings"], mgMap["glutes"], mgMap["back"], mgMap["lower_back"]))
                this.equipment.addAll(listOfNotNull(eqMap["barbell"]))
            })

            exercises.add(Exercise(
                name = "Calf Raise",
                nameKo = "카프레이즈",
                description = "종아리를 단련하는 고립 운동",
                instructions = "1. 발 앞꿈치로 서서 뒤꿈치를 올립니다.\n2. 정점에서 잠시 유지합니다.\n3. 천천히 내립니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("4.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1605296867424-35fc25c9212a?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["calves"]))
                this.equipment.addAll(listOfNotNull(eqMap["bodyweight"]))
            })

            // 팔 운동
            exercises.add(Exercise(
                name = "Bicep Curl",
                nameKo = "바이셉 컬",
                description = "이두근을 단련하는 고립 운동",
                instructions = "1. 덤벨을 양손에 듭니다.\n2. 팔꿈치를 고정하고 덤벨을 들어올립니다.\n3. 천천히 내립니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("5.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1581009137042-c552e485697a?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["biceps"]))
                this.equipment.addAll(listOfNotNull(eqMap["dumbbell"]))
            })

            exercises.add(Exercise(
                name = "Tricep Extension",
                nameKo = "트라이셉 익스텐션",
                description = "삼두근을 단련하는 고립 운동",
                instructions = "1. 덤벨을 머리 위로 듭니다.\n2. 팔꿈치를 고정하고 덤벨을 머리 뒤로 내립니다.\n3. 팔을 펴며 올립니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("5.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1530822847156-5df684ec5ee1?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["triceps"]))
                this.equipment.addAll(listOfNotNull(eqMap["dumbbell"]))
            })

            exercises.add(Exercise(
                name = "Tricep Dip",
                nameKo = "트라이셉 딥",
                description = "삼두근과 가슴을 단련하는 맨몸 운동",
                instructions = "1. 양손으로 벤치를 잡고 몸을 앞으로 내립니다.\n2. 팔꿈치를 굽혀 몸을 내립니다.\n3. 팔을 펴며 올라옵니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("6.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1597452485669-2c7bb5fef90d?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["triceps"], mgMap["chest"]))
                this.equipment.addAll(listOfNotNull(eqMap["bodyweight"]))
            })

            // 코어 운동
            exercises.add(Exercise(
                name = "Plank",
                nameKo = "플랭크",
                description = "코어 전체를 단련하는 정적 운동",
                instructions = "1. 팔꿈치와 발끝으로 몸을 지탱합니다.\n2. 몸을 일직선으로 유지합니다.\n3. 정해진 시간 동안 버팁니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("4.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1566241142559-40e1dab266c6?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["abs"], mgMap["obliques"], mgMap["lower_back"]))
                this.equipment.addAll(listOfNotNull(eqMap["exercise_mat"]))
            })

            exercises.add(Exercise(
                name = "Crunch",
                nameKo = "크런치",
                description = "복근 상부를 단련하는 운동",
                instructions = "1. 바닥에 누워 무릎을 굽힙니다.\n2. 상체를 들어 올립니다.\n3. 천천히 내립니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("5.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["abs"]))
                this.equipment.addAll(listOfNotNull(eqMap["exercise_mat"]))
            })

            exercises.add(Exercise(
                name = "Leg Raise",
                nameKo = "레그레이즈",
                description = "복근 하부를 단련하는 운동",
                instructions = "1. 바닥에 누워 다리를 펴줍니다.\n2. 다리를 들어 올립니다.\n3. 천천히 내립니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("5.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["abs"], mgMap["hip_flexors"]))
                this.equipment.addAll(listOfNotNull(eqMap["exercise_mat"]))
            })

            exercises.add(Exercise(
                name = "Russian Twist",
                nameKo = "러시안 트위스트",
                description = "옆구리를 단련하는 회전 운동",
                instructions = "1. 바닥에 앉아 상체를 뒤로 기울입니다.\n2. 몸을 좌우로 회전합니다.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("6.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["obliques"], mgMap["abs"]))
                this.equipment.addAll(listOfNotNull(eqMap["exercise_mat"]))
            })

            // 유산소 운동
            exercises.add(Exercise(
                name = "Running",
                nameKo = "달리기",
                description = "심폐지구력을 향상시키는 기본 유산소 운동",
                instructions = "1. 적절한 속도로 달립니다.\n2. 호흡을 일정하게 유지합니다.",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("10.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1476480862126-209bfaa8edc8?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["quadriceps"], mgMap["calves"]))
                this.equipment.addAll(listOfNotNull(eqMap["treadmill"]))
            })

            exercises.add(Exercise(
                name = "Cycling",
                nameKo = "자전거",
                description = "하체와 심폐지구력을 단련하는 유산소 운동",
                instructions = "1. 페달을 밟아 자전거를 굴립니다.\n2. 적절한 저항으로 운동합니다.",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("8.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1517649763962-0c623066013b?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["quadriceps"], mgMap["calves"]))
                this.equipment.addAll(listOfNotNull(eqMap["stationary_bike"]))
            })

            exercises.add(Exercise(
                name = "Rowing",
                nameKo = "로잉",
                description = "전신을 사용하는 유산소 운동",
                instructions = "1. 핸들을 잡고 다리로 밀어냅니다.\n2. 팔로 당기며 마무리합니다.\n3. 역순으로 돌아갑니다.",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("9.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1519505907962-0a6cb0167c73?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["back"], mgMap["quadriceps"]))
                this.equipment.addAll(listOfNotNull(eqMap["rowing_machine"]))
            })

            exercises.add(Exercise(
                name = "Jumping Jack",
                nameKo = "점핑잭",
                description = "전신 유산소 운동",
                instructions = "1. 점프하며 팔다리를 벌립니다.\n2. 다시 점프하며 모읍니다.",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("8.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1601422407692-ec4eeec1d9b3?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["quadriceps"], mgMap["calves"]))
                this.equipment.addAll(listOfNotNull(eqMap["bodyweight"]))
            })

            exercises.add(Exercise(
                name = "Burpee",
                nameKo = "버피",
                description = "고강도 전신 유산소 운동",
                instructions = "1. 스쿼트 자세로 내려갑니다.\n2. 손을 바닥에 짚고 다리를 뻗어 플랭크 자세로 갑니다.\n3. 푸쉬업을 합니다.\n4. 다리를 당기고 점프합니다.",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.ADVANCED,
                caloriesPerMinute = BigDecimal("12.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1599058917212-d750089bc07e?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["quadriceps"], mgMap["chest"], mgMap["abs"]))
                this.equipment.addAll(listOfNotNull(eqMap["bodyweight"]))
            })

            exercises.add(Exercise(
                name = "Mountain Climber",
                nameKo = "마운틴 클라이머",
                description = "코어와 심폐지구력을 단련하는 운동",
                instructions = "1. 플랭크 자세를 취합니다.\n2. 무릎을 번갈아 가슴 쪽으로 당깁니다.",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("10.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1434682881908-b43d0467b798?w=400"
            ).apply {
                this.muscleGroups.addAll(listOfNotNull(mgMap["abs"], mgMap["quadriceps"]))
                this.equipment.addAll(listOfNotNull(eqMap["exercise_mat"]))
            })

            // 유연성 운동
            exercises.add(Exercise(
                name = "Stretching",
                nameKo = "스트레칭",
                description = "유연성을 높이는 정적 스트레칭",
                instructions = "1. 각 근육을 15-30초간 늘립니다.\n2. 통증이 없는 범위에서 합니다.",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("3.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1566241142559-40e1dab266c6?w=400"
            ).apply {
                this.equipment.addAll(listOfNotNull(eqMap["exercise_mat"]))
            })

            exercises.add(Exercise(
                name = "Yoga",
                nameKo = "요가",
                description = "유연성과 균형감을 높이는 운동",
                instructions = "1. 다양한 요가 자세를 취합니다.\n2. 호흡과 함께 동작합니다.",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = Difficulty.INTERMEDIATE,
                caloriesPerMinute = BigDecimal("4.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400"
            ).apply {
                this.equipment.addAll(listOfNotNull(eqMap["exercise_mat"]))
            })

            exercises.add(Exercise(
                name = "Foam Rolling",
                nameKo = "폼롤링",
                description = "근막 이완을 통한 회복 운동",
                instructions = "1. 폼롤러 위에 근육을 올립니다.\n2. 천천히 굴려가며 마사지합니다.",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = Difficulty.BEGINNER,
                caloriesPerMinute = BigDecimal("2.0"),
                primaryImageUrl = "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=400"
            ).apply {
                this.equipment.addAll(listOfNotNull(eqMap["foam_roller"]))
            })

            exerciseRepository.saveAll(exercises)

            println("Seed data created: ${muscleGroupsList.size} muscle groups, ${equipmentList.size} equipment, ${exercises.size} exercises")
        }
    }
}
