FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
ARG VERSION
# Копируем Maven wrapper и родительский POM
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw

# Устанавливаем родительский POM
RUN ./mvnw clean install -N -DskipTests

# Устанавливаем common
COPY common common
RUN ./mvnw -f common/pom.xml clean install -DskipTests

# === Кэшируем зависимости user-service ===
COPY user-service/pom.xml user-service/
RUN mkdir -p user-service/src/main/java user-service/src/main/resources

# Скачиваем зависимости (не собираем)
RUN ./mvnw -f user-service/pom.xml dependency:resolve -DskipTests
RUN ./mvnw -f user-service/pom.xml dependency:resolve-plugins -DskipTests

# === Копируем исходники и собираем ===
COPY user-service/src/main/java user-service/src/main/java
COPY user-service/src/main/resources user-service/src/main/resources

ARG VERSION=1.0.0
RUN ./mvnw -f user-service/pom.xml clean package -DskipTests -Drevision=$VERSION



FROM eclipse-temurin:21-jre-alpine
ARG VERSION
COPY --from=build /app/user-service/target/user-service-${VERSION}.jar /app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]