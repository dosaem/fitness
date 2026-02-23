import { apiClient } from './client';
import { WorkoutLog, WorkoutLogRequest, DailyWorkoutSummary, PageResponse } from '@/types';

export const workoutsApi = {
  getWorkouts: async (page = 0, size = 20): Promise<PageResponse<WorkoutLog>> => {
    return apiClient.get<PageResponse<WorkoutLog>>(`/api/workouts?page=${page}&size=${size}`);
  },

  getWorkoutsByDate: async (date: string): Promise<WorkoutLog[]> => {
    return apiClient.get<WorkoutLog[]>(`/api/workouts/date/${date}`);
  },

  getWorkoutsByDateRange: async (startDate: string, endDate: string): Promise<DailyWorkoutSummary[]> => {
    return apiClient.get<DailyWorkoutSummary[]>(
      `/api/workouts/range?startDate=${startDate}&endDate=${endDate}`
    );
  },

  createWorkout: async (data: WorkoutLogRequest): Promise<WorkoutLog> => {
    return apiClient.post<WorkoutLog>('/api/workouts', data);
  },

  updateWorkout: async (id: number, data: WorkoutLogRequest): Promise<WorkoutLog> => {
    return apiClient.put<WorkoutLog>(`/api/workouts/${id}`, data);
  },

  deleteWorkout: async (id: number): Promise<void> => {
    return apiClient.delete<void>(`/api/workouts/${id}`);
  },
};
