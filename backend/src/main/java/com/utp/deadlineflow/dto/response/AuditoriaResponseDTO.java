package com.utp.deadlineflow.dto.response;

import com.utp.deadlineflow.entity.TipoEvento;

import java.time.LocalDateTime;

public record AuditoriaResponseDTO(
        Long id,
        Long entidadId,
        TipoEvento tipoEvento,
        String detalle,
        String usuarioResponsable,
        LocalDateTime fechaEvento
) {}
