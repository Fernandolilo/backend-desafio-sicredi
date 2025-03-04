FROM openjdk:17-jdk-slim

# Instalar curl e outras dependências necessárias
RUN apt-get update && apt-get install -y curl

# Definir variável para o caminho do JAR
ARG JAR_FILE=target/sessao-0.0.1-SNAPSHOT.jar

# Copiar o arquivo JAR para o contêiner
COPY ${JAR_FILE} /app.jar

# Garantir permissões corretas
RUN chmod +x /app.jar

# Expor a porta 8000
EXPOSE 8000

# Executar o aplicativo
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/.urandom", "-jar", "/app.jar"]


