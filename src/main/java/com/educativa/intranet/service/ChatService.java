package com.educativa.intranet.service;

import com.educativa.intranet.dto.MensajeCreateDTO;
import com.educativa.intranet.dto.MensajeResponseDTO;
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
    private final ModeracionService moderacionService;

    @Transactional
    public MensajeResponseDTO enviarMensaje(Long emisorId, MensajeCreateDTO dto) {
        Usuario emisor = obtenerUsuarioOArrojarError(emisorId, "Emisor");
        Usuario receptor = obtenerUsuarioOArrojarError(dto.getReceptorId(), "Receptor");

        Mensaje mensajeGuardado = asentarNuevoMensaje(emisor, receptor, dto.getContenido());
        
        // Delegamos asíncronamente (modo fantasma) al vigilante sin retrasar la respuesta
        moderacionService.auditarMensajeEnSegundoPlano(mensajeGuardado);

        return convertirHaciaDTO(mensajeGuardado);
    }

    // =====================================
    // FUNCIONES PRIVADAS UNITARIAS (Clean Code)
    // =====================================

    private Usuario obtenerUsuarioOArrojarError(Long usuarioId, String tipo) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Error en Chat: El " + tipo + " con ID " + usuarioId + " no es válido."));
    }

    private Mensaje asentarNuevoMensaje(Usuario emisor, Usuario receptor, String contenido) {
        Mensaje mensaje = Mensaje.builder()
                .emisor(emisor)
                .receptor(receptor)
                .contenido(contenido)
                .fecha(LocalDateTime.now())
                .build();
        return mensajeRepository.save(mensaje);
    }

    private MensajeResponseDTO convertirHaciaDTO(Mensaje mensaje) {
        return MensajeResponseDTO.builder()
                .id(mensaje.getId())
                .emisorId(mensaje.getEmisor().getId())
                .emisorNombre(mensaje.getEmisor().getNombre())
                .contenido(mensaje.getContenido())
                .fechaEntregado(mensaje.getFecha())
                .build();
    }
}
