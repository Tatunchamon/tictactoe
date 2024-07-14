# Stage 1: Build application
FROM gradle:8.9 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build

# Stage 2: Final image
FROM openjdk:21
RUN groupadd --gid 1000 tictactoe \
    && useradd --uid 1000 --gid tictactoe --shell /bin/bash --create-home tictactoe
USER tictactoe

WORKDIR /home/tictactoe/app
COPY --from=builder /app/application/build/libs/application.jar /home/tictactoe/app

EXPOSE 8080
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "application.jar"]