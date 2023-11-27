# Ниже представлена конфигурация, которая будет
# собирать приложение из папки target
FROM openjdk:17-jdk-slim
# Копируем все файлы, которые имеют расширение jar
COPY target/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
