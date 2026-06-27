CREATE TABLE shipments (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           order_id BIGINT NOT NULL,
                           direccion_destino VARCHAR(255) NOT NULL,
                           estado_actual VARCHAR(50) NOT NULL
);

CREATE TABLE tracking_history (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  shipment_id BIGINT NOT NULL,
                                  descripcion VARCHAR(255) NOT NULL,
                                  fecha_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_shipment FOREIGN KEY (shipment_id) REFERENCES shipments(id)
);