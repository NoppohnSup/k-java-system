FROM openjdk:8-jdk-alpine

WORKDIR /k-java-system
COPY . .
RUN ./gradlew clean build

CMD ./gradlew bootRun