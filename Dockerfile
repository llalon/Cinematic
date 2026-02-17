# ==========================================
# Build stage
# ==========================================
FROM eclipse-temurin:11-jdk-focal AS build

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
# Runtime stage
# ==========================================
FROM eclipse-temurin:11-jdk-focal

WORKDIR /app

COPY --from=build /build/target/classes /usr/share/java/
COPY --from=build /build/target/dependency /usr/share/java/

ENV CLASSPATH=/usr/share/java/*

RUN apt-get update && apt-get install -y --no-install-recommends \
    groovy \
    jruby \
    && rm -rf /var/lib/apt/lists/*

CMD ["/bin/bash"]