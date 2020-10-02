# Batch Processing Large Data Sets with Spring Boot and Spring Batch
In this project I demonstrate how to process file from AWS S3 with Spring Boot Batch.

### ECS Repo
* 575711874019.dkr.ecr.us-east-1.amazonaws.com/spring-batch-s3

### authenticate with ECR 
* aws ecr get-login-password --region us-east-1 | sudo docker login --username AWS --password-stdin 575711874019.dkr.ecr.us-east-1.amazonaws.com

### build and push docker container to AWS ECR
* sudo docker build -t spring-boot-s3:1.0 --build-arg ACCESS_ARG={AWS_ACCESS} --build-arg SECRET_ARG={AWS_SECRET} .
* sudo docker run -d -p 8080:8080 -t spring-boot-s3:1.0

* sudo docker image tag spring-boot-s3:1.0 sadrayan/spring-boot-s3:1.0
* sudo docker image push sadrayan/spring-boot-s3:1.0

* sudo docker images # get the images ids
* sudo docker tag acba92113cdf 575711874019.dkr.ecr.us-east-1.amazonaws.com/spring-batch-s3
* sudo docker push 575711874019.dkr.ecr.us-east-1.amazonaws.com/spring-batch-s3