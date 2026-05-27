# Етап 1: Збірка (Build stage)
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
# Завантажуємо залежності (щоб прискорити наступні збірки)
RUN mvn dependency:go-offline
COPY src ./src
# Збираємо проєкт, пропускаючи тести (ми їх вже перевірили локально)
RUN mvn clean package -DskipTests

# Етап 2: Запуск (Run stage)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Копіюємо готовий jar файл з першого етапу
COPY --from=builder /app/target/*.jar app.jar
# Вказуємо порт, на якому працює Spring Boot
EXPOSE 8080
# Запускаємо програму
ENTRYPOINT ["java", "-jar", "app.jar"]