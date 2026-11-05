-- Ejecutar en PostgreSQL (pg-fis), base fis_db
CREATE TABLE IF NOT EXISTS articulos (
    idarticulo     SERIAL PRIMARY KEY,
    nombrearticulo VARCHAR(100) NOT NULL,
    valor          NUMERIC(12,2) NOT NULL
);
INSERT INTO articulos (nombrearticulo, valor) VALUES ('Portatil (PostgreSQL)', 2500000);
SELECT * FROM articulos;
