FROM eclipse-temurin:17-alpine

RUN apk add --no-cache netcat-openbsd

VOLUME /tmp
EXPOSE 8080

ARG JAR_FILE=target/EnergyControl-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

COPY entrypoint.sh /
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
CMD ["java","-jar","/app.jar"]