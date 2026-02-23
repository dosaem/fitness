import { apiClient } from './client';
import {
  Exercise,
  ExerciseListItem,
  ExerciseCategory,
  Difficulty,
  MuscleGroup,
  Equipment,
  PageResponse
} from '@/types';

export interface ExerciseSearchParams {
  keyword?: string;
  category?: ExerciseCategory;
  difficulty?: Difficulty;
  muscleGroupId?: number;
  page?: number;
  size?: number;
}

export const exercisesApi = {
  getExercises: async (params: ExerciseSearchParams = {}): Promise<PageResponse<ExerciseListItem>> => {
    const searchParams = new URLSearchParams();

    if (params.keyword) searchParams.set('keyword', params.keyword);
    if (params.category) searchParams.set('category', params.category);
    if (params.difficulty) searchParams.set('difficulty', params.difficulty);
    if (params.muscleGroupId) searchParams.set('muscleGroupId', params.muscleGroupId.toString());
    if (params.page !== undefined) searchParams.set('page', params.page.toString());
    if (params.size !== undefined) searchParams.set('size', params.size.toString());

    const query = searchParams.toString();
    return apiClient.get<PageResponse<ExerciseListItem>>(`/api/exercises${query ? `?${query}` : ''}`);
  },

  getExercise: async (id: number): Promise<Exercise> => {
    return apiClient.get<Exercise>(`/api/exercises/${id}`);
  },

  searchExercises: async (keyword: string, page = 0, size = 20): Promise<PageResponse<ExerciseListItem>> => {
    return apiClient.get<PageResponse<ExerciseListItem>>(
      `/api/exercises/search?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`
    );
  },

  getMuscleGroups: async (): Promise<MuscleGroup[]> => {
    return apiClient.get<MuscleGroup[]>('/api/exercises/muscle-groups');
  },

  getEquipment: async (): Promise<Equipment[]> => {
    return apiClient.get<Equipment[]>('/api/exercises/equipment');
  },
};
