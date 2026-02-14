package com.sena.BogotaMetroApp.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WebSocketTestController {
    private final SimpMessagingTemplate template;
    @PostMapping("/api/ws-test")
    public ResponseEntity<Void> testWs() {
        template.convertAndSend("/topic/test", Map.of(
                "tipo", "TEST",
                "mensaje", "Hola desde backend",
                "fecha", LocalDateTime.now().toString()
        ));
        return ResponseEntity.ok().build();
    }
}
