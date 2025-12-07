package com.myfinbank.customer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myfinbank.customer.dto.ChatMessageView;
import com.myfinbank.customer.model.ChatMessage;
import com.myfinbank.customer.repository.ChatMessageRepository;

@Service
public class ChatService {

    private final ChatMessageRepository chatRepo;

    public ChatService(ChatMessageRepository chatRepo) {
        this.chatRepo = chatRepo;
    }

    @Transactional
    public ChatMessageView sendMessage(Long customerId, String fromRole, String content) {
        ChatMessage msg = new ChatMessage();
        msg.setCustomerId(customerId);
        msg.setFromRole(fromRole);
        msg.setContent(content);
        ChatMessage saved = chatRepo.save(msg);
        return toView(saved);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageView> getConversation(Long customerId) {
        return chatRepo.findByCustomerIdOrderBySentAtAsc(customerId)
                .stream()
                .map(this::toView)
                .collect(Collectors.toList());
    }

    private ChatMessageView toView(ChatMessage msg) {
        ChatMessageView v = new ChatMessageView();
        v.setId(msg.getId());
        v.setCustomerId(msg.getCustomerId());
        v.setFromRole(msg.getFromRole());
        v.setContent(msg.getContent());
        v.setSentAt(msg.getSentAt());
        return v;
    }
}
