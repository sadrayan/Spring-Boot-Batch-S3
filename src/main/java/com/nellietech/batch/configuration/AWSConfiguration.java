package com.nellietech.batch.configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {
        @Value("${aws.access_key_id}")
        private String accessKeyId;
        @Value("${aws.secret_access_key}")
        private String secretAccessKey;
        @Value("${aws.s3.region}")
        private String region;

        @Bean
        public AWSCredentialsProvider credentialsProvider() {
            return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey));
        }

        @Bean
        public AmazonS3 getAmazonS3Client() {
            return AmazonS3ClientBuilder
                    .standard()
                    .withRegion(Regions.fromName(region))
                    .withCredentials(credentialsProvider())
                    .build();
        }
    }