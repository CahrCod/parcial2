# Guía de Docker para la Aplicación de Gestión de Inventarios

Este documento proporciona instrucciones para utilizar Docker con la aplicación de gestión de inventarios.

## Requisitos Previos

- Docker instalado en su sistema
- Docker Compose instalado en su sistema
- Git (opcional, para clonar el repositorio)

## Estructura de Archivos Docker

- `Dockerfile`: Define cómo se construye la imagen de la aplicación Spring Boot
- `docker-compose.yml`: Orquesta la aplicación y la base de datos MySQL

## Comandos Docker Básicos

### Construir y Ejecutar la Aplicación

Para iniciar toda la aplicación (servidor y base de datos):

```bash
docker-compose up -d
```

El parámetro `-d` ejecuta los contenedores en segundo plano.

### Ver Logs de la Aplicación

Para ver los logs de la aplicación:

```bash
docker-compose logs -f app
```

Para ver los logs de la base de datos:

```bash
docker-compose logs -f mysql
```

### Detener la Aplicación

Para detener todos los contenedores:

```bash
docker-compose down
```

Para detener y eliminar volúmenes (esto eliminará los datos de la base de datos):

```bash
docker-compose down -v
```

### Reconstruir la Aplicación

Si realiza cambios en el código y necesita reconstruir la imagen:

```bash
docker-compose build
docker-compose up -d
```

## Acceso a la Aplicación

- Aplicación Spring Boot: http://localhost:8088
- GraphQL Endpoint: http://localhost:8088/graphql
- GraphiQL Interface: http://localhost:8088/graphiql

## Acceso a la Base de Datos

- Host: localhost
- Puerto: 3306
- Base de datos: parcial2
- Usuario: root
- Contraseña: A03211026z_+*.

Para conectarse a la base de datos desde la línea de comandos:

```bash
docker exec -it parcial2-mysql mysql -uroot -pA03211026z_+*. parcial2
```

## Comandos Docker Útiles

### Ver Contenedores en Ejecución

```bash
docker ps
```

### Ver Todas las Imágenes

```bash
docker images
```

### Eliminar Contenedores e Imágenes No Utilizados

```bash
docker system prune
```

### Ver Uso de Recursos

```bash
docker stats
```

## Solución de Problemas

### La Aplicación No Se Conecta a la Base de Datos

Verifique que la base de datos esté en ejecución:

```bash
docker-compose ps
```

Verifique los logs de la base de datos:

```bash
docker-compose logs mysql
```

### Error al Construir la Imagen

Si hay errores al construir la imagen, intente limpiar el caché de Docker:

```bash
docker builder prune
```

Y luego reconstruya:

```bash
docker-compose build --no-cache
```

## Notas Adicionales

- Los datos de la base de datos se almacenan en un volumen Docker para persistencia
- La aplicación está configurada para reiniciarse automáticamente si se produce un error
- La base de datos está configurada para usar autenticación nativa de MySQL