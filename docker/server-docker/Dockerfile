# 베이스 이미지
FROM eclipse-temurin:17-jdk-alpine

# 작업 디렉토리
WORKDIR /app

# jar 파일 복사
COPY app.jar .

# 실행 (환경변수는 docker-compose에서 주입함)
ENTRYPOINT ["java", "-jar", "app.jar"]