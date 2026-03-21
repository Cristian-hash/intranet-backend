package com.educativa.intranet.service;

import com.educativa.intranet.dto.MensajeCreateDTO;
import com.educativa.intranet.model.Mensaje;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.MensajeRepository;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MensajeRepository mensajeRepository;
    private final UsuarioRepository usuarioRepository;
    
    // Inyectamos a nuestro vigilante
    private final ModeracionService moderacionService;

    @Transactional
    public Mensaje enviarMensaje(Long emisorId, MensajeCreateDTO dto) {
        Usuario emisor = usuarioRepository.findById(emisorId)
                .orElseThrow(() -> new RuntimeException("Emisor inválido"));
        Usuario receptor = usuarioRepository.findById(dto.getReceptorId())
                .orElseThrow(() -> new RuntimeException("Receptor no encontrado"));

        // PASO 1: ORQUESTAR (Mover la maleta)
        // El ChatService simplemente toma los datos y los graba para que el otro usuario los pueda leer.
        Mensaje mensaje = Mensaje.builder()
                .emisor(emisor)
                .receptor(receptor)
                .contenido(dto.getContenido())
                .fecha(LocalDateTime.now())
                .build();
                
        mensaje = mensajeRepository.save(mensaje);

        // PASO 2: DELEGAR AL VIGILANTE EN MODO SILENCIOSO (Rayos X)
        // Llamamos al motor, pero gracias a que es @Async, esta función no se bloquea a esperar.
        // Avanza e inmediatamente le devuelve el "OK" al estudiante.
        moderacionService.auditarMensajeEnSegundoPlano(mensaje);

        return mensaje;
    }
}
