package com.educativa.intranet.service.moderacion;

import com.educativa.intranet.model.CategoriaRiesgo;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ReglaPrivacidad implements ReglaModeracion {

    private static final Pattern PATRON = Pattern.compile("(?i).*(whatsapp|numero|estas solo|direccion).*");

    @Override
    public boolean evaluar(String texto) {
        return PATRON.matcher(texto).matches();
    }

    @Override
    public CategoriaRiesgo getCategoria() {
        return CategoriaRiesgo.PRIVACIDAD;
    }

    @Override
    public String getDetalle() {
        return "Posible fuga de privacidad o grooming.";
    }
}
