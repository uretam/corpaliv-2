-- Ejecutar como administrador local de MySQL. No guardar contraseñas en este archivo.
CREATE DATABASE IF NOT EXISTS corpaliv_contenidos CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE DATABASE IF NOT EXISTS corpaliv_vinculacion CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Sustituir cada marcador al ejecutar de forma privada; usar contraseñas distintas.
-- CREATE USER 'corpaliv_contenidos_app'@'localhost' IDENTIFIED BY '<CLAVE_PRIVADA_1>';
-- CREATE USER 'corpaliv_vinculacion_app'@'localhost' IDENTIFIED BY '<CLAVE_PRIVADA_2>';
-- GRANT ALL PRIVILEGES ON corpaliv_contenidos.* TO 'corpaliv_contenidos_app'@'localhost';
-- GRANT ALL PRIVILEGES ON corpaliv_vinculacion.* TO 'corpaliv_vinculacion_app'@'localhost';
