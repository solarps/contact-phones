FROM openjdk:17-oracle
MAINTAINER andrii_yevstratiev
ARG jar_file=*SNAPSHOT.jar
COPY build/libs/*$jar_file application.jar
ENTRYPOINT ["java", "-jar","application.jar"]
EXPOSE 8080