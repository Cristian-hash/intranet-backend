package com.educativa.intranet.service;

import com.educativa.intranet.model.AlertaModeracion;
import com.educativa.intranet.model.Mensaje;
import com.educativa.intranet.repository.AlertaModeracionRepository;
import com.educativa.intranet.service.moderacion.ReglaModeracion;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeracionService {

    private final AlertaModeracionRepository alertaRepository;
    private final List<ReglaModeracion> reglasModeracion;

    /**
     * @Async es la magia aquí. Le dice a Java: "Haz esto en un hilo fantasma".
     * Devuélvele el control al usuario en milisegundos para que su chat no se trabe,
     * mientras tú te pones a analizar el texto con calma en segundo plano.
     */
    @Async 
    public void auditarMensajeEnSegundoPlano(Mensaje mensaje) {
        String texto = mensaje.getContenido();

        // Evaluamos el mensaje pasándolo por cada escáner disponible (OCP - Open/Closed Principle)
        for (ReglaModeracion regla : reglasModeracion) {
            evaluarRegla(mensaje, texto, regla);
        }
    }

    private void evaluarRegla(Mensaje mensaje, String texto, ReglaModeracion regla) {
        // match? = ¿Los rayos X encontraron algo prohibido?
        if (regla.evaluar(texto)) {
            
            // LA DECISIÓN ESTÁ AQUÍ. El Motor genera la Alerta Oculta (Ficha Oficial).
            AlertaModeracion alerta = AlertaModeracion.builder()
                    .mensaje(mensaje)
                    .categoria(regla.getCategoria())
                    .detalleAnalisis(regla.getDetalle())
                    .fechaDeteccion(LocalDateTime.now())
                    .revisadoPorAdmin(false) // Pendiente de revisión humana
                    .build();
                    
            // Delegamos al guardia para que la archive
            alertaRepository.save(alerta);
            
            // Imprimimos en la consola de tu servidor como aviso visual
            System.out.println("🚨 ---------------------------------------------------- 🚨");
            System.out.println("🚨 [ALERTA SILENCIOSA]: " + regla.getCategoria() + " detectada en Chat!");
            System.out.println("🚨 Emisor: " + mensaje.getEmisor().getNombre());
            System.out.println("🚨 Detalle: " + regla.getDetalle());
            System.out.println("🚨 ---------------------------------------------------- 🚨");
        }
    }
}
