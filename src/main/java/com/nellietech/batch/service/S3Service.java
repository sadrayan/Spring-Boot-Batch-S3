package com.nellietech.batch.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class S3Service {

    private final Logger logger = LoggerFactory.getLogger(S3Service.class);
    @Autowired
    AmazonS3 amazonS3Client;
    @Autowired
    private ResourceLoader resourceLoader;

//    @Value("${aws.s3.bucket}")
    private String amazonS3Bucket = "sadrayan-spring-batch";

    public Resource getFiles(final String file) {
        logger.info("File to be accessed :: " + String.format("s3://%s/%s", amazonS3Bucket, file));
        return resourceLoader.getResource(String.format("s3://%s/%s", amazonS3Bucket, file));
    }

    public byte[] getFile(final String file) {
        S3Object obj = amazonS3Client.getObject(amazonS3Bucket, file);
        S3ObjectInputStream stream = obj.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(stream);
            obj.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
