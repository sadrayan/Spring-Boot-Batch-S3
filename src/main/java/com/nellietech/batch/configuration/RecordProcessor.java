package com.nellietech.batch.configuration;

import com.nellietech.batch.dao.entity.Customer;

import org.springframework.batch.item.ItemProcessor;

import java.sql.Date;
import java.sql.Timestamp;

public class RecordProcessor implements ItemProcessor<Customer, Customer>{

    @Override
    public Customer process(final Customer record) {
        final Customer processedRecord = new Customer();
        processedRecord.setFirst_name(record.getFirst_name());
        processedRecord.setLast_name(record.getLast_name());
        processedRecord.setEmail(record.getEmail());
        processedRecord.setPhone(record.getPhone());
        processedRecord.setCity(record.getCity());
        processedRecord.setZip(record.getZip());
        // adding the timestamp
        processedRecord.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return processedRecord;
    }
}
