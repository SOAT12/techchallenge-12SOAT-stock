# Estágio 1: build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copia arquivos de dependências
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download de dependências (cache em camada separada)
RUN ./mvnw dependency:go-offline -B

# Copia código fonte e compila
COPY src src
RUN ./mvnw package -DskipTests -B

# Estágio 2: runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Usuário não-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o JAR gerado no estágio de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
