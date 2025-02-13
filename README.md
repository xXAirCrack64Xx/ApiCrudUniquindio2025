# CRUD API for University

Esta API RESTful permite gestionar usuarios y trabajos en un entorno universitario. Está diseñada para que los estudiantes puedan enviar sus trabajos (por ejemplo, código) y los profesores puedan calificarlos. La aplicación se construyó con **Spring Boot**, **Maven** y utiliza **MySQL** como base de datos. Además, la API está documentada con **OpenAPI/Swagger**.

---

## Características

- **CRUD de Usuarios:**  
  Permite crear, leer, actualizar y eliminar usuarios (estudiantes y profesores).

- **Gestión de Trabajos:**  
  Los estudiantes pueden enviar trabajos, y los profesores pueden calificar dichos trabajos.

- **Documentación Interactiva:**  
  La API se documenta automáticamente con OpenAPI/Swagger.

- **Integración con MySQL:**  
  La persistencia de datos se realiza en una base de datos MySQL.

- **Seguridad Básica:**  
  Configuración de seguridad (con posibilidad de extender a autenticación JWT).

---

## Tecnologías Utilizadas

- **Java 17** (o la versión requerida)
- **Spring Boot**
- **Maven**
- **MySQL**
- **Spring Data JPA**
- **Springdoc OpenAPI (Swagger)**
- **Spring Security** (opcional)

---

## Instalación y Configuración

### 1. Clonar el Repositorio

Abre una terminal y ejecuta:

```bash
git clone https://github.com/alejandropastrana58152559/crud-api.git
cd crud-api
