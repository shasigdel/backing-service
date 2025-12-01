package com.shashank.banking_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shashank.banking_service.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
}
