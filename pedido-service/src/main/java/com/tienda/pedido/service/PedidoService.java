package com.tienda.pedido.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.pedido.dto.PedidoDTO;
import com.tienda.pedido.model.Pedido;
import com.tienda.pedido.repository.PedidoRepository;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    public List<PedidoDTO> listarTodo() {
        return pedidoRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public PedidoDTO buscarPorId(Integer id) {
        return pedidoRepository.findById(id)
                .map(this::convertirADto)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public PedidoDTO guardar(PedidoDTO dto) {
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0 y no puede estar vacía");
        }
        if (dto.getPrecioUnitario() == null || dto.getPrecioUnitario() <= 0) {
            throw new RuntimeException("El precio unitario debe ser mayor a 0 y no puede estar vacío");
        }
        Integer calculado = dto.getCantidad() * dto.getPrecioUnitario();
        dto.setTotalPedido(calculado); 
    
        Pedido pedido = convertirAEntidad(dto);
        return convertirADto(pedidoRepository.save(pedido));
    }

    private PedidoDTO convertirADto(Pedido p) {
        PedidoDTO d = new PedidoDTO();
        d.setId(p.getId());
        d.setProductoId(p.getProductoId());
        d.setCantidad(p.getCantidad());
        d.setPrecioUnitario(p.getPrecioUnitario());
        d.setTotalPedido(p.getTotalPedido());
        d.setFechaPedido(p.getFechaPedido());
        return d;
    }

    private Pedido convertirAEntidad(PedidoDTO d) {
        Pedido p = new Pedido();
        p.setProductoId(d.getProductoId());
        p.setCantidad(d.getCantidad());
        p.setPrecioUnitario(d.getPrecioUnitario());
        p.setTotalPedido(d.getTotalPedido());
        return p;
    }


   public void eliminar(Integer id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Pedido no encontrado con ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    public PedidoDTO actualizar(Integer id, PedidoDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0 y no puede estar vacía");
        }
        if (dto.getPrecioUnitario() == null || dto.getPrecioUnitario() <= 0) {
            throw new RuntimeException("El precio unitario debe ser mayor a 0 y no puede estar vacío");
        }
        
        pedido.setProductoId(dto.getProductoId());
        pedido.setCantidad(dto.getCantidad());
        pedido.setPrecioUnitario(dto.getPrecioUnitario());
        
        Integer calculado = dto.getCantidad() * dto.getPrecioUnitario();
        pedido.setTotalPedido(calculado);
        
        return convertirADto(pedidoRepository.save(pedido));
    }

    public Integer obtenerTotalVentas() {
        return pedidoRepository.findAll().stream()
                .mapToInt(Pedido::getTotalPedido)
                .sum();
    }

    public List<PedidoDTO> buscarPorFecha(java.time.LocalDateTime inicio, java.time.LocalDateTime fin) {
        return pedidoRepository.findAll().stream()
                .filter(p -> p.getFechaPedido() != null && !p.getFechaPedido().isBefore(inicio) && !p.getFechaPedido().isAfter(fin))
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

}
