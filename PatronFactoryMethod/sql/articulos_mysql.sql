-- Ejecutar en MySQL (mysql-fis), base fis_db
CREATE TABLE IF NOT EXISTS articulos (
    idarticulo     INT AUTO_INCREMENT PRIMARY KEY,
    nombrearticulo VARCHAR(100) NOT NULL,
    valor          DECIMAL(12,2) NOT NULL
);
INSERT INTO articulos (nombrearticulo, valor) VALUES ('Monitor (MySQL)', 890000);
SELECT * FROM articulos;
