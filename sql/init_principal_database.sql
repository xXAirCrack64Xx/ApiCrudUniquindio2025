-- 1. Crear la base de datos (si no existe) y seleccionarla
CREATE DATABASE IF NOT EXISTS principal_database;
USE principal_database;

-- 2. Eliminar las tablas existentes (si existen) para evitar conflictos con las relaciones
DROP TABLE IF EXISTS trabajos;
DROP TABLE IF EXISTS usuarios;

-- 3. Crear la tabla de usuarios
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,      -- 'PROFESOR' o 'ESTUDIANTE'
    clase VARCHAR(100) NOT NULL      -- Ej.: 'Lógica de programación', 'Programación I', etc.
);

-- 4. Crear la tabla de trabajos
CREATE TABLE trabajos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    calificacion DOUBLE,            -- Puede ser NULL si aún no se ha calificado
    fechaEnvio DATETIME NOT NULL,
    estudiante_id BIGINT NOT NULL,  -- Clave foránea que referencia al estudiante (rol ESTUDIANTE)
    profesor_id BIGINT,             -- Clave foránea que referencia al profesor (rol PROFESOR); puede ser NULL
    CONSTRAINT fk_estudiante FOREIGN KEY (estudiante_id) REFERENCES usuarios(id),
    CONSTRAINT fk_profesor FOREIGN KEY (profesor_id) REFERENCES usuarios(id)
);

-- 5. Insertar 5 profesores en la tabla de usuarios
INSERT INTO usuarios (nombre, email, rol, clase) VALUES 
  ('Profesor Juan Pérez', 'juan.perez@uniquindio.edu', 'PROFESOR', 'Lógica de programación'),
  ('Profesor María Gómez', 'maria.gomez@uniquindio.edu', 'PROFESOR', 'Programación I'),
  ('Profesor Carlos Rodríguez', 'carlos.rodriguez@uniquindio.edu', 'PROFESOR', 'Programación II'),
  ('Profesor Ana Martínez', 'ana.martinez@uniquindio.edu', 'PROFESOR', 'Programación III'),
  ('Profesor Luis Fernández', 'luis.fernandez@uniquindio.edu', 'PROFESOR', 'Programación avanzada');

-- 6. Insertar 15 estudiantes en la tabla de usuarios
INSERT INTO usuarios (nombre, email, rol, clase) VALUES 
  ('Estudiante 1', 'estudiante1@uniquindio.edu', 'ESTUDIANTE', 'Lógica de programación'),
  ('Estudiante 2', 'estudiante2@uniquindio.edu', 'ESTUDIANTE', 'Lógica de programación'),
  ('Estudiante 3', 'estudiante3@uniquindio.edu', 'ESTUDIANTE', 'Lógica de programación'),
  ('Estudiante 4', 'estudiante4@uniquindio.edu', 'ESTUDIANTE', 'Programación I'),
  ('Estudiante 5', 'estudiante5@uniquindio.edu', 'ESTUDIANTE', 'Programación I'),
  ('Estudiante 6', 'estudiante6@uniquindio.edu', 'ESTUDIANTE', 'Programación I'),
  ('Estudiante 7', 'estudiante7@uniquindio.edu', 'ESTUDIANTE', 'Programación II'),
  ('Estudiante 8', 'estudiante8@uniquindio.edu', 'ESTUDIANTE', 'Programación II'),
  ('Estudiante 9', 'estudiante9@uniquindio.edu', 'ESTUDIANTE', 'Programación II'),
  ('Estudiante 10', 'estudiante10@uniquindio.edu', 'ESTUDIANTE', 'Programación III'),
  ('Estudiante 11', 'estudiante11@uniquindio.edu', 'ESTUDIANTE', 'Programación III'),
  ('Estudiante 12', 'estudiante12@uniquindio.edu', 'ESTUDIANTE', 'Programación III'),
  ('Estudiante 13', 'estudiante13@uniquindio.edu', 'ESTUDIANTE', 'Programación avanzada'),
  ('Estudiante 14', 'estudiante14@uniquindio.edu', 'ESTUDIANTE', 'Programación avanzada'),
  ('Estudiante 15', 'estudiante15@uniquindio.edu', 'ESTUDIANTE', 'Programación avanzada');

-- Nota: Con este orden, los 5 primeros registros (id 1 a 5) son profesores y los siguientes (id 6 a 20) son estudiantes.

-- 7. Insertar 20 registros en la tabla de trabajos

-- 7.1. Insertar 10 trabajos con calificación asignada
INSERT INTO trabajos (titulo, descripcion, calificacion, fechaEnvio, estudiante_id, profesor_id) VALUES 
  ('Trabajo 1', 'Descripción del trabajo 1', 85.0, '2025-02-01 09:00:00', 6, 1),
  ('Trabajo 2', 'Descripción del trabajo 2', 92.0, '2025-02-01 09:30:00', 7, 2),
  ('Trabajo 3', 'Descripción del trabajo 3', 78.0, '2025-02-01 10:00:00', 8, 3),
  ('Trabajo 4', 'Descripción del trabajo 4', 88.0, '2025-02-01 10:30:00', 9, 4),
  ('Trabajo 5', 'Descripción del trabajo 5', 95.0, '2025-02-01 11:00:00', 10, 5),
  ('Trabajo 6', 'Descripción del trabajo 6', 80.0, '2025-02-02 09:00:00', 11, 1),
  ('Trabajo 7', 'Descripción del trabajo 7', 82.0, '2025-02-02 09:30:00', 12, 2),
  ('Trabajo 8', 'Descripción del trabajo 8', 87.0, '2025-02-02 10:00:00', 13, 3),
  ('Trabajo 9', 'Descripción del trabajo 9', 91.0, '2025-02-02 10:30:00', 14, 4),
  ('Trabajo 10', 'Descripción del trabajo 10', 89.0, '2025-02-02 11:00:00', 15, 5);

-- 7.2. Insertar 10 trabajos sin calificar
INSERT INTO trabajos (titulo, descripcion, calificacion, fechaEnvio, estudiante_id, profesor_id) VALUES 
  ('Trabajo 11', 'Descripción del trabajo 11', NULL, '2025-02-03 09:00:00', 16, NULL),
  ('Trabajo 12', 'Descripción del trabajo 12', NULL, '2025-02-03 09:30:00', 17, NULL),
  ('Trabajo 13', 'Descripción del trabajo 13', NULL, '2025-02-03 10:00:00', 18, NULL),
  ('Trabajo 14', 'Descripción del trabajo 14', NULL, '2025-02-03 10:30:00', 19, NULL),
  ('Trabajo 15', 'Descripción del trabajo 15', NULL, '2025-02-03 11:00:00', 20, NULL),
  ('Trabajo 16', 'Descripción del trabajo 16', NULL, '2025-02-04 09:00:00', 6, NULL),
  ('Trabajo 17', 'Descripción del trabajo 17', NULL, '2025-02-04 09:30:00', 7, NULL),
  ('Trabajo 18', 'Descripción del trabajo 18', NULL, '2025-02-04 10:00:00', 8, NULL),
  ('Trabajo 19', 'Descripción del trabajo 19', NULL, '2025-02-04 10:30:00', 9, NULL),
  ('Trabajo 20', 'Descripción del trabajo 20', NULL, '2025-02-04 11:00:00', 10, NULL);


