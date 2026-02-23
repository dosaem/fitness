"use client";

import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Trash2, Edit2 } from "lucide-react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { workoutsApi } from "@/lib/api";
import { getCategoryLabel, formatDate } from "@/lib/utils";
import { format, startOfMonth, endOfMonth, subMonths } from "date-fns";
import { ko } from "date-fns/locale";

export default function WorkoutsPage() {
  const queryClient = useQueryClient();
  const [selectedMonth, setSelectedMonth] = useState(new Date());

  const startDate = format(startOfMonth(selectedMonth), "yyyy-MM-dd");
  const endDate = format(endOfMonth(selectedMonth), "yyyy-MM-dd");

  const { data: workoutSummaries, isLoading } = useQuery({
    queryKey: ["workouts", "range", startDate, endDate],
    queryFn: () => workoutsApi.getWorkoutsByDateRange(startDate, endDate),
  });

  const deleteWorkoutMutation = useMutation({
    mutationFn: (id: number) => workoutsApi.deleteWorkout(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["workouts"] });
    },
  });

  const handleDeleteWorkout = (id: number) => {
    if (confirm("이 운동 기록을 삭제하시겠습니까?")) {
      deleteWorkoutMutation.mutate(id);
    }
  };

  const months = [
    { value: 0, label: format(new Date(), "M월", { locale: ko }) },
    { value: 1, label: format(subMonths(new Date(), 1), "M월", { locale: ko }) },
    { value: 2, label: format(subMonths(new Date(), 2), "M월", { locale: ko }) },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">운동 기록</h1>
        <p className="text-muted-foreground">
          나의 운동 기록을 확인하세요
        </p>
      </div>

      <Tabs defaultValue="0" className="w-full">
        <TabsList>
          {months.map((month) => (
            <TabsTrigger
              key={month.value}
              value={month.value.toString()}
              onClick={() => setSelectedMonth(subMonths(new Date(), month.value))}
            >
              {month.label}
            </TabsTrigger>
          ))}
        </TabsList>
      </Tabs>

      {isLoading ? (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
        </div>
      ) : workoutSummaries && workoutSummaries.length > 0 ? (
        <div className="space-y-4">
          {workoutSummaries.map((summary) => (
            <Card key={summary.date}>
              <CardHeader className="pb-2">
                <div className="flex items-center justify-between">
                  <CardTitle className="text-lg">
                    {format(new Date(summary.date), "M월 d일 (EEEE)", { locale: ko })}
                  </CardTitle>
                  <div className="text-sm text-muted-foreground">
                    {summary.totalExercises}개 운동 | {summary.totalSets}세트
                    {summary.totalDurationMinutes > 0 && ` | ${summary.totalDurationMinutes}분`}
                  </div>
                </div>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  {summary.workouts.map((workout) => (
                    <div
                      key={workout.id}
                      className="flex items-center justify-between p-3 bg-muted/50 rounded-lg"
                    >
                      <div className="flex items-center gap-3">
                        <Badge
                          variant={
                            workout.exerciseCategory === "STRENGTH"
                              ? "strength"
                              : workout.exerciseCategory === "CARDIO"
                              ? "cardio"
                              : "flexibility"
                          }
                        >
                          {getCategoryLabel(workout.exerciseCategory)}
                        </Badge>
                        <div>
                          <div className="font-medium">{workout.exerciseNameKo}</div>
                          <div className="text-sm text-muted-foreground">
                            {workout.sets && workout.reps && (
                              <span>
                                {workout.sets}세트 x {workout.reps}회
                                {workout.weightKg && ` @ ${workout.weightKg}kg`}
                              </span>
                            )}
                            {workout.durationMinutes && (
                              <span>{workout.durationMinutes}분</span>
                            )}
                            {workout.notes && (
                              <span className="ml-2 text-muted-foreground">
                                - {workout.notes}
                              </span>
                            )}
                          </div>
                        </div>
                      </div>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleDeleteWorkout(workout.id)}
                        disabled={deleteWorkoutMutation.isPending}
                      >
                        <Trash2 className="h-4 w-4 text-red-500" />
                      </Button>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      ) : (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <p className="text-muted-foreground mb-4">
              이 달에 기록된 운동이 없습니다
            </p>
            <Button onClick={() => window.location.href = "/exercises"}>
              운동 추가하기
            </Button>
          </CardContent>
        </Card>
      )}
    </div>
  );
}
