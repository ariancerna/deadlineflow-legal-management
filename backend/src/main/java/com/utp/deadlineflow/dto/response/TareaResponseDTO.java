package com.utp.deadlineflow.dto.response;

import com.utp.deadlineflow.entity.EstadoTarea;
import com.utp.deadlineflow.entity.Prioridad;
import com.utp.deadlineflow.entity.TipoTarea;

import java.time.LocalDate;

public record TareaResponseDTO(
        Long id,
        String titulo,
        TipoTarea tipo,
        Prioridad prioridad,
        EstadoTarea estado,
        Long expedienteId,
        String responsableNombre,
        LocalDate fechaLimite,
        boolean esDuplicada,
        double scoreRiesgo
) {}
