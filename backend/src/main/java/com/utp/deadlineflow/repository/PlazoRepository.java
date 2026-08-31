package com.utp.deadlineflow.repository;

import com.utp.deadlineflow.entity.Plazo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * NOTA (R1): deliberadamente NO se expone deleteById/delete a nivel de servicio.
 * JpaRepository los hereda a nivel de framework, pero PlazoService nunca los invoca;
 * la única vía de "cierre" de un Plazo es anular(), verificado en PlazoServiceTest.
 */
public interface PlazoRepository extends JpaRepository<Plazo, Long> {

    @Query("""
           SELECT p FROM Plazo p
           WHERE p.estado = 'ACTIVO' AND p.fechaLimite BETWEEN :desde AND :hasta
           """)
    List<Plazo> buscarActivosProximosAVencer(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    long countByEstado(com.utp.deadlineflow.entity.EstadoPlazo estado);
}
