![build passing](https://raw.githubusercontent.com/dwyl/repo-badges/master/highresPNGs/build-passing.png)
# 
Batch Processing Large Data Sets with Spring Boot and Spring Batch

https://dzone.com/articles/batch-processing-large-data-sets-with-spring-boot


ECS Repo
575711874019.dkr.ecr.us-east-1.amazonaws.com/spring-batch-s3

# authenticate with ECR 
aws ecr get-login-password --region us-east-1 | sudo docker login --username AWS --password-stdin 575711874019.dkr.ecr.us-east-1.amazonaws.com


sudo docker images # get the images ids
sudo docker tag acba92113cdf 575711874019.dkr.ecr.us-east-1.amazonaws.com/spring-batch-s3
sudo docker push 575711874019.dkr.ecr.us-east-1.amazonaws.com/spring-batch-s3