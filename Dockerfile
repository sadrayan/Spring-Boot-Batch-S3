FROM openjdk:8-jdk-alpine

RUN apk --no-cache add curl
VOLUME /tmp

ARG REGION_ARG=us-east-1
ARG ACCESS_ARG
ARG SECRET_ARG

ENV aws.s3.region=$REGION_ARG
ENV AWS_ACCESS_KEY_ID=$ACCESS_ARG
ENV AWS_SECRET_ACCESS_KEY=$SECRET_ARG

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]