
# syntax=docker/dockerfile:1

# ==========================================
# 1. BUILD
# ==========================================
FROM eclipse-temurin:21-jdk-alpine-3.24 AS build

WORKDIR /app

# Copia os arquivos necessários do Gradle
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# Baixa as dependências
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon dependencies

# Copia o código-fonte
COPY src ./src

# Compila e gera o JAR
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon bootJar

# Separa o JAR gerado
RUN JAR=$(find build/libs -name '*.jar' ! -name '*-plain.jar' -print -quit) && \
    mkdir -p /out && \
    cp "$JAR" /out/app.jar


# ==========================================
# 2. RUNTIME
# ==========================================
FROM eclipse-temurin:21-jre-alpine-3.24 AS runtime

WORKDIR /app

# Cria usuário sem privilégios administrativos
RUN addgroup -S -g 10001 appgroup && \
    adduser -S -D -H -u 10001 -G appgroup appuser

# Copia apenas o JAR da etapa anterior
COPY --from=build --chown=10001:10001 /out/app.jar /app/app.jar

# Porta da aplicação
EXPOSE 8080

# Executa como usuário não-root
USER 10001:10001

# Inicialização da aplicação
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-Djava.io.tmpdir=/tmp", "-jar", "/app/app.jar"]
