"use client";

import { useQuery } from "@tanstack/react-query";
import { Activity, Calendar, Flame, TrendingUp } from "lucide-react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import { statsApi, checkinsApi, workoutsApi } from "@/lib/api";
import { useAuthStore } from "@/store/authStore";
import { format, startOfWeek, endOfWeek } from "date-fns";
import { ko } from "date-fns/locale";

export default function DashboardPage() {
  const { user } = useAuthStore();

  const { data: stats } = useQuery({
    queryKey: ["stats", "overview"],
    queryFn: () => statsApi.getOverviewStats(),
  });

  const { data: streak } = useQuery({
    queryKey: ["stats", "streak"],
    queryFn: () => statsApi.getStreak(),
  });

  const { data: todayCheckin } = useQuery({
    queryKey: ["checkins", "today"],
    queryFn: () => checkinsApi.getTodayCheckin(),
  });

  const today = new Date();
  const weekStart = format(startOfWeek(today, { weekStartsOn: 1 }), "yyyy-MM-dd");
  const weekEnd = format(endOfWeek(today, { weekStartsOn: 1 }), "yyyy-MM-dd");

  const { data: weekWorkouts } = useQuery({
    queryKey: ["workouts", "week", weekStart, weekEnd],
    queryFn: () => workoutsApi.getWorkoutsByDateRange(weekStart, weekEnd),
  });

  const weeklyGoal = 5;
  const weeklyProgress = stats?.thisWeekWorkouts || 0;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">
          안녕하세요, {user?.nickname}님!
        </h1>
        <p className="text-muted-foreground">
          {format(today, "yyyy년 M월 d일 EEEE", { locale: ko })}
        </p>
      </div>

      {/* Stats Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">연속 기록</CardTitle>
            <Flame className="h-4 w-4 text-orange-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{streak?.currentStreak || 0}일</div>
            <p className="text-xs text-muted-foreground">
              최장 기록: {streak?.longestStreak || 0}일
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">이번 주 운동</CardTitle>
            <Activity className="h-4 w-4 text-blue-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{weeklyProgress}회</div>
            <Progress value={(weeklyProgress / weeklyGoal) * 100} className="mt-2" />
            <p className="text-xs text-muted-foreground mt-1">
              주간 목표: {weeklyGoal}회
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">이번 달 운동</CardTitle>
            <Calendar className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats?.thisMonthWorkouts || 0}회</div>
            <p className="text-xs text-muted-foreground">
              총 운동일: {stats?.totalWorkoutDays || 0}일
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">주간 평균</CardTitle>
            <TrendingUp className="h-4 w-4 text-purple-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {stats?.averageWorkoutsPerWeek?.toFixed(1) || 0}회
            </div>
            <p className="text-xs text-muted-foreground">
              최근 4주 기준
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Today's Status */}
      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>오늘의 체크인</CardTitle>
            <CardDescription>
              {todayCheckin ? "오늘 체크인을 완료했습니다" : "아직 체크인하지 않았습니다"}
            </CardDescription>
          </CardHeader>
          <CardContent>
            {todayCheckin ? (
              <div className="space-y-2">
                <div className="flex justify-between">
                  <span className="text-muted-foreground">운동 완료</span>
                  <span className={todayCheckin.workoutCompleted ? "text-green-500" : "text-red-500"}>
                    {todayCheckin.workoutCompleted ? "완료" : "미완료"}
                  </span>
                </div>
                {todayCheckin.weightKg && (
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">체중</span>
                    <span>{todayCheckin.weightKg}kg</span>
                  </div>
                )}
                {todayCheckin.sleepHours && (
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">수면</span>
                    <span>{todayCheckin.sleepHours}시간</span>
                  </div>
                )}
              </div>
            ) : (
              <p className="text-sm text-muted-foreground">
                체크인 페이지에서 오늘의 상태를 기록하세요
              </p>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>이번 주 운동 기록</CardTitle>
            <CardDescription>
              {weekWorkouts?.length || 0}일 운동
            </CardDescription>
          </CardHeader>
          <CardContent>
            {weekWorkouts && weekWorkouts.length > 0 ? (
              <div className="space-y-2">
                {weekWorkouts.slice(0, 5).map((summary) => (
                  <div key={summary.date} className="flex justify-between text-sm">
                    <span className="text-muted-foreground">
                      {format(new Date(summary.date), "M/d (E)", { locale: ko })}
                    </span>
                    <span>{summary.totalExercises}개 운동</span>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-sm text-muted-foreground">
                이번 주 운동 기록이 없습니다
              </p>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
