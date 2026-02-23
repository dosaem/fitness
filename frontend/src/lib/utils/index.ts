import { type ClassValue, clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function formatDate(dateString: string): string {
  const date = new Date(dateString);
  return date.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
}

export function formatShortDate(dateString: string): string {
  const date = new Date(dateString);
  return date.toLocaleDateString('ko-KR', {
    month: 'short',
    day: 'numeric',
  });
}

export function formatTime(dateString: string): string {
  const date = new Date(dateString);
  return date.toLocaleTimeString('ko-KR', {
    hour: '2-digit',
    minute: '2-digit',
  });
}

export function getCategoryLabel(category: string): string {
  const labels: Record<string, string> = {
    STRENGTH: '근력',
    CARDIO: '유산소',
    FLEXIBILITY: '유연성',
  };
  return labels[category] || category;
}

export function getDifficultyLabel(difficulty: string): string {
  const labels: Record<string, string> = {
    BEGINNER: '초급',
    INTERMEDIATE: '중급',
    ADVANCED: '고급',
  };
  return labels[difficulty] || difficulty;
}

export function getBodyPartLabel(bodyPart: string): string {
  const labels: Record<string, string> = {
    UPPER_BODY: '상체',
    CORE: '코어',
    LOWER_BODY: '하체',
    FULL_BODY: '전신',
  };
  return labels[bodyPart] || bodyPart;
}

export function getMoodEmoji(mood: number): string {
  const emojis = ['', '😢', '😕', '😐', '🙂', '😄'];
  return emojis[mood] || '';
}

export function getEnergyEmoji(energy: number): string {
  const emojis = ['', '🔋', '🔋🔋', '🔋🔋🔋', '🔋🔋🔋🔋', '⚡'];
  return emojis[energy] || '';
}
