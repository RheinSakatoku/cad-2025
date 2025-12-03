package com.example.univercity.controller;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class MessageController {

    private static final int MAX_MESSAGES = 100; // лимит сообщений
    private final List<Message> userMessages = new ArrayList<>();

    // Вспомогательный класс для хранения сообщения + дата
    private static class Message {
        private String text;
        private LocalDateTime timestamp;

        public Message(String text) {
            this.text = text;
            this.timestamp = LocalDateTime.now();
        }

        public String getText() {
            return text;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setText(String text) {
            this.text = text;
            this.timestamp = LocalDateTime.now();
        }
    }

    @GetMapping("/")
    public String helloWorld() {
        return "Hello, World!";
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return userMessages;
    }

    @PostMapping("/messages")
    public String publishMessage(@RequestBody String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Error: message cannot be empty!";
        }

        if (userMessages.size() >= MAX_MESSAGES) {
            // удаляем самое старое сообщение
            userMessages.remove(0);
        }

        userMessages.add(new Message(message));
        return "Message published successfully!";
    }

    @PutMapping("/messages/{index}")
    public String updateMessage(@PathVariable int index, @RequestBody String message) {
        if (index >= 0 && index < userMessages.size()) {
            if (message == null || message.trim().isEmpty()) {
                return "Error: message cannot be empty!";
            }
            userMessages.get(index).setText(message);
            return "Message updated successfully!";
        }
        return "Message not found at index " + index;
    }

    @DeleteMapping("/messages/{index}")
    public String deleteMessage(@PathVariable int index) {
        if (index >= 0 && index < userMessages.size()) {
            userMessages.remove(index);
            return "Message deleted successfully!";
        }
        return "Message not found at index " + index;
    }

    // 1. Очистка всех сообщений
    @DeleteMapping("/messages")
    public String clearAllMessages() {
        userMessages.clear();
        return "All messages cleared!";
    }

    // 3. Получить сообщение по индексу
    @GetMapping("/messages/{index}")
    public Object getMessageByIndex(@PathVariable int index) {
        if (index >= 0 && index < userMessages.size()) {
            return userMessages.get(index);
        }
        return "Message not found at index " + index;
    }

    // 5. Получить сообщения после определенной даты
    @GetMapping("/messages/after")
    public List<Message> getMessagesAfter(@RequestParam String dateTime) {
        LocalDateTime dt;
        try {
            dt = LocalDateTime.parse(dateTime); // ожидаем формат "yyyy-MM-ddTHH:mm:ss"
        } catch (Exception e) {
            return new ArrayList<>(); // пустой список при неверном формате
        }

        List<Message> result = new ArrayList<>();
        for (Message msg : userMessages) {
            if (msg.getTimestamp().isAfter(dt)) {
                result.add(msg);
            }
        }
        return result;
    }

    // 6. Подсчет количества сообщений
    @GetMapping("/messages/count")
    public int countMessages() {
        return userMessages.size();
    }
}
