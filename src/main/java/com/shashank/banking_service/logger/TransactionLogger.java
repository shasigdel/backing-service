package com.shashank.banking_service.logger;

import org.springframework.stereotype.Component;

import com.shashank.banking_service.model.Transaction;

@Component
public class TransactionLogger {
    
    public void onTransaction(Transaction tx){
        System.out.println("Detected transaction " + tx.getType() + " " + tx.getAmount());
    }
}
