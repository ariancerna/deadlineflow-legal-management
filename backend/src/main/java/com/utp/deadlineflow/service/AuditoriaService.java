package com.utp.deadlineflow.service;

import com.utp.deadlineflow.dto.response.AuditoriaResponseDTO;
import com.utp.deadlineflow.repository.BitacoraRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaService {

    private final BitacoraRepository bitacoraRepository;

    public AuditoriaService(BitacoraRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    public List<AuditoriaResponseDTO> buscar(Long entidadId, LocalDateTime desde, LocalDateTime hasta) {
        return bitacoraRepository.buscarPorFiltros(entidadId, desde, hasta).stream()
                .map(b -> new AuditoriaResponseDTO(
                        b.getId(), b.getEntidadId(), b.getTipoEvento(),
                        b.getDetalle(), b.getUsuarioResponsable(), b.getFechaEvento()))
                .toList();
    }
}
