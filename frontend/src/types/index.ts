// Auth types
export interface User {
  id: number;
  email: string;
  nickname: string;
  profileImage: string | null;
  timezone: string;
  locale: string;
  heightCm: number | null;
  weightKg: number | null;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  nickname: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}

export interface MessageResponse {
  message: string;
}

// Exercise types
export type ExerciseCategory = 'STRENGTH' | 'CARDIO' | 'FLEXIBILITY';
export type Difficulty = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';
export type BodyPart = 'UPPER_BODY' | 'CORE' | 'LOWER_BODY' | 'FULL_BODY';
export type EquipmentCategory = 'FREE_WEIGHT' | 'MACHINE' | 'BODYWEIGHT' | 'CARDIO' | 'ACCESSORY';

export interface MuscleGroup {
  id: number;
  name: string;
  nameKo: string;
  bodyPart: BodyPart;
}

export interface Equipment {
  id: number;
  name: string;
  nameKo: string;
  category: EquipmentCategory;
}

export interface Exercise {
  id: number;
  name: string;
  nameKo: string;
  description: string | null;
  instructions: string | null;
  category: ExerciseCategory;
  difficulty: Difficulty;
  primaryImageUrl: string | null;
  videoUrl: string | null;
  caloriesPerMinute: number | null;
  muscleGroups: MuscleGroup[];
  equipment: Equipment[];
}

export interface ExerciseListItem {
  id: number;
  name: string;
  nameKo: string;
  category: ExerciseCategory;
  difficulty: Difficulty;
  primaryImageUrl: string | null;
  muscleGroups: string[];
}

// Workout types
export interface WorkoutLog {
  id: number;
  exerciseId: number;
  exerciseName: string;
  exerciseNameKo: string;
  exerciseCategory: ExerciseCategory;
  workoutDate: string;
  sets: number | null;
  reps: number | null;
  weightKg: number | null;
  durationMinutes: number | null;
  distanceKm: number | null;
  notes: string | null;
  startedAt: string | null;
  completedAt: string | null;
  createdAt: string;
}

export interface WorkoutLogRequest {
  exerciseId: number;
  workoutDate: string;
  sets?: number;
  reps?: number;
  weightKg?: number;
  durationMinutes?: number;
  distanceKm?: number;
  notes?: string;
  startedAt?: string;
  completedAt?: string;
}

export interface DailyWorkoutSummary {
  date: string;
  workouts: WorkoutLog[];
  totalExercises: number;
  totalSets: number;
  totalDurationMinutes: number;
}

// Check-in types
export interface DailyCheckin {
  id: number;
  checkinDate: string;
  weightKg: number | null;
  bodyFatPercentage: number | null;
  sleepHours: number | null;
  energyLevel: number | null;
  mood: number | null;
  workoutCompleted: boolean;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface DailyCheckinRequest {
  checkinDate: string;
  weightKg?: number;
  bodyFatPercentage?: number;
  sleepHours?: number;
  energyLevel?: number;
  mood?: number;
  workoutCompleted?: boolean;
  notes?: string;
}

export interface CalendarDay {
  date: string;
  hasCheckin: boolean;
  workoutCompleted: boolean;
  weightKg: number | null;
}

export interface MonthlyCalendar {
  year: number;
  month: number;
  days: CalendarDay[];
}

// Stats types
export interface OverviewStats {
  totalWorkouts: number;
  totalWorkoutDays: number;
  currentStreak: number;
  longestStreak: number;
  thisWeekWorkouts: number;
  thisMonthWorkouts: number;
  averageWorkoutsPerWeek: number;
}

export interface Streak {
  currentStreak: number;
  longestStreak: number;
  lastWorkoutDate: string | null;
}

export interface WeightHistory {
  date: string;
  weightKg: number;
}

export interface WorkoutFrequency {
  date: string;
  count: number;
}

// API types
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
}
