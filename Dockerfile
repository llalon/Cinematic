
# Build stage
FROM eclipse-temurin:11-jdk-focal AS build

WORKDIR /build

COPY mvnw ./mvnw
COPY .mvn ./.mvn

COPY pom.xml ./pom.xml

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean compile

# Runtime stage
FROM eclipse-temurin:11-jdk-focal

RUN apt-get update && apt-get install -y --no-install-recommends jruby groovy && rm -rf /var/lib/apt/lists/*

COPY --from=build /build/target/classes /usr/share/java/

ENV CLASSPATH=/usr/share/java/

# Default to bash shell
CMD ["bash"]