// package com.shashank.banking_service.kafka;

// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.stereotype.Service;

// @Service
// public class TransactionProducer {

//     private final KafkaTemplate<String, String> kafkaTemplate;
//     private final String TOPIC = "bank-transactions";

//     public TransactionProducer(KafkaTemplate<String, String> kafkaTemplate) {
//         this.kafkaTemplate = kafkaTemplate;
//     }

//     public void sendTransaction(String message) {
//         kafkaTemplate.send(TOPIC, message);
//     }
// }
