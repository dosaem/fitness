import { apiClient } from './client';
import { OverviewStats, Streak, WeightHistory, WorkoutFrequency } from '@/types';

export const statsApi = {
  getOverviewStats: async (): Promise<OverviewStats> => {
    return apiClient.get<OverviewStats>('/api/stats/overview');
  },

  getStreak: async (): Promise<Streak> => {
    return apiClient.get<Streak>('/api/stats/streak');
  },

  getWeightHistory: async (days = 30): Promise<WeightHistory[]> => {
    return apiClient.get<WeightHistory[]>(`/api/stats/weight-history?days=${days}`);
  },

  getWorkoutFrequency: async (days = 30): Promise<WorkoutFrequency[]> => {
    return apiClient.get<WorkoutFrequency[]>(`/api/stats/workout-frequency?days=${days}`);
  },
};
