package com.tienda.inventario.config;

import com.tienda.inventario.model.Producto;
import com.tienda.inventario.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(ProductoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                System.out.println("Cargando datos iniciales de inventario (DataLoader)...");
                
                Producto p1 = new Producto();
                p1.setNombre("Seda Premium");
                p1.setTipo("Seda");
                p1.setDescripcion("Tela de seda de alta calidad para vestidos");
                p1.setPrecio(15000);
                p1.setStock(100);
                p1.setMetros(50.0);
                
                Producto p2 = new Producto();
                p2.setNombre("Algodon rustico");
                p2.setTipo("Algodon");
                p2.setDescripcion("Algodon ideal para sabanas");
                p2.setPrecio(8000);
                p2.setStock(200);
                p2.setMetros(150.0);

                Producto p3 = new Producto();
                p3.setNombre("Lino Italiano");
                p3.setTipo("Lino");
                p3.setDescripcion("Lino elegante importado");
                p3.setPrecio(22000);
                p3.setStock(50);
                p3.setMetros(30.0);

                repository.saveAll(List.of(p1, p2, p3));
                System.out.println("Datos cargados correctamente.");
            }
        };
    }
}
