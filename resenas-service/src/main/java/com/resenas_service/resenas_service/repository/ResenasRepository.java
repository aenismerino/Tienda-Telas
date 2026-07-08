package com.resenas_service.resenas_service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resenas_service.resenas_service.model.Resena;

@Repository
public interface ResenasRepository extends JpaRepository<Resena, Long> {
    
    List<Resena> findByTelaId(Long telaId);

    boolean existsByTelaIdAndUsuarioId(Long telaId, Long usuarioId);
}
