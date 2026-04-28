CREATE DATABASE restaurante_db_oficial;
USE restaurante_db_oficial;

-- =====================================================
-- CREACION DE TABLAS
-- =====================================================

-- tabla configuracion
CREATE TABLE configuracion (
    id_configuracion INT AUTO_INCREMENT,
    nombre VARCHAR(45),
    valor DECIMAL(10,2),
    descripcion VARCHAR(100),
    CONSTRAINT pk_configuracion PRIMARY KEY (id_configuracion)
);

-- tabla cliente
CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT,
    nombre VARCHAR(45),
    apellido VARCHAR(45),
    cedula VARCHAR(10),
    celular VARCHAR(10),
    correo VARCHAR(100),
    CONSTRAINT pk_cliente PRIMARY KEY (id_cliente),
    CONSTRAINT uq_cliente_cedula UNIQUE (cedula),
    CONSTRAINT uq_cliente_correo UNIQUE (correo)
);

-- tabla usuario
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT,
    nombre VARCHAR(45),
    apellido VARCHAR(45),
    cedula VARCHAR(10),
    fecha_nacimiento DATE,
    celular VARCHAR(10),
    correo VARCHAR(100) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
    CONSTRAINT uq_usuario_cedula UNIQUE (cedula),
    CONSTRAINT uq_usuario_correo UNIQUE (correo)
);

-- tabla rol
CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL,
    descripcion VARCHAR(200),
    CONSTRAINT pk_rol PRIMARY KEY (id_rol),
    CONSTRAINT uq_rol_nombre UNIQUE (nombre)
);

-- tabla usuario_rol
CREATE TABLE usuario_rol (
    id_usuario INT,
    id_rol INT,
    CONSTRAINT pk_usuario_rol PRIMARY KEY (id_usuario, id_rol),
    CONSTRAINT fk_usuario_rol_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_usuario_rol_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

-- tabla permiso
CREATE TABLE permiso (
    id_permiso INT AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL,
    descripcion VARCHAR(200),
    CONSTRAINT pk_permiso PRIMARY KEY (id_permiso),
    CONSTRAINT uq_permiso_nombre UNIQUE (nombre)
);

-- tabla rol_permiso
CREATE TABLE rol_permiso (
    id_rol INT,
    id_permiso INT,
    CONSTRAINT pk_rol_permiso PRIMARY KEY (id_rol, id_permiso),
    CONSTRAINT fk_rol_permiso_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
    CONSTRAINT fk_rol_permiso_permiso FOREIGN KEY (id_permiso) REFERENCES permiso(id_permiso)
);

-- tabla categoria
CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT,
    nombre VARCHAR(50),
    descripcion VARCHAR(200),
    CONSTRAINT pk_categoria PRIMARY KEY (id_categoria)
);

-- tabla producto
CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT,
    id_categoria INT,
    nombre VARCHAR(50),
    precio DECIMAL(10,2),
    CONSTRAINT pk_producto PRIMARY KEY (id_producto),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

-- tabla pedido
CREATE TABLE pedido (
    id_pedido INT AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    id_usuario INT NOT NULL,
    tipo_pedido ENUM('local', 'llevar', 'delivery') NOT NULL,
    codigo VARCHAR(12) NOT NULL,
    estado ENUM('PENDIENTE', 'EN_PREPARACION', 'LISTO', 'EN_RUTA', 'ENTREGADO', 'CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal DECIMAL(10,2) NOT NULL,
    impuesto DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    CONSTRAINT pk_pedido PRIMARY KEY (id_pedido),
    CONSTRAINT uq_pedido_codigo UNIQUE (codigo),
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- tabla detalle_pedido
CREATE TABLE detalle_pedido (
    id_detalle INT AUTO_INCREMENT,
    id_pedido INT,
    id_producto INT,
    cantidad INT,
    precio_unitario DECIMAL(10,2),
    subtotal DECIMAL(10,2),
    CONSTRAINT pk_detalle_pedido PRIMARY KEY (id_detalle),
    CONSTRAINT fk_detalle_pedido_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    CONSTRAINT fk_detalle_pedido_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

-- tabla tarifa (Actualizada con km_min, km_max y precio)
CREATE TABLE tarifa (
    id_tarifa INT AUTO_INCREMENT,
    km_min DECIMAL(6,2) NOT NULL,
    km_max DECIMAL(6,2) NOT NULL,
    precio DECIMAL(8,2) NOT NULL,
    descripcion VARCHAR(200),
    CONSTRAINT pk_tarifa PRIMARY KEY (id_tarifa),
    CONSTRAINT ck_km_rango CHECK (km_max >= km_min)
);

-- tabla entrega_pedido
CREATE TABLE entrega_pedido (
    id_entrega INT NOT NULL AUTO_INCREMENT,
    id_pedido INT NOT NULL,
    id_usuario_repartidor INT NOT NULL,
    id_tarifa INT NOT NULL,
    direccion_entrega VARCHAR(255) NOT NULL,
    fecha_hora_salida DATETIME NOT NULL,
    fecha_hora_entrega DATETIME NULL,
    nombre_recibe VARCHAR(120) NULL,
    costo_envio DECIMAL(8,2) NOT NULL,
    distancia_km DECIMAL(6,2) NOT NULL,
    observacion TEXT NULL,
    CONSTRAINT pk_entrega_pedido PRIMARY KEY (id_entrega),
    CONSTRAINT uq_entrega_pedido UNIQUE (id_pedido),
    CONSTRAINT fk_entrega_pedido_ref FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    CONSTRAINT fk_entrega_repartidor FOREIGN KEY (id_usuario_repartidor) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_entrega_tarifa FOREIGN KEY (id_tarifa) REFERENCES tarifa(id_tarifa),
    CONSTRAINT ck_distancia CHECK (distancia_km >= 0),
    CONSTRAINT ck_costo_envio CHECK (costo_envio >= 0),
    CONSTRAINT ck_fechas_entrega CHECK (fecha_hora_entrega IS NULL OR fecha_hora_entrega >= fecha_hora_salida)
);

-- tabla historial_pedido
CREATE TABLE historial_pedido (
    id_historial INT NOT NULL AUTO_INCREMENT,
    id_pedido INT NOT NULL,
    id_usuario INT NOT NULL,
    estado ENUM('PENDIENTE', 'EN_PREPARACION', 'LISTO', 'EN_RUTA', 'ENTREGADO', 'CANCELADO') NOT NULL,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion TEXT NULL,
    CONSTRAINT pk_historial_pedido PRIMARY KEY (id_historial),
    CONSTRAINT fk_historial_pedido_ref FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    CONSTRAINT fk_historial_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- tabla factura
CREATE TABLE factura (
    id_factura INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_pedido INT NOT NULL,
    numero VARCHAR(20) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal DECIMAL(10,2) NOT NULL,
    impuesto DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(10,2) NOT NULL,
    CONSTRAINT pk_factura PRIMARY KEY (id_factura),
    CONSTRAINT uq_factura_numero UNIQUE (numero),
    CONSTRAINT uq_factura_pedido UNIQUE (id_pedido),
    CONSTRAINT fk_factura_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_factura_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    CONSTRAINT ck_factura_subtotal CHECK (subtotal >= 0),
    CONSTRAINT ck_factura_impuesto CHECK (impuesto >= 0),
    CONSTRAINT ck_factura_total CHECK (total = subtotal + impuesto)
);

-- =====================================================
-- INDEX
-- =====================================================

CREATE INDEX idx_pedido_estado        ON pedido (estado);
CREATE INDEX idx_pedido_fecha         ON pedido (fecha_hora);
CREATE INDEX idx_pedido_cliente       ON pedido (id_cliente);
CREATE INDEX idx_pedido_usuario       ON pedido (id_usuario);

CREATE INDEX idx_historial_pedido     ON historial_pedido (id_pedido);
CREATE INDEX idx_historial_usuario    ON historial_pedido (id_usuario);
CREATE INDEX idx_historial_estado     ON historial_pedido (estado);

CREATE INDEX idx_entrega_repartidor   ON entrega_pedido (id_usuario_repartidor);
CREATE INDEX idx_entrega_tarifa       ON entrega_pedido (id_tarifa);

CREATE INDEX idx_factura_usuario      ON factura (id_usuario);

-- ==============================================================
-- INSERCIÓN DE DATOS PARA LAS TABLAS ROL, CONFIGURACION Y TARIFA
-- ==============================================================
USE restaurante_db_oficial;
INSERT INTO tarifa(km_min, km_max, precio) VALUES
(0.0, 0.9, 1.25),
(1.0, 2.4, 1.50),
(2.5, 3.4, 1.75),
(3.5, 3.9, 2.00),
(4.0, 4.9, 2.25),
(5.0, 5.9, 2.50),
(6.0, 6.9, 2.75),
(7.0, 7.9, 3.00),
(8.0, 8.9, 3.50),
(9.0, 9.9, 4.00),
(10.0, 10.9, 4.50);

-- SQL ROLES --
INSERT INTO rol(nombre, descripcion) VALUES
('Administrador', 'Control total del sistema'),
('Cajero', 'Gestiona pagos y facturación'),
('Mesero', 'Toma pedidos de los clientes'),
('Cocinero', 'Prepara los pedidos'),
('Cliente', 'Realiza pedidos'),
('Inventario', 'Gestiona stock e insumos'),
('Repartidor', 'Entrega pedidos a domicilio'),
('Gerente', 'Supervisa operaciones y reportes');

-- CONFIGURACION --
INSERT INTO configuracion (nombre, valor, descripcion) VALUES
('IVA','15', 'Consiste en el recargo del 15% al monto del precio final determinado por el vendedor');
DESCRIBE configuracion;
