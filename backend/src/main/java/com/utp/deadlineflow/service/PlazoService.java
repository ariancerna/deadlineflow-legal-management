package com.utp.deadlineflow.service;

import com.utp.deadlineflow.dto.request.PlazoRequestDTO;
import com.utp.deadlineflow.dto.response.PlazoResponseDTO;
import com.utp.deadlineflow.entity.*;
import com.utp.deadlineflow.exception.OperacionNoPermitidaException;
import com.utp.deadlineflow.exception.RecursoNoEncontradoException;
import com.utp.deadlineflow.mapper.PlazoMapper;
import com.utp.deadlineflow.repository.ExpedienteRepository;
import com.utp.deadlineflow.repository.PlazoRepository;
import com.utp.deadlineflow.repository.TareaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlazoService {

    private final PlazoRepository plazoRepository;
    private final ExpedienteRepository expedienteRepository;
    private final TareaRepository tareaRepository;
    private final BitacoraService bitacoraService;
    private final ScoreRiesgoService scoreRiesgoService;

    public PlazoService(PlazoRepository plazoRepository,
                         ExpedienteRepository expedienteRepository,
                         TareaRepository tareaRepository,
                         BitacoraService bitacoraService,
                         ScoreRiesgoService scoreRiesgoService) {
        this.plazoRepository = plazoRepository;
        this.expedienteRepository = expedienteRepository;
        this.tareaRepository = tareaRepository;
        this.bitacoraService = bitacoraService;
        this.scoreRiesgoService = scoreRiesgoService;
    }

    @Transactional
    public PlazoResponseDTO crear(PlazoRequestDTO request) {
        Expediente expediente = expedienteRepository.findById(request.expedienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Expediente no encontrado: " + request.expedienteId()));

        Plazo plazo = Plazo.builder()
                .expediente(expediente)
                .estado(EstadoPlazo.ACTIVO)
                .fechaLimite(request.fechaLimite())
                .build();

        Plazo guardado = plazoRepository.save(plazo);
        bitacoraService.registrar(guardado.getId(), TipoEvento.CREACION_PLAZO,
                "Plazo creado para expediente " + expediente.getNumeroExpediente());

        return PlazoMapper.toResponse(guardado);
    }

    /**
     * Regla R1: única vía de "cierre" administrativo de un Plazo.
     * Transaccional: si falla el registro en bitácora, se revierte la anulación (atomicidad).
     */
    @Transactional
    public PlazoResponseDTO anular(Long id, String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new OperacionNoPermitidaException("Debe registrar un motivo para anular el plazo");
        }

        Plazo plazo = plazoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plazo no encontrado: " + id));

        plazo.setEstado(EstadoPlazo.ANULADO);
        plazo.setMotivoAnulacion(motivo);
        Plazo actualizado = plazoRepository.save(plazo); // UPDATE, jamás delete/deleteById

        bitacoraService.registrar(id, TipoEvento.ANULACION_PLAZO, motivo);

        return PlazoMapper.toResponse(actualizado);
    }

    public double calcularScoreRiesgo(Long plazoId) {
        Plazo plazo = plazoRepository.findById(plazoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plazo no encontrado: " + plazoId));

        List<Tarea> tareasExpediente = tareaRepository.findByExpedienteId(plazo.getExpediente().getId());
        long pendientes = tareasExpediente.stream()
                .filter(t -> t.getEstado() == EstadoTarea.PENDIENTE || t.getEstado() == EstadoTarea.EN_PROCESO)
                .count();

        return scoreRiesgoService.calcular(plazo, (int) pendientes);
    }
}
