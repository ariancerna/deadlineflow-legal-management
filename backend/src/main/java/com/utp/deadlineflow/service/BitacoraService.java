package com.utp.deadlineflow.service;

import com.utp.deadlineflow.entity.Bitacora;
import com.utp.deadlineflow.entity.TipoEvento;
import com.utp.deadlineflow.repository.BitacoraRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BitacoraService {

    private final BitacoraRepository bitacoraRepository;

    public BitacoraService(BitacoraRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    /** Registro inmutable: solo INSERT, nunca UPDATE/DELETE (bitácora append-only). */
    public void registrar(Long entidadId, TipoEvento tipoEvento, String detalle) {
        String usuario = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "SISTEMA";

        Bitacora bitacora = Bitacora.builder()
                .entidadId(entidadId)
                .tipoEvento(tipoEvento)
                .detalle(detalle)
                .usuarioResponsable(usuario)
                .fechaEvento(LocalDateTime.now())
                .build();

        bitacoraRepository.save(bitacora);
    }
}
