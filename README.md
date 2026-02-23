# Fitness - 운동 관리 웹사이트

매일 운동을 체크하고 관리할 수 있는 웹 애플리케이션입니다.

## 기술 스택

### Backend
- Spring Boot 3.2 + Kotlin
- Spring Security + JWT 인증
- JPA + QueryDSL
- PostgreSQL
- OpenAPI/Swagger

### Frontend
- Next.js 14 (App Router)
- TypeScript
- TailwindCSS + shadcn/ui
- Zustand + React Query

## 시작하기

### 요구 사항
- JDK 17+
- Node.js 20+
- Docker & Docker Compose

### 로컬 개발 환경 설정

#### 1. 데이터베이스 실행

```bash
cd backend
docker compose -f docker-compose.local.yml up -d
```

#### 2. 백엔드 실행

```bash
cd backend
./gradlew bootRun
```

백엔드가 http://localhost:8080 에서 실행됩니다.

- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console (local 프로필): http://localhost:8080/h2-console

#### 3. 프론트엔드 실행

```bash
cd frontend
npm install
npm run dev
```

프론트엔드가 http://localhost:3000 에서 실행됩니다.

## 프로덕션 URL

- **웹사이트**: https://fitness.dosaem.com
- **API**: https://fitness.dosaem.com/api
- **Swagger UI**: https://fitness.dosaem.com/swagger-ui.html

## 프로젝트 구조

```
/Fitness
├── backend/
│   ├── src/main/kotlin/com/fitness/
│   │   ├── config/           # 설정 (Security, OpenAPI, DataSeeder)
│   │   ├── security/         # JWT 인증
│   │   ├── entity/           # JPA 엔티티
│   │   ├── repository/       # JPA Repository
│   │   ├── dto/              # Request/Response DTO
│   │   ├── service/          # 비즈니스 로직
│   │   └── controller/       # REST API 컨트롤러
│   ├── build.gradle.kts
│   ├── Dockerfile
│   └── docker-compose.local.yml
│
├── frontend/
│   ├── src/
│   │   ├── app/              # Next.js App Router 페이지
│   │   ├── components/       # React 컴포넌트
│   │   ├── lib/api/          # API 클라이언트
│   │   ├── store/            # Zustand 스토어
│   │   └── types/            # TypeScript 타입
│   ├── package.json
│   └── tailwind.config.ts
│
└── docker-compose.yml        # 프로덕션 Docker Compose
```

## 주요 기능

### 인증
- 이메일/비밀번호 회원가입 및 로그인
- JWT 기반 인증

### 운동 라이브러리
- 30+ 운동 목록 (근력, 유산소, 유연성)
- 카테고리/난이도/근육 그룹별 필터링
- 운동 검색

### 운동 기록
- 운동별 세트/반복/무게 기록 (근력)
- 운동 시간/거리 기록 (유산소)
- 날짜별 운동 히스토리

### 일일 체크인
- 운동 완료 여부
- 체중, 수면, 에너지/기분 기록
- 월별 캘린더 뷰

### 대시보드
- 연속 기록 (스트릭)
- 주간/월간 운동 통계
- 최근 운동 요약

## API 엔드포인트

### Auth
- `POST /api/auth/register` - 회원가입
- `POST /api/auth/login` - 로그인
- `POST /api/auth/refresh` - 토큰 갱신

### Exercises
- `GET /api/exercises` - 운동 목록
- `GET /api/exercises/{id}` - 운동 상세
- `GET /api/exercises/search` - 운동 검색

### Workouts
- `GET /api/workouts` - 운동 기록 목록
- `POST /api/workouts` - 운동 기록 추가
- `GET /api/workouts/date/{date}` - 특정 날짜 운동

### Checkins
- `GET /api/checkins/today` - 오늘 체크인
- `POST /api/checkins` - 체크인 생성/수정
- `GET /api/checkins/calendar/{year}/{month}` - 월별 캘린더

### Stats
- `GET /api/stats/overview` - 대시보드 통계
- `GET /api/stats/streak` - 연속 기록

## 배포 (Synology NAS)

1. `.env` 파일 생성:
```bash
cp .env.example .env
# DB_PASSWORD, JWT_SECRET 설정
```

2. Docker Compose로 실행:
```bash
docker compose up -d
```

3. Nginx 리버스 프록시 설정 후 `fitness.dosaem.com` 접속
