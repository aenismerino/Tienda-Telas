package com.tienda.carrito.repository;

import com.tienda.carrito.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<CarritoItem, Integer> {
    
    List<CarritoItem> findByUsuarioId(String usuarioId);
    
    void deleteById(Integer id);
    
    void deleteByUsuarioId(String usuarioId);
}
