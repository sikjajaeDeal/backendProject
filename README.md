# 콩바구니(beanba) API SERVER

멋쟁이 사자처럼 2025 백엔드 단기 심화과정 부트캠프 5기(LIKE LION BESP5) 1차 실전 프로젝트 **콩바구니(구: 식자재 deal)**의 백엔드 저장소입니다.

> **Quick Links**
> - 📘 Notion: https://www.notion.so/2256e9004b00803b934fd6144e861e0a
> - 🧪 Swagger UI: `/swagger-ui/index.html`

---

## 프로젝트 개요

### 🎯 목적
안전하고 확장성 높은 **식자재 전문 웹 플랫폼**을 위한 RESTful API 제공

### 🧩 주요 기능
- 식자재 데이터 **CRUD** 및 **Elasticsearch** 기반 검색
- **WebSocket + STOMP + Redis** 실시간 채팅
- **JWT** 기반 인증/인가 + 소셜 로그인(Google, Kakao)
- **외부 API 연동(KAMIS)** 으로 실시간 싯가 데이터 제공
- 표준화된 **예외 처리** 및 **보안 강화**
- **관리자 페이지**를 통한 사용자/식자재/신고 데이터 관리

---

## 기술 스택

| 분류       | 기술                         |
|------------|------------------------------|
| 언어       | Java 17                      |
| 프레임워크 | Spring Boot 3.5.3            |
| ORM        | Spring Data JPA              |
| DB         | AWS RDS (MySQL)              |
| 인증       | Spring Security, JWT, OAuth2 |
| 배포       | GitHub Actions, Docker, AWS EC2 |
| 문서화     | Swagger, Notion              |
| 빌드툴     | Gradle                        |
| 기타       | Lombok                        |

---

## AWS 인프라 구성

콩바구니(beanba) 백엔드는 AWS 클라우드 상에서 다음과 같이 구성됩니다.

### 📦 주요 서비스
1. **EC2 (t3.medium)**
   - Spring Boot 애플리케이션을 호스팅하는 가상 서버
   - 배포 자동화(**Docker**, **GitHub Actions**) 및 보안 그룹 설정
   - **컨테이너 구성**
     - Nginx
     - Spring Boot 애플리케이션 **2개** (Blue & Green)
     - Redis
     - Elasticsearch
     - Kibana

2. **RDS**
   - MySQL 엔진 사용
   - 백업/장애 복구, 보안 그룹, 파라미터 그룹 관리

3. **S3**
   - 식자재 이미지 파일 저장소
  
### 구조도
![콩바구니_구조도.png](attachment:dd94a316-be0a-4320-b578-c675a970721a:콩바구니_구조도.png)
