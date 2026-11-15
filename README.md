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

JWT_SECRET se usa para firmar y también para validar el token de las rutas protegidas. Sin esa línea la aplicación no arranca y muestra Could not resolve placeholder 'JWT_SECRET'. El archivo debe llamarse exactamente application-local.properties y estar junto a pom.xml. En Windows, comprobar que no se guardó como application-local.properties.txt.

## Cómo revisar el avance

- Entrar a http://localhost:8080/registro y crear una cuenta con un usuario y un correo nuevos.
- Entrar a http://localhost:8080/login con esa cuenta. Si las credenciales son correctas, se abre Inicio.
- Entrar a http://localhost:8080/catalogo para probar la búsqueda, la categoría y el orden. Si la base está vacía, entrar al panel, crear una categoría y guardar un lugar con foto. Al volver al catálogo debe aparecer su tarjeta con la imagen.

Las cuentas de prueba se crean al arrancar si no existen y está habilitada la carga de datos de prueba.

- Panel en http://localhost:8080/admin/login. Usuario admin y contraseña Admin123!
- Cliente en http://localhost:8080/login. Usuario regiiniita y contraseña Usuario123!
- También se crean Yisus0714 (activo) y rams3s (inactivo), con la misma contraseña Usuario123!. Una cuenta inactiva no puede iniciar sesión ni ser seguida.

## Peticiones para Postman

Usar http://localhost:8080 como dirección del servidor.

- POST /api/usuarios recibe username, email y password en JSON. Devuelve 201 con id, username y email.
- POST /api/sesiones recibe username y password en JSON. Devuelve 200 con el token si las credenciales son correctas.
- GET /api/lugares devuelve la lista en JSON. Se puede probar con query para buscar, categoria para filtrar y orden para ordenar. Los valores de orden son recientes, mejor_valorado y mas_resenados. Escribir la categoría con el nombre que tiene en MySQL, incluidas las tildes.
- GET /api/usuarios/{username}/seguidos devuelve en JSON la lista de usuarios que sigue {username}. Cada uno trae id, username y nombre. No necesita token. Devuelve 200.
- PUT /api/usuarios/me/seguidos/{username} hace que el usuario del token empiece a seguir a {username}. Necesita token. Devuelve 204 sin cuerpo. Si ya lo seguía no da error ni duplica el seguimiento.
- DELETE /api/usuarios/me/seguidos/{username} hace que el usuario del token deje de seguir a {username}. Necesita token. Devuelve 204 sin cuerpo. Si no lo seguía no da error.

Para los POST, hacer primero un GET a /registro desde Postman. Buscar el campo oculto _csrf en el HTML y enviar su valor en el encabezado X-CSRF-TOKEN. Postman debe conservar la cookie JSESSIONID. Para el catálogo basta un GET sin body. La pantalla usa el parámetro vista=catalogo para recibir HTML. En Postman se omite ese parámetro para recibir JSON.

Ese procedimiento con _csrf solo aplica a los formularios del sitio. Las rutas /api/** están exentas de CSRF porque la API trabaja con JWT y no con sesión, así que para las peticiones de la API no hace falta el GET a /registro ni el encabezado X-CSRF-TOKEN.

Las rutas /api/usuarios/me/** necesitan el token que devuelve POST /api/sesiones, enviado en el encabezado Authorization con el formato Bearer TOKEN. Sin token, o con un token inválido o vencido, responden 401. El token dura 24 horas.

Reglas del seguimiento:

- Un usuario no puede seguirse a sí mismo.
- Solo se puede seguir a cuentas que existen y están activas.
- Un usuario solo puede seguir una vez a la misma cuenta. Repetir el PUT o el DELETE no produce error.
- Los seguimientos se guardan en la tabla seguimientos, que se crea sola al arrancar la aplicación.
- Si el usuario no existe o se intenta seguir a uno mismo, la petición falla y por ahora responde 500, porque unificar los errores de la API en JSON sigue pendiente.

La colección de Postman está en la carpeta postman/ del repositorio. Trae dos archivos: The Spot API.postman_collection.json, organizada por módulos (Registro y acceso, Perfil y configuración y Seguimiento), y The Spot.postman_environment.json, que es el entorno The Spot con las variables baseUrl (http://localhost:8080) y token. Para usarla:

1. Hacer pull del repositorio para tener la carpeta postman/.
2. En Postman, pulsar Import y arrastrar los dos archivos. En la ventana de importación dejar marcados los dos, la colección y el entorno, y pulsar Import.
3. Elegir el entorno The Spot arriba a la derecha. Sin entorno, {{baseUrl}} y {{token}} no tienen valor y las peticiones fallan.
4. Con la aplicación corriendo, enviar Registro y acceso → Iniciar Sesión con regiiniita y Usuario123!. La respuesta trae el token y la petición lo guarda sola en el entorno. Las demás peticiones lo usan automáticamente.

Para probar el seguimiento con los datos de prueba, en este orden:

1. Iniciar Sesión con regiiniita.
2. Seguimiento → Seguir usuario. Sigue a Yisus0714 y debe responder 204.
3. Seguimiento → Listar seguidos. Debe devolver 200 con Yisus0714 en la lista.
4. Seguimiento → Dejar de seguir usuario. Debe responder 204.
5. Seguimiento → Listar seguidos otra vez. La lista debe salir vacía.

Cada petición de la colección trae pruebas que se ven en la pestaña Test Results. Si Seguir usuario o Dejar de seguir usuario responden 401, el token está vacío o venció: volver a enviar Iniciar Sesión. Si Postman muestra error de conexión, comprobar que la aplicación está corriendo y que el entorno The Spot está elegido.

Para agregar un módulo o guardar cambios en la colección: crear una carpeta por módulo con sus peticiones y, en las que requieren sesión, usar la autenticación Bearer Token con el valor {{token}}. Al terminar, usar Export → Collection v2.1, reemplazar el archivo dentro de postman/ y hacer commit y push. Cada persona que importa tiene su propia copia, así que si dos editan al mismo tiempo el último archivo que se sube pisa al otro. Conviene pasarla por turnos: hacer pull, borrar la colección anterior de su Postman, importar la nueva, agregar lo suyo, exportar, subir y avisar a la siguiente persona.

## Sobre la entrega

Cada petición corresponde a una acción del sitio. Los datos de entrada se validan con Bean Validation y las respuestas se devuelven con DTOs. El inicio de sesión ya genera el JWT. Queda pendiente validar ese token en las rutas protegidas y unificar los errores de la API en JSON.

Avance posterior: el token ya se valida en las rutas /api/usuarios/me/**, y sin token responden 401. La unificación de los errores de la API en JSON sigue pendiente.

El avance pide integrar el código en main y entregar una colección de Postman en JSON, organizada por módulo, con variables para la dirección del servidor y el token.

La colección entregada está en postman/ y tiene los módulos Registro y acceso, Perfil y configuración y Seguimiento, y las variables baseUrl y token están en el entorno The Spot.

## Tecnologías utilizadas

Java, Spring Boot, Spring MVC, Thymeleaf, Spring Security, BCrypt, JWT, Spring Data JPA, Hibernate, MySQL, Bean Validation, Maven y Lombok.