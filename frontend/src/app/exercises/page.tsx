"use client";

import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import Link from "next/link";
import Image from "next/image";
import { Search, Dumbbell } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { exercisesApi, ExerciseSearchParams } from "@/lib/api";
import { ExerciseCategory, Difficulty } from "@/types";
import { getCategoryLabel, getDifficultyLabel } from "@/lib/utils";

export default function ExercisesPage() {
  const [searchParams, setSearchParams] = useState<ExerciseSearchParams>({
    page: 0,
    size: 20,
  });
  const [searchKeyword, setSearchKeyword] = useState("");

  const { data: exercisesData, isLoading } = useQuery({
    queryKey: ["exercises", searchParams],
    queryFn: () => exercisesApi.getExercises(searchParams),
  });

  const { data: muscleGroups } = useQuery({
    queryKey: ["muscleGroups"],
    queryFn: () => exercisesApi.getMuscleGroups(),
  });

  const handleSearch = () => {
    setSearchParams((prev) => ({ ...prev, keyword: searchKeyword, page: 0 }));
  };

  const handleFilterChange = (key: keyof ExerciseSearchParams, value: string | undefined) => {
    setSearchParams((prev) => ({
      ...prev,
      [key]: value === "all" ? undefined : value,
      page: 0,
    }));
  };

  const getCategoryBadgeVariant = (category: ExerciseCategory) => {
    switch (category) {
      case "STRENGTH":
        return "strength" as const;
      case "CARDIO":
        return "cardio" as const;
      case "FLEXIBILITY":
        return "flexibility" as const;
      default:
        return "default" as const;
    }
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">운동 라이브러리</h1>
        <p className="text-muted-foreground">
          다양한 운동을 찾아보고 기록을 시작하세요
        </p>
      </div>

      {/* Search and Filters */}
      <div className="flex flex-col gap-4 md:flex-row md:items-center">
        <div className="flex flex-1 gap-2">
          <Input
            placeholder="운동 검색..."
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && handleSearch()}
            className="max-w-sm"
          />
          <Button variant="outline" onClick={handleSearch}>
            <Search className="h-4 w-4" />
          </Button>
        </div>

        <div className="flex gap-2">
          <Select
            value={searchParams.category || "all"}
            onValueChange={(value) => handleFilterChange("category", value as ExerciseCategory)}
          >
            <SelectTrigger className="w-[130px]">
              <SelectValue placeholder="카테고리" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">전체</SelectItem>
              <SelectItem value="STRENGTH">근력</SelectItem>
              <SelectItem value="CARDIO">유산소</SelectItem>
              <SelectItem value="FLEXIBILITY">유연성</SelectItem>
            </SelectContent>
          </Select>

          <Select
            value={searchParams.difficulty || "all"}
            onValueChange={(value) => handleFilterChange("difficulty", value as Difficulty)}
          >
            <SelectTrigger className="w-[120px]">
              <SelectValue placeholder="난이도" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">전체</SelectItem>
              <SelectItem value="BEGINNER">초급</SelectItem>
              <SelectItem value="INTERMEDIATE">중급</SelectItem>
              <SelectItem value="ADVANCED">고급</SelectItem>
            </SelectContent>
          </Select>

          <Select
            value={searchParams.muscleGroupId?.toString() || "all"}
            onValueChange={(value) =>
              handleFilterChange("muscleGroupId", value === "all" ? undefined : Number(value) as any)
            }
          >
            <SelectTrigger className="w-[140px]">
              <SelectValue placeholder="근육 그룹" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">전체</SelectItem>
              {muscleGroups?.map((mg) => (
                <SelectItem key={mg.id} value={mg.id.toString()}>
                  {mg.nameKo}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>

      {/* Exercise List */}
      {isLoading ? (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
        </div>
      ) : (
        <>
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            {exercisesData?.content.map((exercise) => (
              <Link key={exercise.id} href={`/exercises/${exercise.id}`}>
                <Card className="hover:shadow-md transition-shadow cursor-pointer h-full overflow-hidden">
                  <div className="relative h-48 bg-muted">
                    {exercise.primaryImageUrl ? (
                      <Image
                        src={exercise.primaryImageUrl}
                        alt={exercise.nameKo}
                        fill
                        className="object-cover"
                        sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
                      />
                    ) : (
                      <div className="flex items-center justify-center h-full">
                        <Dumbbell className="h-12 w-12 text-muted-foreground/50" />
                      </div>
                    )}
                    <div className="absolute top-2 right-2">
                      <Badge variant={getCategoryBadgeVariant(exercise.category)}>
                        {getCategoryLabel(exercise.category)}
                      </Badge>
                    </div>
                  </div>
                  <CardHeader className="pb-2">
                    <CardTitle className="text-lg">{exercise.nameKo}</CardTitle>
                    <CardDescription>{exercise.name}</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="flex flex-wrap gap-1">
                      <Badge variant="outline" className="text-xs">
                        {getDifficultyLabel(exercise.difficulty)}
                      </Badge>
                      {exercise.muscleGroups.slice(0, 3).map((mg, idx) => (
                        <Badge key={idx} variant="secondary" className="text-xs">
                          {mg}
                        </Badge>
                      ))}
                    </div>
                  </CardContent>
                </Card>
              </Link>
            ))}
          </div>

          {/* Pagination */}
          {exercisesData && exercisesData.totalPages > 1 && (
            <div className="flex justify-center gap-2">
              <Button
                variant="outline"
                disabled={searchParams.page === 0}
                onClick={() =>
                  setSearchParams((prev) => ({ ...prev, page: (prev.page || 0) - 1 }))
                }
              >
                이전
              </Button>
              <span className="flex items-center px-4">
                {(searchParams.page || 0) + 1} / {exercisesData.totalPages}
              </span>
              <Button
                variant="outline"
                disabled={(searchParams.page || 0) >= exercisesData.totalPages - 1}
                onClick={() =>
                  setSearchParams((prev) => ({ ...prev, page: (prev.page || 0) + 1 }))
                }
              >
                다음
              </Button>
            </div>
          )}

          {exercisesData?.content.length === 0 && (
            <div className="text-center py-12 text-muted-foreground">
              검색 결과가 없습니다
            </div>
          )}
        </>
      )}
    </div>
  );
}
