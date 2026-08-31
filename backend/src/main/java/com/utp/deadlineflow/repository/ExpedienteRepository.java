package com.utp.deadlineflow.repository;

import com.utp.deadlineflow.entity.EstadoExpediente;
import com.utp.deadlineflow.entity.Expediente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpedienteRepository extends JpaRepository<Expediente, Long> {

    Page<Expediente> findByEstado(EstadoExpediente estado, Pageable pageable);

    @Query("SELECT e FROM Expediente e WHERE e.responsable.id = :responsableId AND e.estado <> 'CERRADO'")
    Page<Expediente> buscarActivosPorResponsable(@Param("responsableId") Long responsableId, Pageable pageable);

    @Query("""
           SELECT COUNT(e) FROM Expediente e
           WHERE e.estado = 'ABIERTO' OR e.estado = 'EN_PROCESO'
           """)
    long contarExpedientesActivos();
}
