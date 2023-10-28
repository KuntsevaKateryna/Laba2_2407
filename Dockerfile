

FROM openjdk:11

COPY target/demo-0.0.1-SNAPSHOT.jar DockerLab.jar
ENTRYPOINT ["java","-jar","/DockerLab.jar"]

