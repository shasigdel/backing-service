package com.shashank.banking_service.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.shashank.banking_service.model.Account;
import com.shashank.banking_service.model.Transaction;
import com.shashank.banking_service.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    @Autowired
    private AccountService accountService;

    // Get All Accounts
    @GetMapping
    public List<Account> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    // Get Account by account Number
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber){
        return accountService.getAccountByNumber(accountNumber)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Create new account
    @PostMapping
    public Account createAccount(@RequestBody Account account){
        return accountService.createAccount(account);
    }

    // Deposit
    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<Transaction> deposit(@PathVariable String accountNumber,
                                                @RequestParam BigDecimal amount) {
            try {
                Transaction transaction = accountService.deposit(accountNumber, amount);
                return ResponseEntity.ok(transaction);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }

    // Withdraw
    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<Transaction> withdraw(@PathVariable String accountNumber, @RequestParam BigDecimal amount){
        try {
            Transaction transaction = accountService.withdraw(accountNumber, amount);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestParam String fromAccount, @RequestParam String toAccount, @RequestParam BigDecimal amount){
        try {
            Transaction transaction = accountService.transfer(fromAccount, toAccount, amount);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get Transaction for an account
    @GetMapping("/{accountId}/transactions")
    public List<Transaction> geTransactions(@PathVariable Long accountId){
        return accountService.getTransactions(accountId);
    }

}
