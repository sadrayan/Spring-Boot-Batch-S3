package com.nellietech.batch.dao.repository;

import com.nellietech.batch.dao.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRecordRepository extends JpaRepository<Customer, Long> {

}
