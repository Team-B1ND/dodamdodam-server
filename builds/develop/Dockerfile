FROM eclipse-temurin:21-jre

ARG JAR_FILE=app.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]