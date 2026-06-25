USE hotel_manager;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('Recepcionista', 'Administrador', 'Analista') NOT NULL,
    primer_login TINYINT(1) DEFAULT 1
);

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nom_cliente VARCHAR(150) NOT NULL,
    num_cliente VARCHAR(20) NOT NULL
);

CREATE TABLE habitacion (
    id_habitacion INT AUTO_INCREMENT PRIMARY KEY,
    num_habitacion VARCHAR(10) NOT NULL,
    tipo ENUM('Individual', 'Doble', 'Triple') NOT NULL,
    piso INT NOT NULL,
    precio_hora DECIMAL(10,2) NOT NULL,
    num_capacidad INT NOT NULL,
    estado ENUM('Libre', 'Ocupada', 'Limpieza') DEFAULT 'Libre'
);

CREATE TABLE reservacion (
    id_reservacion INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_habitacion INT NOT NULL,
    fecha_res DATETIME NOT NULL,
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME NOT NULL,
    periodo DECIMAL(10,2),
    precio_total DECIMAL(10,2),
    estado ENUM('Activa', 'Cancelada', 'Concluida') DEFAULT 'Activa',
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_habitacion) REFERENCES habitacion(id_habitacion)
);

CREATE TABLE registro (
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_reservacion INT NOT NULL,
    id_habitacion INT NOT NULL,
    hora_reg TIME NOT NULL,
    fecha_reg DATE NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_reservacion) REFERENCES reservacion(id_reservacion),
    FOREIGN KEY (id_habitacion) REFERENCES habitacion(id_habitacion)
);

INSERT INTO usuario (nombre, contrasena, rol, primer_login) 
VALUES ('admin', SHA2('holamundito',256), 'Administrador', 1);

ALTER TABLE habitacion
MODIFY COLUMN estado VARCHAR(50) NOT NULL DEFAULT 'Disponible';

ALTER TABLE reservacion
MODIFY COLUMN estado VARCHAR(50) NOT NULL DEFAULT 'Activa';

UPDATE habitacion
SET estado = 'Disponible'
WHERE estado IS NULL OR estado = '' OR estado = 'Libre';

UPDATE habitacion h
JOIN reservacion r ON h.id_habitacion = r.id_habitacion
SET h.estado = 'Ocupada'
WHERE r.estado = 'Activa';

UPDATE habitacion h
LEFT JOIN reservacion r
  ON h.id_habitacion = r.id_habitacion AND r.estado = 'Activa'
SET h.estado = 'Disponible'
WHERE r.id_reservacion IS NULL
  AND h.estado NOT IN ('Limpieza', 'Mantenimiento');
