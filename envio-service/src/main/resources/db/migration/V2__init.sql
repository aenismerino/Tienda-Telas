CREATE TABLE envios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    rut_cliente VARCHAR(20) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_envio DATETIME
);

INSERT INTO envios (pedido_id, rut_cliente, direccion, estado, fecha_envio) VALUES (1, '11111111-1', 'Calle 1', 'PENDIENTE', '2026-05-20 10:00:00');
INSERT INTO envios (pedido_id, rut_cliente, direccion, estado, fecha_envio) VALUES (2, '22222222-2', 'Calle 2', 'ENVIADO', '2026-05-20 11:00:00');
INSERT INTO envios (pedido_id, rut_cliente, direccion, estado, fecha_envio) VALUES (3, '33333333-3', 'Calle 3', 'PENDIENTE', '2026-05-20 12:00:00');
INSERT INTO envios (pedido_id, rut_cliente, direccion, estado, fecha_envio) VALUES (4, '44444444-4', 'Calle 4', 'ENTREGADO', '2026-05-20 13:00:00');
INSERT INTO envios (pedido_id, rut_cliente, direccion, estado, fecha_envio) VALUES (5, '55555555-5', 'Calle 5', 'PENDIENTE', '2026-05-20 14:00:00');
INSERT INTO envios (pedido_id, rut_cliente, direccion, estado, fecha_envio) VALUES (6, '66666666-6', 'Calle 6', 'ENVIADO', '2026-05-20 15:00:00');
INSERT INTO envios (pedido_id, rut_cliente, direccion, estado, fecha_envio) VALUES (7, '77777777-7', 'Calle 7', 'ENTREGADO', '2026-05-20 16:00:00');
INSERT INTO envios (pedido_id, rut_cliente, direccion, estado, fecha_envio) VALUES (8, '88888888-8', 'Calle 8', 'PENDIENTE', '2026-05-20 17:00:00');
INSERT INTO envios (pedido_id, rut_cliente, direccion, estado, fecha_envio) VALUES (9, '99999999-9', 'Calle 9', 'ENVIADO', '2026-05-20 18:00:00');
INSERT INTO envios (pedido_id, rut_cliente, direccion, estado, fecha_envio) VALUES (10, '10101010-0', 'Calle 10', 'ENTREGADO', '2026-05-20 19:00:00');
