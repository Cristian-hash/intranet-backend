package com.educativa.intranet.service;

import com.educativa.intranet.model.AlertaModeracion;
import com.educativa.intranet.model.CategoriaRiesgo;
import com.educativa.intranet.model.Mensaje;
import com.educativa.intranet.repository.AlertaModeracionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ModeracionService {

    private final AlertaModeracionRepository alertaRepository;

    // EL MOTOR DE REGLAS (Las configuraciones de los Rayos X)
    // Utilizamos Expresiones Regulares (Regex) complejas en minúsculas ignorando mayúsculas (?i).
    // Esto evita falsos positivos comprobando frases o contextos, en lugar de palabras sueltas.
    
    // Grooming / Privacidad
    private static final Pattern PATRON_PRIVACIDAD = Pattern.compile("(?i).*(whatsapp|numero|estas solo|direccion).*");
    
    // Fraude Académico: Captura "paso el examen" o "pasa el examen" o "pasar examen"
    private static final Pattern PATRON_FRAUDE = Pattern.compile("(?i).*(pas.*examen|cobro.*tarea|respuestas.*prueba).*");
    
    // Bullying / Acoso
    private static final Pattern PATRON_BULLYING = Pattern.compile("(?i).*(inútil|inutil|salida|idiota|imbecil).*");

    /**
     * @Async es la magia aquí. Le dice a Java: "Haz esto en un hilo fantasma".
     * Devuélvele el control al usuario en milisegundos para que su chat no se trabe,
     * mientras tú te pones a analizar el texto con calma en segundo plano.
     */
    @Async 
    public void auditarMensajeEnSegundoPlano(Mensaje mensaje) {
        String texto = mensaje.getContenido();

        // Evaluamos el mensaje pasándolo por cada escáner
        evaluarRegla(mensaje, texto, PATRON_PRIVACIDAD, CategoriaRiesgo.PRIVACIDAD, "Posible fuga de privacidad o grooming.");
        evaluarRegla(mensaje, texto, PATRON_FRAUDE, CategoriaRiesgo.FRAUDE, "Posible venta de tareas o trampa académica.");
        evaluarRegla(mensaje, texto, PATRON_BULLYING, CategoriaRiesgo.BULLYING, "Alerta de bullying o acoso escolar.");
    }

    private void evaluarRegla(Mensaje mensaje, String texto, Pattern patron, CategoriaRiesgo categoria, String detalle) {
        // match? = ¿Los rayos X encontraron algo prohibido?
        if (patron.matcher(texto).matches()) {
            
            // LA DECISIÓN ESTÁ AQUÍ. El Motor genera la Alerta Oculta (Ficha Oficial).
            AlertaModeracion alerta = AlertaModeracion.builder()
                    .mensaje(mensaje)
                    .categoria(categoria)
                    .detalleAnalisis(detalle)
                    .fechaDeteccion(LocalDateTime.now())
                    .revisadoPorAdmin(false) // Pendiente de revisión humana
                    .build();
                    
            // Delegamos al guardia para que la archive
            alertaRepository.save(alerta);
            
            // Imprimimos en la consola de tu servidor como aviso visual
            System.out.println("🚨 ---------------------------------------------------- 🚨");
            System.out.println("🚨 [ALERTA SILENCIOSA]: " + categoria + " detectada en Chat!");
            System.out.println("🚨 Emisor: " + mensaje.getEmisor().getNombre());
            System.out.println("🚨 Detalle: " + detalle);
            System.out.println("🚨 ---------------------------------------------------- 🚨");
        }
    }
}
