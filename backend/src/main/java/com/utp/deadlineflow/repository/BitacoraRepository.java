package com.utp.deadlineflow.repository;

import com.utp.deadlineflow.entity.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {

    @Query("""
           SELECT b FROM Bitacora b
           WHERE (:entidadId IS NULL OR b.entidadId = :entidadId)
             AND b.fechaEvento BETWEEN :desde AND :hasta
           ORDER BY b.fechaEvento DESC
           """)
    List<Bitacora> buscarPorFiltros(@Param("entidadId") Long entidadId,
                                     @Param("desde") LocalDateTime desde,
                                     @Param("hasta") LocalDateTime hasta);
}
