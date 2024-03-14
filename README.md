# **Hibernate & Java Persistence API (JPA) CurseForge**

## Descripción

Este proyecto implementa un sistema de gestión de base de datos utilizando Hibernate y JPA (Java Persistence API) para almacenar y gestionar información sobre juegos, mods, detalles y categorías relacionadas con el mundo de los videojuegos. Proporciona una interfaz de línea de comandos para realizar operaciones CRUD (crear, leer, actualizar, eliminar) en las entidades mencionadas.

> [!NOTE]
> ## Requisitos
> - Java Development Kit (JDK) 8 o superior.
> - Un servidor de base de datos compatible (por ejemplo, MySQL, PostgreSQL).

## Configuración

1. Clona o descarga el repositorio de GitHub.
2. Configura la conexión a tu base de datos editando el archivo de configuración de Hibernate.
3. Asegúrate de tener las dependencias necesarias para Hibernate y JPA en tu proyecto.

## Uso

### Ejecución del Menú

1. Abre una terminal o línea de comandos.
2. Navega al directorio donde se encuentra el archivo `PROJECTMenu.java`.
3. Compila el código Java utilizando el comando `javac PROJECTMenu.java`.
4. Ejecuta el programa compilado con el comando `java PROJECTMenu`.

### Funcionalidades Disponibles

El menú de la aplicación ofrece las siguientes funcionalidades:

1. **Crear Tablas**: Crea las tablas necesarias en la base de datos si aún no existen.
2. **Borrar Tablas**: Elimina todas las tablas de la base de datos.
3. **Poblar Masivamente**: Inserta datos masivamente desde un archivo XML en la base de datos.
4. **Seleccionar con Texto**: Busca registros que contengan un texto específico en una tabla seleccionada.
5. **Seleccionar Elementos Por Condición**: Selecciona registros de una tabla que cumplan una condición específica.
6. **Seleccionar Elementos Concretos**: Selecciona un registro específico de una tabla por su ID.
7. **Modificar Registro**: Modifica un registro específico de una tabla por su ID.
8. **Modificar Registros**: Modifica múltiples registros de una tabla que cumplan una condición específica.
9. **Eliminar Registro**: Elimina un registro específico de una tabla por su ID.
10. **Eliminar Registros**: Elimina múltiples registros de una tabla que cumplan una condición específica.

> [!WARNING]
> ### Advertencia
>- **¡Precaución al eliminar registros!**: La eliminación de registros es permanente y no se puede deshacer. Asegúrate de revisar cuidadosamente los registros que deseas eliminar antes de confirmar la operación.

## Autor

- [Hammad J M C](https://github.com/hammad2003)