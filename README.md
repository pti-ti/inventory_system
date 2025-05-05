# 💻 Inventario de Equipos de Cómputo

Aplicación web para gestionar un inventario de equipos de cómputo.  
Permite **registrar**, **actualizar**, **consultar**, **listar** y **eliminar** información sobre los equipos.

---

## 🚀 Tecnologías Utilizadas

- **Control de versiones**: Git
- **Base de datos**: PostgreSQL
- **Backend**: Java con Spring Boot

---

## 🧩 Plan de Desarrollo - Backend

### 1. Configuración del Control de Versiones

- Uso de Git y GitHub para control de código fuente y colaboración.

### 2. Configuración del Sistema de Base de Datos (PostgreSQL)

- Descargar e instalar:
  - PostgreSQL
  - PgAdmin

### 3. Configuración del Entorno de Desarrollo para Java

- Descargar e instalar:
  - Java Development Kit (JDK)
  - Apache Maven
  - Configurar variables de entorno (`JAVA_HOME`, `MAVEN_HOME`)
  - IDE IntelliJ IDEA (versión Community)

### 4. Creación del Proyecto

- Generado a través de [Spring Initializr](https://start.spring.io)

---

## 📦 Librerías Usadas en Spring Boot

- `Lombok`: para reducir código repetitivo (getters, setters, etc.)
- `Spring Web`: para construir APIs REST
- `PostgreSQL Driver`: conexión con la base de datos
- `Spring Security`: autenticación y autorización
- `MapStruct`: mapeo entre entidades y DTOs
- `Spring Boot DevTools`: recarga automática en desarrollo
- `JSON Web Token (JWT)`: autenticación basada en tokens

---

## 🧱 Diagrama de Arquitectura (Hexagonal)

              +---------------------+
              |    Controladores    | ← REST API
              |     (Adapters)      |
              +---------------------+
                        |
                        v
              +---------------------+
              |    application      | ← Casos de uso / servicios
              +---------------------+
                        |
                        v
              +---------------------+
              |      domain         | ← Entidades y puertos
              +---------------------+
                        |
                        v
              +---------------------+
              |   infrastructure    | ← Repositorios, configuración, seguridad
              +---------------------+

## 🗄️ Base de Datos Inicial

**Tipo**: Relacional (PostgreSQL)

| Parámetro              | Valor              |
|------------------------|--------------------|
| Nombre del servidor    | `docker-postgres`  |
| Usuario                | `postgres`         |
| Contraseña             | `root`             |
| Nombre de la BD        | `tech_inventory`   |
| Puerto postgresql      | `5432`   |

---

## 📁 Recursos Estáticos

La carpeta `static/` contiene archivos en formato Excel utilizados como plantillas para la gestión de documentación:

- 📝 **Bitácoras de uso**: registro del uso diario de los equipos.
- 🛠️ **Registros de mantenimiento**: historial y control de mantenimientos realizados.

Estos archivos pueden ser descargados y utilizados por los usuarios del sistema como referencia o para carga masiva de datos.


## 🐳 Docker

Este proyecto incluye un `Dockerfile` que permite construir y ejecutar la aplicación Spring Boot dentro de un contenedor.

### 🧱 Etapas del Dockerfile

El `Dockerfile` está dividido en dos etapas:

1. **Etapa de construcción (`build`)**
   - Imagen base: `maven:3.9.4-eclipse-temurin-21`
   - Copia el código fuente al contenedor (`COPY . .`)
   - Compila el proyecto con Maven (`mvn clean package -DskipTests`)
   - Genera el `.jar` dentro de `/app/target/`

2. **Etapa de ejecución (`runtime`)**
   - Imagen ligera: `eclipse-temurin:21-jdk-alpine`
   - Copia el `.jar` generado en la etapa anterior
   - Expone el puerto `8085`
   - Ejecuta el archivo `app.jar` con `java -jar`
  
Para levantar el contenedor de docker debes ingresar a la siguiente ruta C:\Users\Cristian\IdeaProjects 
Dentro de la carpeta 'IdeaProjects' abre el terminal y ejecuta el comando DOCKER-COMPOSE up --build -d
Este comando iniciará el contenedor que almacena el inventario y la base de datos .

