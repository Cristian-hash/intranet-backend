package com.educativa.intranet.service.moderacion;

import com.educativa.intranet.model.CategoriaRiesgo;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ReglaFraude implements ReglaModeracion {

    private static final Pattern PATRON = Pattern.compile("(?i).*(pas.*examen|cobro.*tarea|respuestas.*prueba).*");

    @Override
    public boolean evaluar(String texto) {
        return PATRON.matcher(texto).matches();
    }

    @Override
    public CategoriaRiesgo getCategoria() {
        return CategoriaRiesgo.FRAUDE;
    }

    @Override
    public String getDetalle() {
        return "Posible venta de tareas o trampa académica.";
    }
}
