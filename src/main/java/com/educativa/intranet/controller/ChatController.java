package com.educativa.intranet.controller;

import com.educativa.intranet.dto.MensajeCreateDTO;
import com.educativa.intranet.dto.MensajeResponseDTO;
import com.educativa.intranet.security.UserPrincipal;
import com.educativa.intranet.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<MensajeResponseDTO> enviarMensajeSeguro(
            @AuthenticationPrincipal UserPrincipal emisorLogueado, 
            @Valid @RequestBody MensajeCreateDTO dto) {
            
        Long idMio = emisorLogueado.getId();
        return ResponseEntity.ok(chatService.enviarMensaje(idMio, dto));
    }
}
