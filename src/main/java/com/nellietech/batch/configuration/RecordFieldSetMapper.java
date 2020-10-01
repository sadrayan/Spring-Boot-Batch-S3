package com.nellietech.batch.configuration;

import com.nellietech.batch.dao.entity.Customer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class RecordFieldSetMapper implements FieldSetMapper<Customer> {

    @Override
    public Customer mapFieldSet(FieldSet fieldSet) {
        final Customer customerRecord = new Customer();
        customerRecord.setFirst_name(fieldSet.readString("first_name"));
        customerRecord.setLast_name(fieldSet.readString("last_name"));
        customerRecord.setEmail(fieldSet.readString("email"));
        customerRecord.setPhone(fieldSet.readString("phone"));
        customerRecord.setCity(fieldSet.readString("city"));
        customerRecord.setZip(fieldSet.readString("zip"));
        return customerRecord;
    }
}
