package com.shashank.banking_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shashank.banking_service.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    List<Transaction> findByAccountId(Long accountId);
}
