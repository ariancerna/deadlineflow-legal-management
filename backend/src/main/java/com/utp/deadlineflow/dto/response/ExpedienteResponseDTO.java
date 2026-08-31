package com.utp.deadlineflow.dto.response;

import com.utp.deadlineflow.entity.EstadoExpediente;

import java.time.LocalDate;

/**
 * Proyección por rol (R5): "honorariosPactados" se envía como null
 * cuando el usuario solicitante no tiene permiso de visualizarlo (ver ExpedienteMapper).
 */
public record ExpedienteResponseDTO(
        Long id,
        String numeroExpediente,
        String tipoProceso,
        EstadoExpediente estado,
        String responsableNombre,
        Double honorariosPactados,
        LocalDate fechaApertura
) {}
