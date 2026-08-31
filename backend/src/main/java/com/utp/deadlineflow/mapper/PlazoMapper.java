package com.utp.deadlineflow.mapper;

import com.utp.deadlineflow.dto.response.PlazoResponseDTO;
import com.utp.deadlineflow.entity.Plazo;

public class PlazoMapper {

    private PlazoMapper() {}

    public static PlazoResponseDTO toResponse(Plazo p) {
        return new PlazoResponseDTO(p.getId(), p.getEstado(), p.getFechaLimite());
    }
}
