package com.nellietech.batch.configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.nellietech.batch.dao.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;


@Configuration
@EnableScheduling
@EnableBatchProcessing
public class BatchConfiguration {

    private final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    AmazonS3 amazonS3Client;

    private final String amazonS3Bucket = "sadrayan-spring-batch";
    private final String file = "data.csv";

    @Bean
    public FlatFileItemReader<Customer> reader() {
//        Resource resource = resourceLoader.getResource(String.format("s3://%s/%s", amazonS3Bucket, file));
//        Resource resource = resourceLoader.getResource("s3://" + amazonS3Bucket + "/" + file);
        Resource resource = new ClassPathResource("data/data.csv");
//        UrlResource resource = null;

//        Resource resource = awsReader;
        try {
//            s3Service.getFile(file);
//              resource = new UrlResource("https://s3.us-east-1.amazonaws.com/sadrayan-spring-batch/data.csv");
//              resource = new FileUrlResource("s3://sadrayan-spring-batch/data.csv");
//            InputStream inputStream = resource.getInputStream();
//            amazonS3Client.getObject("sadrayan-spring-batch","data.csv");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }

        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .resource(resource)
                .strict(false) // that was the trick for aws s3
                .delimited()
                .names("first_name", "last_name", "email", "phone", "city", "zip")
                .lineMapper(lineMapper())
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {{
                    setTargetType(Customer.class);
                }})
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setQueueCapacity(25);
        return taskExecutor;
    }

    @Bean
    AsyncTaskExecutor asyncTaskExecutor() {
        SimpleAsyncTaskExecutor t = new SimpleAsyncTaskExecutor();
        t.setConcurrencyLimit(100);
        return t;
    }

    @Bean
    public LineMapper<Customer> lineMapper() {
        final DefaultLineMapper<Customer> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("first_name", "last_name", "email", "phone", "city", "zip");

        final RecordFieldSetMapper fieldSetMapper = new RecordFieldSetMapper();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    @Bean
    public RecordProcessor processor() {
        return new RecordProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Customer> writer(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO CUSTOMER (first_name, last_name, email, phone, city, zip, timestamp) VALUES (:first_name, :last_name, :email, :phone, :city, :zip, :timestamp)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importCustomerJob(NotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importCustomerJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Customer> writer) {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(10)
                .reader(reader())
                .writer(writer)
                .processor(processor())
                .taskExecutor(taskExecutor()) // for running in multi-thread
                .build();
    }
}
