package com.educativa.intranet.controller;

import com.educativa.intranet.dto.MensajeCreateDTO;
import com.educativa.intranet.security.UserPrincipal;
import com.educativa.intranet.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // LA RECEPCIONISTA DEL CHAT
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarMensaje(
            @Valid @RequestBody MensajeCreateDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
            
        chatService.enviarMensaje(userPrincipal.getId(), dto);
        
        return ResponseEntity.ok(Map.of("mensaje", "Mensaje enviado exitosamente."));
    }
}
