# Build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
# copiar dependencias
COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY pom.xml .
RUN chmod +x mvnw
# descargar dependencias
RUN ./mvnw dependency:go-offline -B
# copiar código (app como tal)
COPY src ./src
# build
RUN ./mvnw clean package -DskipTests
# Runtime ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# tomar el JAR generado en la build
COPY --from=build /app/target/*.jar app.jar
# default port
EXPOSE 8080
# variables de entorno
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms256m -Xmx512m"
# comando de ejecución
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]