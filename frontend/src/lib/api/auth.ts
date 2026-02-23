import { apiClient } from './client';
import { AuthResponse, LoginRequest, RegisterRequest } from '@/types';

export const authApi = {
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/api/auth/login', data);
    apiClient.setAccessToken(response.accessToken);
    apiClient.setRefreshToken(response.refreshToken);
    return response;
  },

  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/api/auth/register', data);
    apiClient.setAccessToken(response.accessToken);
    apiClient.setRefreshToken(response.refreshToken);
    return response;
  },

  logout: () => {
    apiClient.setAccessToken(null);
    apiClient.setRefreshToken(null);
  },
};
