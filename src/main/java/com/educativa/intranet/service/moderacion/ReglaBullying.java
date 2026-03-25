package com.educativa.intranet.service.moderacion;

import com.educativa.intranet.model.CategoriaRiesgo;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ReglaBullying implements ReglaModeracion {

    private static final Pattern PATRON = Pattern.compile("(?i).*(inútil|inutil|salida|idiota|imbecil).*");

    @Override
    public boolean evaluar(String texto) {
        return PATRON.matcher(texto).matches();
    }

    @Override
    public CategoriaRiesgo getCategoria() {
        return CategoriaRiesgo.BULLYING;
    }

    @Override
    public String getDetalle() {
        return "Alerta de bullying o acoso escolar.";
    }
}
