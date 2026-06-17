package com.tienda.pedido.repository;

import com.tienda.pedido.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<CarritoItem, Integer> {
    
    List<CarritoItem> findByUsuarioId(Integer usuarioId);
    
    void deleteById(Integer id);
    
    void deleteByUsuarioId(Integer usuarioId);
}
