FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /workspace/target/scorm-engine-0.1.0-SNAPSHOT.jar /app/scorm-engine.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/scorm-engine.jar"]
