# 🌱 콩바구니(beanba) API SERVER

멋쟁이 사자처럼 **2025 백엔드 단기 심화과정 부트캠프 5기 (LIKE LION BESP5)**  
1차 실전 프로젝트 **콩바구니(구: 식자재 deal)** 백엔드 저장소입니다.

---

## 🔗 Quick Links
- 📘 [Notion](https://www.notion.so/2256e9004b00803b934fd6144e861e0a)
- 🧪 Swagger UI: `/swagger-ui/index.html`

---

## 📌 프로젝트 개요

### 🎯 목적
안전하고 확장성 높은 **식자재 전문 웹 거래 플랫폼**을 위한 RESTful API 제공

### 🧩 주요 기능
- **식자재 판매 글 데이터 CRUD 및 검색**
  - Elasticsearch 조건 검색:
    - 제목/내용 (초성 및 검색어 포함 검색 지원)
    - 현재 위치(위도/경도 기준) ± 5km 내 데이터
    - 가격 범위
    - 카테고리
- **실시간 채팅:** WebSocket + STOMP + Redis
- **인증/인가:** JWT 기반 로그인 + 소셜 로그인(Google, Kakao)
- **신고 기능:** 구매자/판매자 신고하기
- **외부 API 연동:** KAMIS 실시간 식자재 싯가 데이터 제공
- **관리자 페이지:** 사용자·식자재·신고 데이터 관리
- **표준화된 예외 처리 및 보안 강화**

---

## 🛠 기술 스택

| 분류                 | 기술 |
|----------------------|------|
| **Framework / 언어** | Spring Boot 3.5.3 (Java 17) |
| **DB**              | AWS RDS (MySQL) |
| **Search / Messaging** | Elasticsearch, WebSocket + STOMP, Redis |
| **Infra & DevOps**   | AWS (EC2, RDS, S3), Docker, GitHub Actions |
| **Collaboration**    | GitHub, Git |
| **ORM**             | Spring Data JPA |
| **Auth**            | Spring Security, JWT, OAuth2 |
| **Docs**            | Swagger, Notion |
| **Build Tool**      | Gradle |
| **Etc**             | Lombok |

---

## ☁️ AWS 인프라 구성

콩바구니(beanba) 백엔드는 AWS 클라우드 상에서 다음과 같이 구성됩니다.

### 🗺️ 아키텍처
<img width="960" height="513" alt="콩바구니구조_250724" src="https://github.com/user-attachments/assets/1e5b4d1f-6cfa-49ef-97b1-4899d4e4ef24" />

### 📦 주요 서비스

#### 1. EC2 (t3.medium)
- Spring Boot 애플리케이션 호스팅
- **컨테이너 구성**
  - Nginx
  - Spring Boot 애플리케이션 2개 (Blue & Green)
  - Redis
  - Elasticsearch
  - Kibana
- **배포 자동화**
  - Docker + GitHub Actions
  - 보안 그룹 관리

#### 2. RDS
- MySQL 엔진
- 백업/장애 복구, 보안 그룹, 파라미터 그룹 관리

#### 3. S3
- 식자재 이미지 파일 저장소

### ✨ 특징
- AWS + Docker 기반 인프라
- Nginx Load Balancing + HTTPS (Let’s Encrypt)
- Blue-Green 무중단 배포 (GitHub Actions CI/CD)
- Redis 기반 WebSocket 확장성 확보

---
