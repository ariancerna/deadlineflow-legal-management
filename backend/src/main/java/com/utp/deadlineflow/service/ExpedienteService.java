package com.utp.deadlineflow.service;

import com.utp.deadlineflow.dto.request.ExpedienteRequestDTO;
import com.utp.deadlineflow.dto.response.ExpedienteResponseDTO;
import com.utp.deadlineflow.entity.*;
import com.utp.deadlineflow.exception.RecursoNoEncontradoException;
import com.utp.deadlineflow.mapper.ExpedienteMapper;
import com.utp.deadlineflow.repository.ExpedienteRepository;
import com.utp.deadlineflow.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ExpedienteService {

    private final ExpedienteRepository expedienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final BitacoraService bitacoraService;

    public ExpedienteService(ExpedienteRepository expedienteRepository,
                              UsuarioRepository usuarioRepository,
                              BitacoraService bitacoraService) {
        this.expedienteRepository = expedienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.bitacoraService = bitacoraService;
    }

    @Transactional
    public ExpedienteResponseDTO crear(ExpedienteRequestDTO request, Rol rolSolicitante) {
        Usuario responsable = usuarioRepository.findById(request.responsableId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Responsable no encontrado: " + request.responsableId()));

        Expediente expediente = Expediente.builder()
                .numeroExpediente(request.numeroExpediente())
                .tipoProceso(request.tipoProceso())
                .estado(EstadoExpediente.ABIERTO)
                .responsable(responsable)
                .honorariosPactados(request.honorariosPactados())
                .fechaApertura(LocalDate.now())
                .build();

        Expediente guardado = expedienteRepository.save(expediente);
        bitacoraService.registrar(guardado.getId(), TipoEvento.CREACION_EXPEDIENTE,
                "Expediente creado: " + guardado.getNumeroExpediente());

        return ExpedienteMapper.toResponse(guardado, rolSolicitante);
    }

    public ExpedienteResponseDTO buscarPorId(Long id, Rol rolSolicitante) {
        Expediente expediente = expedienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Expediente no encontrado: " + id));
        return ExpedienteMapper.toResponse(expediente, rolSolicitante);
    }

    public Page<ExpedienteResponseDTO> listarPorEstado(EstadoExpediente estado, Rol rolSolicitante, Pageable pageable) {
        return expedienteRepository.findByEstado(estado, pageable)
                .map(e -> ExpedienteMapper.toResponse(e, rolSolicitante));
    }
}
