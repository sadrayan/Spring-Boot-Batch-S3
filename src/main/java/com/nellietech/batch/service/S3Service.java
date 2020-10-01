package com.nellietech.batch.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class S3Service {

    private final Logger logger = LoggerFactory.getLogger(S3Service.class);
    @Autowired
    AmazonS3 amazonS3Client;

    private final String amazonS3BucketName = "sadrayan-spring-batch";
    private final String fileName = "data.csv";

    public Resource getS3Resource(){
        logger.info("Fetching S3 object");
        S3Object s3Object = amazonS3Client.getObject(amazonS3BucketName, fileName);

        byte[] content = new byte[0];
        try {
            content = IOUtils.toByteArray(s3Object.getObjectContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayResource(content);
    }



}
