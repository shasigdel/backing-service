package com.shashank.banking_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shashank.banking_service.logger.TransactionLogger;
import com.shashank.banking_service.model.Account;
import com.shashank.banking_service.model.Transaction;
import com.shashank.banking_service.repository.AccountRepository;
import com.shashank.banking_service.repository.TransactionRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // @Autowired
    // private TransactionProducer transactionProducer;

    @Autowired
    private TransactionLogger transactionLogger;

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountByNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Transactional
    public Account createAccount(Account account){
        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount) throws Exception {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new Exception("Account Not Found"));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction(account, amount, "DEPOSIT", LocalDateTime.now());
        transactionRepository.save(transaction);

        // String transactionMessage = "Account: " + account.getAccountNumber() +
        //                     ", Type: DEPOSIT" +
        //                     ", Amount: " + amount;
        // transactionProducer.sendTransaction(transactionMessage);
        transactionLogger.onTransaction(transaction);
        return transaction;
    }

    @Transactional
    public Transaction withdraw(String accountNumber, BigDecimal amount) throws Exception {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new Exception("Account Not Found"));

        if (account.getBalance().compareTo(amount) < 0){
            throw new Exception("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction(account, amount, "WITHDRAW", LocalDateTime.now());
        transactionRepository.save(transaction);

        // String withdrawMessage = "Account: " + account.getAccountNumber() +
        //                  ", Type: WITHDRAW" +
        //                  ", Amount: " + amount;
        // transactionProducer.sendTransaction(withdrawMessage);
        transactionLogger.onTransaction(transaction);

        return transaction;
    }

    @Transactional
    public Transaction transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) throws Exception {

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new Exception("Cannot transfer to the same account");
        }

        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new Exception("Sender account not found"));

        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new Exception("Receiver account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0){
            throw new Exception("Insufficient Balance");
        }

        // update balances
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // sender transaction
        Transaction debitTx = new Transaction(
                fromAccount, amount, "TRANSFER_OUT", LocalDateTime.now());

        // receiver transaction
        Transaction creditTx = new Transaction(
                toAccount, amount, "TRANSFER_IN", LocalDateTime.now());

        transactionRepository.save(debitTx);
        transactionRepository.save(creditTx);

        transactionLogger.onTransaction(debitTx);
        transactionLogger.onTransaction(creditTx);



        // publish both events
        // String debitMessage = "Account: " + fromAccount.getAccountNumber() +
        //                     ", Type: TRANSFER_OUT" +
        //                     ", Amount: " + amount +
        //                     ", To Account: " + toAccount.getAccountNumber();

        // String creditMessage = "Account: " + toAccount.getAccountNumber() +
        //                     ", Type: TRANSFER_IN" +
        //                     ", Amount: " + amount +
        //                     ", From Account: " + fromAccount.getAccountNumber();

        // transactionProducer.sendTransaction(debitMessage);
        // transactionProducer.sendTransaction(creditMessage);

        return debitTx;
    }

    public List<Transaction> getTransactions(Long accountId){
        return transactionRepository.findByAccountId(accountId);
    }
}

