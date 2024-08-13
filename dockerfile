FROM openjdk:17
COPY dodam-application/dodam-rest-api/build/libs/dodam-rest-api-0.0.1-SNAPSHOT.jar app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java","-jar","/app.jar","-Duser.timezone=Asia/Seoul"]