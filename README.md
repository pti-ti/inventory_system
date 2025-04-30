# Inventario de Equipos de Cómputo 

Este proyecto es una aplicación web para gestionar un inventario de equipos de cómputo.
Permite registrar, actualizar, consultar y eliminar información sobre los equipos.

## Tecnologías 
 - Git
 - Base de datos (PostgreSQL) 
 - Backend (Java con Spring boot)
 - Frontend (JavaScript,Css, Html, React).
 
## Plan de desarrollo Backend
 - Configuración del control de versiones con Git y Github.
 - Configuración del sistema para SQL
	a. Descarga PostgreSQL y PgAdmin

 - Configuración del entorno de desarrollo para Java

	a. Descarga del JDK(Java development kit)
     
	b. Descarga Maven
	
	c. Variables de entorno.

	d. Instalación IDE intellij versión community.

 - Creación del proyecto en Spring initializr

 ##  Librerias usadas en spring

-Lombok
 	- Spring Web
  
  	- PostgreSQL Driver
   
   	- Spring Security
    
    	- MapStruct 
     
     	- Spring Boot DevTools
      
      	- Json web token(JWT) 

 ##  Creación de la base de datos inicial (RELACIONAL)
 - Nombre del servidor : docker-postgres
 - Username del servidor : postgres
 - Contraseña del servidor : root
 - Nombre de la base de datos : tech_inventory
 
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
