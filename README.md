# The Spot

The Spot es una aplicación para consultar lugares y compartir reseñas. Tiene un sitio para clientes y un panel de administración.

## Qué se necesita

Java 21 o compatible y MySQL 8. Para probar las peticiones de la API se usa Postman. El proyecto ya incluye Maven Wrapper. La primera ejecución necesita internet para descargar las dependencias.

## Cómo ejecutar el proyecto

1. Clonar el repositorio y abrir la carpeta red-social-proyecto-final. En IntelliJ se puede abrir el pom.xml como proyecto Maven.
2. Iniciar MySQL y crear la base de datos. Las tablas se crean al arrancar la aplicación.

   ~~~sql
   CREATE DATABASE IF NOT EXISTS thespot
     CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
   ~~~

3. Crear un archivo application-local.properties junto a pom.xml. Poner el usuario y la contraseña de la instalación local de MySQL.

   ~~~properties
   spring.datasource.url=jdbc:mysql://localhost:3306/thespot
   spring.datasource.username=root
   spring.datasource.password=CONTRASEÑA_DE_MYSQL
   JWT_SECRET=CLAVE_BASE64
   ~~~

4. Generar la clave en PowerShell y copiar el resultado como valor de JWT_SECRET. El archivo de configuración local está excluido de Git.

   ~~~powershell
   $bytes = New-Object byte[] 32
   $generador = [System.Security.Cryptography.RandomNumberGenerator]::Create()
   $generador.GetBytes($bytes)
   [Convert]::ToBase64String($bytes)
   ~~~

5. Desde la carpeta red-social-proyecto-final, ejecutar:

   ~~~powershell
   .\mvnw.cmd spring-boot:run
   ~~~

En IntelliJ también se puede ejecutar la clase RedSocialProyectoFinalApplication. El directorio de trabajo debe ser red-social-proyecto-final para que se lea el archivo de configuración local.

## Cómo revisar el avance

- Entrar a http://localhost:8080/registro y crear una cuenta con un usuario y un correo nuevos.
- Entrar a http://localhost:8080/login con esa cuenta. Si las credenciales son correctas, se abre Inicio.
- Entrar a http://localhost:8080/catalogo para probar la búsqueda, la categoría y el orden. Si la base está vacía, entrar al panel, crear una categoría y guardar un lugar con foto. Al volver al catálogo debe aparecer su tarjeta con la imagen.

Las cuentas de prueba se crean al arrancar si no existen y está habilitada la carga de datos de prueba.

- Panel en http://localhost:8080/admin/login. Usuario admin y contraseña Admin123!
- Cliente en http://localhost:8080/login. Usuario regiiniita y contraseña Usuario123!

## Peticiones para Postman

Usar http://localhost:8080 como dirección del servidor.

- POST /api/usuarios recibe username, email y password en JSON. Devuelve 201 con id, username y email.
- POST /api/sesiones recibe username y password en JSON. Devuelve 200 con el token si las credenciales son correctas.
- GET /api/lugares devuelve la lista en JSON. Se puede probar con query para buscar, categoria para filtrar y orden para ordenar. Los valores de orden son recientes, mejor_valorado y mas_resenados. Escribir la categoría con el nombre que tiene en MySQL, incluidas las tildes.

Para los POST, hacer primero un GET a /registro desde Postman. Buscar el campo oculto _csrf en el HTML y enviar su valor en el encabezado X-CSRF-TOKEN. Postman debe conservar la cookie JSESSIONID. Para el catálogo basta un GET sin body. La pantalla usa el parámetro vista=catalogo para recibir HTML. En Postman se omite ese parámetro para recibir JSON.

## Sobre la entrega

Cada petición corresponde a una acción del sitio. Los datos de entrada se validan con Bean Validation y las respuestas se devuelven con DTOs. El inicio de sesión ya genera el JWT. Queda pendiente validar ese token en las rutas protegidas y unificar los errores de la API en JSON.

El avance pide integrar el código en main y entregar una colección de Postman en JSON, organizada por módulo, con variables para la dirección del servidor y el token.

## Tecnologías utilizadas

Java, Spring Boot, Spring MVC, Thymeleaf, Spring Security, BCrypt, JWT, Spring Data JPA, Hibernate, MySQL, Bean Validation, Maven y Lombok.
