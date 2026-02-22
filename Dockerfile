# ==========================================
# Build stage
# ==========================================
FROM eclipse-temurin:11-jdk AS build

WORKDIR /build

COPY mvnw ./mvnw
COPY .mvn ./.mvn

COPY pom.xml ./pom.xml

RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean compile

RUN ./mvnw dependency:copy-dependencies -DoutputDirectory=target/dependency

# ==========================================
# Runtime stage (JRuby)
# ==========================================
FROM jruby:9.4-jdk11

WORKDIR /app

COPY --from=build /build/target/classes /app/classes
COPY --from=build /build/target/dependency /app/lib

ENV CLASSPATH=/app/classes:/app/lib/*

ENTRYPOINT ["jruby"]

# Default: interactive shell
CMD ["-S", "irb"]