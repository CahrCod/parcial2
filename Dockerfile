FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Uncomment the line in application.properties for Docker
RUN mkdir -p /app/src/main/resources
COPY src/main/resources/application.properties /app/src/main/resources/
RUN sed -i 's/#spring.datasource.url=jdbc:mysql:\/\/mysql:3306\/parcial2/spring.datasource.url=jdbc:mysql:\/\/mysql:3306\/parcial2/' /app/src/main/resources/application.properties
RUN sed -i 's/spring.datasource.url=jdbc:mysql:\/\/localhost:3306\/parcial2/#spring.datasource.url=jdbc:mysql:\/\/localhost:3306\/parcial2/' /app/src/main/resources/application.properties

EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]