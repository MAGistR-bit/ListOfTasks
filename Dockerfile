# Ниже представлена конфигурация
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /
COPY /src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package

FROM openjdk:17-jdk-slim
# Указываем рабочую директорию
WORKDIR /
COPY /src /src
# Копируем все файлы, которые имеют расширение jar (из build)
COPY --from=build /target/*.jar application.jar
# Указываем порт для работы
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
