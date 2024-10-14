# Usa una imagen de Java como base
FROM maven:3.9.0-amazoncorretto-17

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR generado por Maven
COPY src /app/src/
COPY pom.xml /app/pom.xml

RUN mvn -f /app/pom.xml clean package
#
# Package stage
#
FROM amazoncorretto:17

# Definir argumentos de construcción para las credenciales
ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD
ARG MAIL_USERNAME
ARG MAIL_PASSWORD

# Convertir los argumentos en variables de entorno en el contenedor final
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV MAIL_USERNAME=${MAIL_USERNAME}
ENV MAIL_PASSWORD=${MAIL_PASSWORD}

COPY --from=0 /app/target/notifications-0.0.1-SNAPSHOT.jar /usr/local/lib/notifications-0.0.1-SNAPSHOT.jar
EXPOSE 8093
ENTRYPOINT ["java", "-jar", "/usr/local/lib/notifications-0.0.1-SNAPSHOT.jar"]