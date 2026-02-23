"use client";

import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { ChevronLeft, ChevronRight, Check, X } from "lucide-react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { checkinsApi } from "@/lib/api";
import { DailyCheckinRequest } from "@/types";
import { cn, getMoodEmoji, getEnergyEmoji } from "@/lib/utils";
import { format, addMonths, subMonths, startOfMonth, endOfMonth, eachDayOfInterval, isSameMonth, isToday, isSameDay } from "date-fns";
import { ko } from "date-fns/locale";

export default function CheckinsPage() {
  const queryClient = useQueryClient();
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [selectedDate, setSelectedDate] = useState<Date | null>(new Date());
  const [checkinForm, setCheckinForm] = useState<Partial<DailyCheckinRequest>>({
    workoutCompleted: false,
  });

  const year = currentMonth.getFullYear();
  const month = currentMonth.getMonth() + 1;

  const { data: calendar } = useQuery({
    queryKey: ["checkins", "calendar", year, month],
    queryFn: () => checkinsApi.getMonthlyCalendar(year, month),
  });

  const { data: selectedCheckin, refetch: refetchSelectedCheckin } = useQuery({
    queryKey: ["checkins", "date", selectedDate ? format(selectedDate, "yyyy-MM-dd") : null],
    queryFn: () =>
      selectedDate
        ? checkinsApi.getCheckinByDate(format(selectedDate, "yyyy-MM-dd"))
        : null,
    enabled: !!selectedDate,
  });

  const saveCheckinMutation = useMutation({
    mutationFn: (data: DailyCheckinRequest) => checkinsApi.createOrUpdateCheckin(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["checkins"] });
      refetchSelectedCheckin();
    },
  });

  const handleSaveCheckin = () => {
    if (!selectedDate) return;

    const data: DailyCheckinRequest = {
      checkinDate: format(selectedDate, "yyyy-MM-dd"),
      ...checkinForm,
    };

    saveCheckinMutation.mutate(data);
  };

  const monthStart = startOfMonth(currentMonth);
  const monthEnd = endOfMonth(currentMonth);
  const days = eachDayOfInterval({ start: monthStart, end: monthEnd });

  const checkinMap = new Map(
    calendar?.days.map((d) => [d.date, d])
  );

  const firstDayOfWeek = monthStart.getDay();
  const paddingDays = firstDayOfWeek === 0 ? 6 : firstDayOfWeek - 1;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">일일 체크인</h1>
        <p className="text-muted-foreground">
          매일의 상태를 기록하고 추적하세요
        </p>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        {/* Calendar */}
        <Card className="lg:col-span-2">
          <CardHeader>
            <div className="flex items-center justify-between">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => setCurrentMonth(subMonths(currentMonth, 1))}
              >
                <ChevronLeft className="h-4 w-4" />
              </Button>
              <CardTitle>
                {format(currentMonth, "yyyy년 M월", { locale: ko })}
              </CardTitle>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => setCurrentMonth(addMonths(currentMonth, 1))}
              >
                <ChevronRight className="h-4 w-4" />
              </Button>
            </div>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-7 gap-1 text-center mb-2">
              {["월", "화", "수", "목", "금", "토", "일"].map((day) => (
                <div key={day} className="text-sm font-medium text-muted-foreground py-2">
                  {day}
                </div>
              ))}
            </div>
            <div className="grid grid-cols-7 gap-1">
              {Array(paddingDays)
                .fill(null)
                .map((_, i) => (
                  <div key={`padding-${i}`} className="aspect-square" />
                ))}
              {days.map((day) => {
                const dateStr = format(day, "yyyy-MM-dd");
                const checkin = checkinMap.get(dateStr);
                const isSelected = selectedDate && isSameDay(day, selectedDate);

                return (
                  <button
                    key={dateStr}
                    onClick={() => setSelectedDate(day)}
                    className={cn(
                      "aspect-square flex flex-col items-center justify-center rounded-lg text-sm transition-colors",
                      isToday(day) && "ring-2 ring-primary",
                      isSelected && "bg-primary text-primary-foreground",
                      !isSelected && checkin?.workoutCompleted && "bg-green-100 text-green-700",
                      !isSelected && checkin?.hasCheckin && !checkin?.workoutCompleted && "bg-yellow-100 text-yellow-700",
                      !isSelected && !checkin?.hasCheckin && "hover:bg-muted"
                    )}
                  >
                    <span>{format(day, "d")}</span>
                    {checkin?.workoutCompleted && !isSelected && (
                      <Check className="h-3 w-3" />
                    )}
                  </button>
                );
              })}
            </div>
          </CardContent>
        </Card>

        {/* Checkin Form */}
        <Card>
          <CardHeader>
            <CardTitle>
              {selectedDate
                ? format(selectedDate, "M월 d일", { locale: ko })
                : "날짜를 선택하세요"}
            </CardTitle>
            <CardDescription>
              {selectedCheckin ? "체크인 수정" : "새 체크인"}
            </CardDescription>
          </CardHeader>
          <CardContent>
            {selectedDate && (
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <Label>운동 완료</Label>
                  <Button
                    variant={checkinForm.workoutCompleted || selectedCheckin?.workoutCompleted ? "default" : "outline"}
                    size="sm"
                    onClick={() =>
                      setCheckinForm((prev) => ({
                        ...prev,
                        workoutCompleted: !(prev.workoutCompleted ?? selectedCheckin?.workoutCompleted),
                      }))
                    }
                  >
                    {checkinForm.workoutCompleted ?? selectedCheckin?.workoutCompleted ? (
                      <Check className="h-4 w-4 mr-1" />
                    ) : (
                      <X className="h-4 w-4 mr-1" />
                    )}
                    {checkinForm.workoutCompleted ?? selectedCheckin?.workoutCompleted ? "완료" : "미완료"}
                  </Button>
                </div>

                <div className="space-y-2">
                  <Label>체중 (kg)</Label>
                  <Input
                    type="number"
                    step="0.1"
                    placeholder={selectedCheckin?.weightKg?.toString() || "70.5"}
                    value={checkinForm.weightKg || ""}
                    onChange={(e) =>
                      setCheckinForm((prev) => ({
                        ...prev,
                        weightKg: e.target.value ? parseFloat(e.target.value) : undefined,
                      }))
                    }
                  />
                </div>

                <div className="space-y-2">
                  <Label>수면 시간</Label>
                  <Input
                    type="number"
                    step="0.5"
                    placeholder={selectedCheckin?.sleepHours?.toString() || "7"}
                    value={checkinForm.sleepHours || ""}
                    onChange={(e) =>
                      setCheckinForm((prev) => ({
                        ...prev,
                        sleepHours: e.target.value ? parseFloat(e.target.value) : undefined,
                      }))
                    }
                  />
                </div>

                <div className="space-y-2">
                  <Label>에너지 레벨 (1-5)</Label>
                  <div className="flex gap-2">
                    {[1, 2, 3, 4, 5].map((level) => (
                      <Button
                        key={level}
                        variant={
                          (checkinForm.energyLevel ?? selectedCheckin?.energyLevel) === level
                            ? "default"
                            : "outline"
                        }
                        size="sm"
                        onClick={() =>
                          setCheckinForm((prev) => ({ ...prev, energyLevel: level }))
                        }
                      >
                        {level}
                      </Button>
                    ))}
                  </div>
                </div>

                <div className="space-y-2">
                  <Label>기분 (1-5)</Label>
                  <div className="flex gap-2">
                    {[1, 2, 3, 4, 5].map((level) => (
                      <Button
                        key={level}
                        variant={
                          (checkinForm.mood ?? selectedCheckin?.mood) === level
                            ? "default"
                            : "outline"
                        }
                        size="sm"
                        onClick={() =>
                          setCheckinForm((prev) => ({ ...prev, mood: level }))
                        }
                      >
                        {getMoodEmoji(level)}
                      </Button>
                    ))}
                  </div>
                </div>

                <div className="space-y-2">
                  <Label>메모</Label>
                  <Input
                    placeholder="오늘의 컨디션..."
                    value={checkinForm.notes || selectedCheckin?.notes || ""}
                    onChange={(e) =>
                      setCheckinForm((prev) => ({ ...prev, notes: e.target.value }))
                    }
                  />
                </div>

                <Button
                  className="w-full"
                  onClick={handleSaveCheckin}
                  disabled={saveCheckinMutation.isPending}
                >
                  {saveCheckinMutation.isPending ? "저장 중..." : "저장"}
                </Button>
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
