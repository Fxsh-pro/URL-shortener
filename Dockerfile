# CACHE
#FROM docker.io/gradle:jdk17-alpine AS cache
FROM gradle:jdk17 AS cache
# FROM --platform=linux/arm64 docker.io/gradle:jdk17-alpine AS cache
# FROM --platform=linux/arm64 gradle:jdk17-alpine AS cache

ENV GRADLE_USER_HOME=/home/gradle/cache_home
ENV CODE_DIR=/home/gradle/java-code
RUN mkdir -p ${GRADLE_USER_HOME}
COPY ./build.gradle.kts ./settings.gradle.kts ${CODE_DIR}/
WORKDIR ${CODE_DIR}
RUN gradle clean build -i --stacktrace -x bootJar

# BUILDER
FROM gradle:jdk17  AS builder
# FROM --platform=linux/arm64 docker.io/gradle:jdk17-alpine AS builder
# FROM --platform=linux/arm64 gradle:jdk17-alpine AS cache

ENV SRC_DIR=/usr/src/java-code
ENV CACHE_HOME=/home/gradle/cache_home
COPY --from=cache ${CACHE_HOME} /home/gradle/.gradle
COPY ./settings.gradle.kts ./build.gradle.kts ${SRC_DIR}/
COPY ./src ${SRC_DIR}/src
COPY ./gradle ${SRC_DIR}/gradle
WORKDIR ${SRC_DIR}/
RUN gradle bootJar -i --stacktrace

# RUNNER
FROM --platform=linux/arm64 eclipse-temurin:17-jre AS runner
# FROM gradle:jdk17
# FROM --platform=linux/arm64 docker.io/openjdk:17-alpine
# FROM --platform=linux/arm64 gradle:jdk17-alpine AS cache

WORKDIR /usr/src/java-app
ENV SRC_DIR=/usr/src/java-code

COPY --from=builder ${SRC_DIR}/build/libs/*.jar ./app.jar

RUN apt-get update && apt-get install -y netcat-openbsd

CMD ["java", "-jar", "app.jar"]