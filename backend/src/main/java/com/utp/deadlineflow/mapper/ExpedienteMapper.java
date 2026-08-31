package com.utp.deadlineflow.mapper;

import com.utp.deadlineflow.dto.response.ExpedienteResponseDTO;
import com.utp.deadlineflow.entity.Expediente;
import com.utp.deadlineflow.entity.Rol;

public class ExpedienteMapper {

    private ExpedienteMapper() {}

    /**
     * Regla R5: el campo honorariosPactados solo se expone a roles con visibilidad
     * financiera (ABOGADO, COORDINADOR, ADMINISTRADOR). Para ASISTENTE y AUDITOR viaja null.
     */
    public static ExpedienteResponseDTO toResponse(Expediente e, Rol rolSolicitante) {
        boolean puedeVerHonorarios = rolSolicitante == Rol.ABOGADO
                || rolSolicitante == Rol.COORDINADOR
                || rolSolicitante == Rol.ADMINISTRADOR;

        return new ExpedienteResponseDTO(
                e.getId(),
                e.getNumeroExpediente(),
                e.getTipoProceso(),
                e.getEstado(),
                e.getResponsable() != null ? e.getResponsable().getNombre() : null,
                puedeVerHonorarios ? e.getHonorariosPactados() : null,
                e.getFechaApertura()
        );
    }
}
