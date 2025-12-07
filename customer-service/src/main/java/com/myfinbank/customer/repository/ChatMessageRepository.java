package com.myfinbank.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myfinbank.customer.model.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByCustomerIdOrderBySentAtAsc(Long customerId);
}
