# 🐳 Taller Práctico Docker: Administración de Contenedores y Bases de Datos (PostgreSQL & MySQL)

**Asignatura:** Arquitectura de Software

**Institución:** Pontificia Universidad Javeriana

**Sistema de Despliegue:** Windows (Docker Desktop con backend WSL 2) / Consola CMD - Bash

## 📌 1. Resumen de la Arquitectura

En este taller práctico se construyó una infraestructura contenida en Docker compuesta por tres servicios interconectados en una red bridge personalizada denominada **`red-fis`** (`172.25.0.0/16`). Cada motor de base de datos utiliza un **volumen persistente** para resguardar la información, y la interfaz web **CloudBeaver** se comunica con las bases de datos a través del DNS interno de Docker utilizando los nombres de los contenedores (`pg-fis` y `mysql-fis`).

### 📊 Especificaciones del Entorno

| Componente | Detalle / Nombre | Imagen / Versión | Puerto Publicado | Volumen / Red | 
| ----- | ----- | ----- | ----- | ----- | 
| **Red Privada** | `red-fis` | Driver `bridge` | N/A | Subnet `172.25.0.0/16` | 
| **PostgreSQL** | `pg-fis` | `postgres:18` | `5432:5432` | `pg_fis_data` | 
| **MySQL** | `mysql-fis` | `mysql:8.4` | `3306:3306` | `mysql_fis_data` | 
| **CloudBeaver** | `cloudbeaver-fis` | `dbeaver/cloudbeaver:latest` | `8978:8978` | `cb_fis_workspace` | 

## 🚀 2. Guía Paso a Paso para la Replicación del Entorno

### Paso 1: Creación de la Red Personalizada

Creamos la red aislada tipo *bridge* que permitirá la resolución de nombres por DNS interno entre contenedores:

```
docker network create red-fis

```

### Paso 2: Despliegue del Motor PostgreSQL (`pg-fis`)

Ejecutamos el contenedor con las variables de entorno de superusuario (`admin_fis`), nombre de base de datos (`fis_db`), mapeo de puertos y almacenamiento en volumen:

```
docker run -d --name pg-fis --network red-fis \
  -e POSTGRES_USER=admin_fis \
  -e POSTGRES_PASSWORD="Fis2026*Pg" \
  -e POSTGRES_DB=fis_db \
  -p 5432:5432 \
  -v pg_fis_data:/var/lib/postgresql \
  postgres:18

```

### Paso 3: Despliegue del Motor MySQL (`mysql-fis`)

Levantamos MySQL 8.4 configurando las credenciales de `root`, el usuario de aplicación (`app_fis`) con su clave respectiva y el volumen de persistencia:

```
docker run -d --name mysql-fis --network red-fis \
  -e MYSQL_ROOT_PASSWORD="Fis2026*My" \
  -e MYSQL_DATABASE=fis_db \
  -e MYSQL_USER=app_fis \
  -e MYSQL_PASSWORD="App2026*My" \
  -p 3306:3306 \
  -v mysql_fis_data:/var/lib/mysql \
  mysql:8.4

```

### Paso 4: Despliegue de la Interfaz CloudBeaver (`cloudbeaver-fis`)

Desplegamos el administrador de base de datos web expuesto en el puerto host `8978`:

```
docker run -d --name cloudbeaver-fis --network red-fis \
  -p 8978:8978 \
  -v cb_fis_workspace:/opt/cloudbeaver/workspace \
  dbeaver/cloudbeaver:latest

```

## 🧪 3. Evidencias de Ejecución y Validaciones SQL

### 📸 A. Estado de la Infraestructura en Docker

Verificación de los tres contenedores corriendo correctamente en estado `Up` y su inspección dentro de la red `red-fis`:

```
docker ps

```

```
docker network inspect red-fis

```

### 📸 B. Evidencias de Pruebas en PostgreSQL (`pg-fis`)

#### 1. Verificación de Roles y Privilegios en la Terminal (`psql`)

Conexión mediante `docker exec` comprobando el rol `admin_fis` con atributos de Superusuario:

```
docker exec -it pg-fis psql -U admin_fis -d fis_db

```

```
\du
SELECT rolname, rolsuper, rolcreatedb FROM pg_roles WHERE rolname = 'admin_fis';

```

#### 2. Ejecución SQL y Creación de Tabla en CloudBeaver

Creación de la tabla `curso`, inserción de registro y consulta de verificación:

```
CREATE TABLE curso (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(80)
);

INSERT INTO curso (nombre) VALUES ('Fundamentos de Ingeniería de Software');

SELECT * FROM curso;

```

### 📸 C. Evidencias de Pruebas en MySQL (`mysql-fis`)

#### 1. Verificación de Cuentas de Usuario y Hosts Habilitados

Validación de los usuarios `root` y `app_fis` configurados con el wildcard `%` para permitir conexiones desde la red de contenedores:

```
SELECT user, host FROM mysql.user;

```

#### 2. Ejecución SQL y Creación de Tabla en CloudBeaver

*(Nota: Para la conexión de MySQL en CloudBeaver fue necesario habilitar la propiedad `allowPublicKeyRetrieval=true` en los Driver Properties).*

```
USE fis_db;

CREATE TABLE curso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80)
);

INSERT INTO curso (nombre) VALUES ('Arquitectura de Software');

SELECT * FROM curso;

```

#### 3. Demostración de Alcances y Principio de Mínimo Privilegio (`SHOW DATABASES;`)

* **Vista con Usuario Superusuario (`root`):**
  Visibilidad total de la instancia incluyendo las bases del sistema (`mysql`, `sys`, `performance_schema`, etc.):
  

* **Vista con Usuario de Aplicación (`app_fis`):**
  Aislamiento completo de seguridad, mostrando únicamente la base autorizada (`fis_db`):
  

## ❓ 4. Análisis Teórico y Respuestas al Taller

### ¿Por qué CloudBeaver se conecta a `pg-fis:5432` y no a `localhost:5432`?

Cada contenedor Docker opera en su propio espacio de nombres de red aislado. Dentro del contenedor de CloudBeaver, `localhost` se refiere a la misma máquina virtual de CloudBeaver (donde no hay ningún motor de base de datos escuchando).

Al estar los contenedores agrupados en la red definida por el usuario `red-fis`, Docker activa un servidor **DNS interno** que resuelve el nombre del contenedor (`pg-fis` o `mysql-fis`) hacia su IP privada interna asignada (`172.25.0.2` y `172.25.0.3` respectivamente). El mapeo de puertos (`-p 5432:5432`) solo sirve para conectar el equipo host (anfitrión) con los contenedores, pero la comunicación interna dentro de la red Docker utiliza directamente los puertos nativos del contenedor.

### Alternativas si un contenedor no está en la red personalizada

Si CloudBeaver no perteneciera a `red-fis`, fallaría la resolución de nombres (`UnknownHostException`). Las soluciones serían:

1. Conectar manualmente el contenedor a la red:

   `docker network connect red-fis cloudbeaver-fis`

2. Conectarse a través del host anfitrión utilizando la dirección especial `host.docker.internal` y el puerto publicado.