FROM gradle:jdk17-alpine AS TEMP_BUILD_IMAGE
ENV APP_HOME=/workspace/app
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle ./src $APP_HOME/src
USER root
RUN chown -R gradle $APP_HOME/src
RUN gradle clean build -x test

FROM eclipse-temurin:17-jdk-alpine
ENV APP_HOME=/workspace/app
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/city-list-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

