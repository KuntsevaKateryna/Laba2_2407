
#FROM openjdk:11
FROM adoptopenjdk:11-jre-openj9

COPY target/demo-0.0.1-SNAPSHOT.jar DockerLab.jar
ENTRYPOINT ["java","-jar","/DockerLab.jar"]

