CREATE DATABASE CELS
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE CELS;

CREATE TABLE usuario (
    id_usuario INT(5) AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido_prim VARCHAR(50) NOT NULL,
    apellido_seg VARCHAR(50),
    rfc VARCHAR(13) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE,
    psswd VARCHAR(64) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE, -- TRUE = activo, FALSE = inactivo
    CONSTRAINT chk_rfc_length CHECK (CHAR_LENGTH(rfc) = 13)
);

CREATE TABLE cotizacion (
    id_folio INT(8) AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT(5) NOT NULL,
    fecha DATE NOT NULL,
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
    descripcion MEDIUMTEXT,
    cliente VARCHAR(64) NOT NULL,
    precio_final DECIMAL(10,2) NOT NULL,
    aprobado BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE material (
    id_material INT(5) AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(128) NOT NULL,
    tipo_material VARCHAR(32),
    costo DECIMAL(10,2) NOT NULL,
    tipo_unidad ENUM('lts', 'm²', 'kgs', 'pzas', 'mts', 'otro') NOT NULL
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
