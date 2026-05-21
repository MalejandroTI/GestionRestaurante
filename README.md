# GestiónRestaurante

Proyecto orientado al análisis, diseño y desarrollo de un sistema de gestión para restaurante. Su objetivo es administrar de forma eficiente los procesos principales del negocio, desde el registro de pedidos hasta la entrega al cliente, integrando base de datos, lógica de negocio y control de usuarios.

Dentro de este proyecto se desarrolla la estructura completa del sistema, comenzando con el modelado entidad-relación, transformación al modelo relacional, diseño de la base de datos e implementación progresiva hasta llegar a un producto funcional conectado mediante Java, MySQL y JDBC.

## Implementaciones
Diferencia entre Service y JPA Controllers

En este proyecto, los JPA Controllers se encargan del acceso directo a la base de datos utilizando JPA (EntityManager), implementando operaciones CRUD como crear, buscar, actualizar y eliminar entidades, sin incluir lógica de negocio.

Por otro lado, la capa Service contiene la lógica de negocio del sistema, coordinando procesos más complejos, validaciones y reglas del negocio, además de utilizar los JPA Controllers para persistir o consultar datos.

En resumen, los JPA Controllers gestionan la persistencia de datos, mientras que los Services gestionan la lógica y reglas del sistema, manteniendo una arquitectura más ordenada y escalable.

## Funcionalidades principales

* Gestión de usuarios, roles y permisos
* Registro y administración de clientes
* Gestión de productos y menú
* Creación y seguimiento de pedidos
* Tipos de pedido: local, retiro y delivery
* Control de estados del pedido
* Historial de seguimiento
* Facturación
* Configuración general del sistema

## Documentación incluida

En este repositorio se encontrará la documentación generada durante el desarrollo del proyecto:

* Modelo Entidad-Relación
* Modelo Relacional
* Diagramas del sistema
* Scripts de base de datos
* Avances de implementación

## Distribución del proyecto

```
documentos/          -> Diagramas y documentación
FacturasRestaurante/ -> Pdfs de facturas creadas
Java/                -> Java
SQL/                 -> Script sql
```

## Enlaces de acceso

**Draw.io (modo lector):**
https://drive.google.com/file/d/1Ki-i4osE0PLE_nQuk-tWaewi-Chf1X4S/view?usp=sharing

**Documentación del proyecto:**
https://docs.google.com/document/d/1NKTvYlMP2FAsKn9u2wyFZ5k3rMAdXIpAu67a3sHOD6g/edit?usp=sharing

## Tecnologías utilizadas

* Java
* MySQL
* JDBC
* Apache NetBeans IDE 17
* Draw.io

## Librerias

* itext5-5.5.12
* mysql-connector-j-8.0.32

## Estado del proyecto

En desarrollo. Actualmente se encuentra en fase de análisis, modelado y estructura de base de datos.

## Version 
1.0.0

## PROCESO DE DESARROLLO
