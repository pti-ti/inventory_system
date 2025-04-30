# 💻 Inventario de Equipos de Cómputo

Aplicación web para gestionar un inventario de equipos de cómputo.  
Permite **registrar**, **actualizar**, **consultar** y **eliminar** información sobre los equipos.

---

## 🚀 Tecnologías Utilizadas

- **Control de versiones**: Git
- **Base de datos**: PostgreSQL
- **Backend**: Java con Spring Boot
- **Frontend**: JavaScript, HTML, CSS, React

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

## 🗄️ Base de Datos Inicial

**Tipo**: Relacional (PostgreSQL)

| Parámetro              | Valor              |
|------------------------|--------------------|
| Nombre del servidor    | `docker-postgres`  |
| Usuario                | `postgres`         |
| Contraseña             | `root`             |
| Nombre de la BD        | `tech_inventory`   |

---

✅ *Próximas secciones recomendadas:*
- Documentación de endpoints (Swagger/OpenAPI)
- Instrucciones para ejecución local y despliegue
- Manual de usuario y administrador
- Diagrama de arquitectura (puedo ayudarte a generarlo)


 
 - Creación del proyecto Usando Spring Initialzr agregando librerias necesarias (SPRING BOOT).
 - Estructura del proyecto usando arquitectura hexagonal.
   Estructura de paquetes del  
 - Conexión a la base de datos PostgreSQL.
 - Creación de las clases modelos de la base de datos.
 - Creación de las interfaces de los repositorios"
 - Creación de los puertos (comunicadores con la capa de infraestructura)
 - Creación de las clases de persistencia.
 - Creación de las entidades (Entities)
 - Creación de los adaptadores (interfaces de la implementación). 
 - Creación de los Mappers
 - Creación de las clases de la persistencia JPA y su implementación 
 - Implementación del backend (Java con Spring boot).
 - Desarrollo del frontend  (React- JavaScript).
 - Pruebas y despliegue. 
