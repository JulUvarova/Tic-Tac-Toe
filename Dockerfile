# Используем образ с JDK 21
FROM eclipse-temurin:21-jdk

# Рабочая директория в контейнере
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY build/libs/*.jar app.jar

# Порт, который будет использоваться
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
