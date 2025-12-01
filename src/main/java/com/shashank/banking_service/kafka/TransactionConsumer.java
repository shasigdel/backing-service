// package com.shashank.banking_service.kafka;

// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Service;

// @Service
// public class TransactionConsumer {

//    @KafkaListener(topics = "bank-transactions", groupId = "banking-group")
//     public void consume(String message) {
//         System.out.println("Received transaction: " + message);
//     }
// }
