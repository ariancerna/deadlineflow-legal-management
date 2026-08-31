package com.utp.deadlineflow.mapper;

import com.utp.deadlineflow.dto.response.TareaResponseDTO;
import com.utp.deadlineflow.entity.Tarea;

public class TareaMapper {

    private TareaMapper() {}

    public static TareaResponseDTO toResponse(Tarea t, double scoreRiesgo) {
        return new TareaResponseDTO(
                t.getId(),
                t.getTitulo(),
                t.getTipo(),
                t.getPrioridad(),
                t.getEstado(),
                t.getExpediente().getId(),
                t.getResponsable().getNombre(),
                t.getFechaLimite(),
                t.isEsDuplicada(),
                scoreRiesgo
        );
    }
}
