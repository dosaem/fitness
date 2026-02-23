import { apiClient } from './client';
import { DailyCheckin, DailyCheckinRequest, MonthlyCalendar } from '@/types';

export const checkinsApi = {
  getTodayCheckin: async (): Promise<DailyCheckin | null> => {
    return apiClient.get<DailyCheckin | null>('/api/checkins/today');
  },

  getCheckinByDate: async (date: string): Promise<DailyCheckin | null> => {
    return apiClient.get<DailyCheckin | null>(`/api/checkins/date/${date}`);
  },

  createOrUpdateCheckin: async (data: DailyCheckinRequest): Promise<DailyCheckin> => {
    return apiClient.post<DailyCheckin>('/api/checkins', data);
  },

  getMonthlyCalendar: async (year: number, month: number): Promise<MonthlyCalendar> => {
    return apiClient.get<MonthlyCalendar>(`/api/checkins/calendar/${year}/${month}`);
  },

  getRecentCheckins: async (limit = 7): Promise<DailyCheckin[]> => {
    return apiClient.get<DailyCheckin[]>(`/api/checkins/recent?limit=${limit}`);
  },
};
