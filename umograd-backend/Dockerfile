FROM maven:3.9.6-eclipse-temurin-21 AS base-builder
WORKDIR /app
COPY . .

FROM base-builder AS content-builder
RUN cd content-service && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS content-app
WORKDIR /app
COPY --from=content-builder /app/content-service/target/content-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM base-builder AS analytic-builder
RUN cd analytic-service && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS analytic-app
WORKDIR /app
COPY --from=analytic-builder /app/analytic-service/target/analytic-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM base-builder AS umograd-builder
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS umograd-app
WORKDIR /app
COPY --from=umograd-builder /app/target/main-application-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
