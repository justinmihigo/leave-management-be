
FROM eclipse-temurin:21-jre-alpine

RUN addgroup --system spring && adduser --system justin --ingroup spring

WORKDIR /home/justin/app

COPY target/leave_mgmt-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

RUN chown justin:spring app.jar

RUN chmod 755 app.jar

USER justin

ENTRYPOINT ["java", "-jar", "app.jar"]