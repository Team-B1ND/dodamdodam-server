FROM eclipse-temurin:21-jre

ARG JAR_FILE=app.jar
COPY ${JAR_FILE} app.jar

RUN sed -i 's/TLSv1, TLSv1.1, //' $JAVA_HOME/conf/security/java.security

ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]