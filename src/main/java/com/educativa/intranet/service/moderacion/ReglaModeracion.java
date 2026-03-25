package com.educativa.intranet.service.moderacion;

import com.educativa.intranet.model.CategoriaRiesgo;

public interface ReglaModeracion {
    /**
     * Evalúa el texto del mensaje y retorna true si viola la regla.
     */
    boolean evaluar(String texto);

    /**
     * Retorna la categoría de riesgo asociada a esta regla.
     */
    CategoriaRiesgo getCategoria();

    /**
     * Retorna el detalle explicativo de la infracción.
     */
    String getDetalle();
}
