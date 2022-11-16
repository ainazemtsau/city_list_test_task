FROM eclipse-temurin:17-jdk-alpine as build
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --chown=gradle:gradle build.gradle settings.gradle gradlew $APP_HOME
COPY --chown=gradle:gradle gradle $APP_HOME/gradle
COPY --chown=gradle:gradle src $APP_HOME/src
RUN ./gradlew build || return 0
COPY . .
RUN ./gradlew --no-daemon build

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.citylist.CityListApplication"]

ARG BUILD_HOME=/gradle-docker-example
