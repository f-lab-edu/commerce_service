# 1. 사용할 자바 버전 기반의 이미지
FROM eclipse-temurin:17-jdk

# 2. 작업 디렉토리를 설정
WORKDIR /app

# 3. 스프링 부트 애플리케이션의 JAR 파일을 컨테이너로 복사
COPY build/libs/ecService-0.0.1-SNAPSHOT.jar app.jar

# 4. JAR 파일을 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
