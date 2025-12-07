package com.myfinbank.customer.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myfinbank.customer.dto.ChatMessageView;
import com.myfinbank.customer.service.ChatService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:8082")   // allow admin UI on 8082
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/conversation/{customerId}")
    public ResponseEntity<List<ChatMessageView>> getConversation(@PathVariable Long customerId) {
        List<ChatMessageView> messages = chatService.getConversation(customerId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody Map<String, String> body) {
        try {
            Long customerId = Long.valueOf(body.get("customerId"));
            String fromRole = body.get("fromRole");
            String content  = body.get("content");
            ChatMessageView saved = chatService.sendMessage(customerId, fromRole, content);
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error saving chat message");
        }
    }
}
