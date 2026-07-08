package com.tienda.pedido.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.pedido.dto.PedidoDTO;
import com.tienda.pedido.model.Pedido;
import com.tienda.pedido.repository.PedidoRepository;
import com.tienda.pedido.exception.ResourceNotFoundException;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> listarTodo() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Integer id) {
        return pedidoRepository.findById(id);
    }

    public Pedido guardar(PedidoDTO dto) {
        // La validación de >0 se hace en el Controller con @Valid.
        Integer calculado = dto.getCantidad() * dto.getPrecioUnitario();
        dto.setTotalPedido(calculado);

        Pedido pedido = dto.toModel();
        return pedidoRepository.save(pedido);
    }

    public void eliminar(Integer id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: Pedido no encontrado con ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    public Pedido actualizar(Integer id, PedidoDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));

        pedido.setProductoId(dto.getProductoId());
        pedido.setCantidad(dto.getCantidad());
        pedido.setPrecioUnitario(dto.getPrecioUnitario());

        Integer calculado = dto.getCantidad() * dto.getPrecioUnitario();
        pedido.setTotalPedido(calculado);

        return pedidoRepository.save(pedido);
    }

    public Integer obtenerTotalVentas() {
        return pedidoRepository.findAll().stream()
                .mapToInt(Pedido::getTotalPedido)
                .sum();
    }

    public List<Pedido> buscarPorFecha(java.time.LocalDateTime inicio, java.time.LocalDateTime fin) {
        return pedidoRepository.findAll().stream()
                .filter(p -> p.getFechaPedido() != null && !p.getFechaPedido().isBefore(inicio)
                        && !p.getFechaPedido().isAfter(fin))
                .toList();
    }
}
