CREATE DATABASE CELS
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE CELS;

CREATE TABLE usuario (
    id_usuario INT(5) AUTO_INCREMENT PRIMARY KEY,
    codigo_tipo_usuario INT(5) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido_prim VARCHAR(50) NOT NULL,
    apellido_seg VARCHAR(50),
    fecha_nacimiento DATE NOT NULL,
    fecha_creacion_usuario TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado BOOLEAN NOT NULL DEFAULT TRUE -- TRUE = activo, FALSE = inactivo
);

CREATE TABLE us_psswds (
	id_usuario INT(5) PRIMARY KEY,
	psswd VARCHAR(64) NOT NULL,
    ultima_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_uspsswd_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
    ON UPDATE CASCADE 
    ON DELETE CASCADE
);

CREATE TABLE us_datos_sensibles(
	id_usuario INT(5) PRIMARY KEY,
	rfc VARCHAR(13) NOT NULL UNIQUE,
	email VARCHAR(254) NOT NULL UNIQUE,
    CONSTRAINT chk_rfc_length CHECK (CHAR_LENGTH(rfc) = 13),
    CONSTRAINT fk_usdatossensibles_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
    ON UPDATE CASCADE 
    ON DELETE CASCADE
);

CREATE TABLE cotizacion (
    id_folio INT(8) AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT(5) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_proyecto ENUM(
        'Caja Luminosa', 
        'Channel Letters', 
        'Letreros Luminosos',
        'Fachada', 
        'Rotulación Vehicular', 
        'Rotulación Comercial',
        'Impresión de Lona', 
        'Impresión de Vinil', 
        'Otro'
    ) NOT NULL,
    descripcion MEDIUMTEXT NOT NULL,
    cliente VARCHAR(64) NOT NULL,
    precio_final DECIMAL(10,2) NOT NULL,
    is_cotizacion_aprobada BOOL DEFAULT FALSE,
    is_contrato_aprobado BOOL DEFAULT FALSE,
    CONSTRAINT chk_precio_final CHECK (precio_final >= 0),
    CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE material (
    id_material INT(5) AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(128) NOT NULL,
    tipo_material VARCHAR(32),
    costo DECIMAL(10,2) NOT NULL,
    tipo_unidad ENUM('lts', 'm²', 'kgs', 'pzas', 'mts', 'otro') NOT NULL,
    CONSTRAINT chk_costo_material CHECK (costo >= 0)
);

CREATE TABLE cotizacion_material (
    id_folio INT(8) NOT NULL,
    id_material INT(5) NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_folio, id_material),
    CONSTRAINT fk_cotizacion FOREIGN KEY (id_folio) REFERENCES cotizacion(id_folio)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_material FOREIGN KEY (id_material) REFERENCES material(id_material)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE cotizacion_mano_de_obra (
    id_folio INT(8) NOT NULL,
    num_responsable INT(5) NOT NULL,
    costo_hora DECIMAL(10,2) NOT NULL,
    cantidad_horas DECIMAL(5,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_folio, num_responsable),
    CONSTRAINT fk_cotizacion_mano FOREIGN KEY (id_folio) REFERENCES cotizacion(id_folio)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE pedidos_taller (
	id_folio INT(8) PRIMARY KEY,
    estado_en_taller ENUM('Por iniciar', 'En producción', 'Con impedimentos', 'Terminado') 
    NOT NULL DEFAULT 'Por iniciar',
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_pedido_cotizacion FOREIGN KEY (id_folio) REFERENCES cotizacion(id_folio)
    ON UPDATE CASCADE 
    ON DELETE CASCADE
);

CREATE TABLE datos_encuesta (
	id_encuesta INT(8) PRIMARY KEY AUTO_INCREMENT,
    q1 BOOL,
    q2 VARCHAR(50),
    q3 INT(1)
);


CREATE INDEX idx_nombre_usuario ON usuario(nombre);
CREATE INDEX idx_nombre_material ON material(nombre);
CREATE UNIQUE INDEX idx_correo_usuario ON us_datos_sensibles(email);