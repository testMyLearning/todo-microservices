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

COPY user-service user-service
RUN ./mvnw -f user-service/pom.xml clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine
ARG VERSION
COPY --from=build /app/user-service/target/user-service-${VERSION}.jar /app.jar
COPY common-config /common-config/
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]