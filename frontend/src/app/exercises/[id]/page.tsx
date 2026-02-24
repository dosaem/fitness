"use client";

import { useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import Image from "next/image";
import { ArrowLeft, Plus, Dumbbell, Clock, Flame } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { exercisesApi, workoutsApi } from "@/lib/api";
import { getCategoryLabel, getDifficultyLabel, getBodyPartLabel } from "@/lib/utils";
import { format } from "date-fns";

export default function ExerciseDetailPage() {
  const params = useParams();
  const router = useRouter();
  const queryClient = useQueryClient();
  const exerciseId = Number(params.id);

  const [showAddForm, setShowAddForm] = useState(false);
  const [workoutData, setWorkoutData] = useState({
    sets: "",
    reps: "",
    weightKg: "",
    durationMinutes: "",
    notes: "",
  });

  const { data: exercise, isLoading } = useQuery({
    queryKey: ["exercise", exerciseId],
    queryFn: () => exercisesApi.getExercise(exerciseId),
  });

  const addWorkoutMutation = useMutation({
    mutationFn: (data: any) => workoutsApi.createWorkout(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["workouts"] });
      setShowAddForm(false);
      setWorkoutData({ sets: "", reps: "", weightKg: "", durationMinutes: "", notes: "" });
    },
  });

  const handleAddWorkout = () => {
    const data: any = {
      exerciseId,
      workoutDate: format(new Date(), "yyyy-MM-dd"),
    };

    if (exercise?.category === "STRENGTH") {
      if (workoutData.sets) data.sets = parseInt(workoutData.sets);
      if (workoutData.reps) data.reps = parseInt(workoutData.reps);
      if (workoutData.weightKg) data.weightKg = parseFloat(workoutData.weightKg);
    } else if (exercise?.category === "CARDIO") {
      if (workoutData.durationMinutes) data.durationMinutes = parseInt(workoutData.durationMinutes);
    }

    if (workoutData.notes) data.notes = workoutData.notes;

    addWorkoutMutation.mutate(data);
  };

  if (isLoading) {
    return (
      <div className="flex justify-center py-12">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  if (!exercise) {
    return (
      <div className="text-center py-12">
        <p className="text-muted-foreground">운동을 찾을 수 없습니다</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <Button variant="ghost" onClick={() => router.back()}>
        <ArrowLeft className="h-4 w-4 mr-2" />
        뒤로 가기
      </Button>

      <div className="grid gap-6 lg:grid-cols-3">
        <div className="lg:col-span-2 space-y-6">
          <Card className="overflow-hidden">
            {exercise.primaryImageUrl && (
              <div className="relative h-64 md:h-80 bg-muted">
                <Image
                  src={exercise.primaryImageUrl}
                  alt={exercise.nameKo}
                  fill
                  className="object-cover"
                  sizes="(max-width: 1024px) 100vw, 66vw"
                  priority
                />
              </div>
            )}
            <CardHeader>
              <div className="flex items-start justify-between">
                <div>
                  <CardTitle className="text-2xl">{exercise.nameKo}</CardTitle>
                  <CardDescription className="text-lg">{exercise.name}</CardDescription>
                </div>
                <Badge
                  variant={
                    exercise.category === "STRENGTH"
                      ? "strength"
                      : exercise.category === "CARDIO"
                      ? "cardio"
                      : "flexibility"
                  }
                  className="text-sm"
                >
                  {getCategoryLabel(exercise.category)}
                </Badge>
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex flex-wrap gap-2">
                <Badge variant="outline">{getDifficultyLabel(exercise.difficulty)}</Badge>
                {exercise.muscleGroups.map((mg) => (
                  <Badge key={mg.id} variant="secondary">
                    {mg.nameKo}
                  </Badge>
                ))}
              </div>

              {exercise.description && (
                <div>
                  <h3 className="font-semibold mb-2">설명</h3>
                  <p className="text-muted-foreground">{exercise.description}</p>
                </div>
              )}

              {exercise.instructions && (
                <div>
                  <h3 className="font-semibold mb-2">운동 방법</h3>
                  <p className="text-muted-foreground whitespace-pre-line">
                    {exercise.instructions}
                  </p>
                </div>
              )}

              <div className="flex gap-4 pt-4">
                {exercise.caloriesPerMinute && (
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <Flame className="h-4 w-4" />
                    <span>{exercise.caloriesPerMinute} kcal/분</span>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Dumbbell className="h-5 w-5" />
                운동 기록 추가
              </CardTitle>
            </CardHeader>
            <CardContent>
              {!showAddForm ? (
                <Button className="w-full" onClick={() => setShowAddForm(true)}>
                  <Plus className="h-4 w-4 mr-2" />
                  오늘 운동 기록하기
                </Button>
              ) : (
                <div className="space-y-4">
                  {exercise.category === "STRENGTH" && (
                    <>
                      <div className="grid grid-cols-2 gap-4">
                        <div className="space-y-2">
                          <Label>세트</Label>
                          <Input
                            type="number"
                            placeholder="3"
                            value={workoutData.sets}
                            onChange={(e) =>
                              setWorkoutData((prev) => ({ ...prev, sets: e.target.value }))
                            }
                          />
                        </div>
                        <div className="space-y-2">
                          <Label>반복</Label>
                          <Input
                            type="number"
                            placeholder="10"
                            value={workoutData.reps}
                            onChange={(e) =>
                              setWorkoutData((prev) => ({ ...prev, reps: e.target.value }))
                            }
                          />
                        </div>
                      </div>
                      <div className="space-y-2">
                        <Label>무게 (kg)</Label>
                        <Input
                          type="number"
                          step="0.5"
                          placeholder="20"
                          value={workoutData.weightKg}
                          onChange={(e) =>
                            setWorkoutData((prev) => ({ ...prev, weightKg: e.target.value }))
                          }
                        />
                      </div>
                    </>
                  )}

                  {(exercise.category === "CARDIO" || exercise.category === "FLEXIBILITY") && (
                    <div className="space-y-2">
                      <Label>운동 시간 (분)</Label>
                      <Input
                        type="number"
                        placeholder="30"
                        value={workoutData.durationMinutes}
                        onChange={(e) =>
                          setWorkoutData((prev) => ({ ...prev, durationMinutes: e.target.value }))
                        }
                      />
                    </div>
                  )}

                  <div className="space-y-2">
                    <Label>메모</Label>
                    <Input
                      placeholder="오늘 컨디션이 좋았다"
                      value={workoutData.notes}
                      onChange={(e) =>
                        setWorkoutData((prev) => ({ ...prev, notes: e.target.value }))
                      }
                    />
                  </div>

                  <div className="flex gap-2">
                    <Button
                      variant="outline"
                      className="flex-1"
                      onClick={() => setShowAddForm(false)}
                    >
                      취소
                    </Button>
                    <Button
                      className="flex-1"
                      onClick={handleAddWorkout}
                      disabled={addWorkoutMutation.isPending}
                    >
                      {addWorkoutMutation.isPending ? "저장 중..." : "저장"}
                    </Button>
                  </div>
                </div>
              )}
            </CardContent>
          </Card>

          {exercise.equipment.length > 0 && (
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">필요한 장비</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="flex flex-wrap gap-2">
                  {exercise.equipment.map((eq) => (
                    <Badge key={eq.id} variant="outline">
                      {eq.nameKo}
                    </Badge>
                  ))}
                </div>
              </CardContent>
            </Card>
          )}
        </div>
      </div>
    </div>
  );
}
