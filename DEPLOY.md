# Guía de Despliegue — VotacionBack

Este documento describe **todo lo necesario** para desplegar VotacionBack sin problemas, incluyendo los errores conocidos que ya ocurrieron y cómo resolverlos.

> **VotacionBack** es una aplicación Spring Boot que corre en el VM de backends (`10.43.100.131`) en el **puerto 8081**.

---

## Infraestructura del Sistema

| Servicio               | VM Producción    | Puerto |
|------------------------|------------------|--------|
| RegistraduriaFront     | `10.43.97.237`   | `8090` |
| VotacionFront          | `10.43.97.237`   | `4201` |
| RegistraduriaBack      | `10.43.100.131`  | `8080` |
| **VotacionBack**       | `10.43.100.131`  | `8081` |
| PostgreSQL             | `10.43.101.13`   | `5432` |
| CouchDB                | `10.43.101.13`   | `5984` |
| EtlVotacion            | `10.43.101.13`   | `8083` |

> El proceso ETL (extracción de votos de CouchDB → PostgreSQL) fue separado al microservicio **EtlVotacion** (`Vote4TechETLs`), que se despliega en la VM de bases de datos. Para disparar el ETL, ver el DEPLOY.md de ese repositorio.

---

## Ejecución Local

> Para probar el backend sin acceso a los VMs de producción.

### Opción A — Full Docker con `docker-compose.yml` raíz (recomendado)

El `docker-compose.yml` de la **raíz del workspace** levanta toda la infraestructura local:

```bash
# Desde la carpeta raíz ("Arquitectura de Software")
docker compose up -d
```

Este backend queda disponible en **`http://localhost:8081`**.

Las credenciales usadas por el compose raíz son:

```
DB_URL:               jdbc:postgresql://postgres:5432/vote4tech
DB_USER:              postgres
DB_PASSWORD:          postgres123
CORS_ALLOWED_ORIGINS: http://localhost:4200,http://localhost:4201
COUCHDB_URL:          http://couchdb:5984
COUCHDB_USER:         admin
COUCHDB_PASSWORD:     admin123
COUCHDB_DB_URNA:      votos_urna
COUCHDB_DB_DOMICILIO: votos_domicilio
```

> Distintas a producción. Spring Boot crea el schema automáticamente con `ddl-auto=update`. El seed puebla las tablas si están vacías.

**Primera vez — crear las bases de CouchDB** (solo si las creas manualmente, el compose no las crea solo):

```bash
curl -X PUT http://admin:admin123@localhost:5984/votos_urna
curl -X PUT http://admin:admin123@localhost:5984/votos_domicilio
# Verificar:
curl http://admin:admin123@localhost:5984/_all_dbs
```

Para ver los logs:

```bash
docker compose logs -f votacion-back
```

Para reconstruir (si hubo cambios de código Java o se agregó `SecurityConfig.java`):

```bash
docker compose up -d --build votacion-back
```

> ⚠ `SecurityConfig.java` debe existir en `src/` **antes** de ejecutar `--build`.
> El compose raíz hace `build: context: ./Vote4TechVotacionBack`, lo que significa que copia el `src/` local.
> Si el archivo no existe en tu máquina local, créalo antes de construir (ver sección siguiente).

---

### Opción B — `mvn spring-boot:run` directo (para desarrollo Java)

**Paso 1 — Levantar solo DBs:**

```bash
# Desde la carpeta raíz del workspace
docker compose up -d postgres couchdb
```

PostgreSQL: `localhost:5432`, DB: `vote4tech`, user: `postgres`, pass: `postgres123`.
CouchDB: `localhost:5984`, user: `admin`, pass: `admin123`.

Crear las bases de CouchDB si es la primera vez:

```bash
curl -X PUT http://admin:admin123@localhost:5984/votos_urna
curl -X PUT http://admin:admin123@localhost:5984/votos_domicilio
```

**Paso 2 — Verificar que `SecurityConfig.java` existe:**

```bash
ls src/main/java/PortalVotacionBack/config/SecurityConfig.java
```

Si no existe, crearlo (ver sección "Archivo Crítico" más adelante en este documento).

**Paso 3 — Exportar variables de entorno y arrancar:**

En Linux/Mac:

```bash
cd Vote4TechVotacionBack
export DB_URL="jdbc:postgresql://localhost:5432/vote4tech"
export DB_USER="postgres"
export DB_PASSWORD="postgres123"
export CORS_ALLOWED_ORIGINS="http://localhost:4201"
export COUCHDB_URL="http://localhost:5984"
export COUCHDB_USER="admin"
export COUCHDB_PASSWORD="admin123"
export COUCHDB_DB_URNA="votos_urna"
export COUCHDB_DB_DOMICILIO="votos_domicilio"
mvn spring-boot:run
```

En Windows (PowerShell):

```powershell
cd Vote4TechVotacionBack
$env:DB_URL = "jdbc:postgresql://localhost:5432/vote4tech"
$env:DB_USER = "postgres"
$env:DB_PASSWORD = "postgres123"
$env:CORS_ALLOWED_ORIGINS = "http://localhost:4201"
$env:COUCHDB_URL = "http://localhost:5984"
$env:COUCHDB_USER = "admin"
$env:COUCHDB_PASSWORD = "admin123"
$env:COUCHDB_DB_URNA = "votos_urna"
$env:COUCHDB_DB_DOMICILIO = "votos_domicilio"
mvn spring-boot:run
```

El backend arranca en `http://localhost:8081`.

**Verificar que arrancó correctamente:**

```bash
curl http://localhost:8081/eleccion/activas
# Debe devolver un array JSON
```

> Las variables de CouchDB tienen valores por defecto en `application.properties`
> (`:http://localhost:5984`, `:admin`, `:admin`, `:votos_urna`, `:votos_domicilio`),
> por lo que técnicamente son opcionales si CouchDB corre en localhost con esas credenciales.
> `DB_URL`, `DB_USER`, `DB_PASSWORD` y `CORS_ALLOWED_ORIGINS` **no tienen default** y son obligatorias.

---

## Archivo Crítico: `SecurityConfig.java`

> **Este archivo es obligatorio.** Sin él, Spring Boot activa su formulario de login por defecto y todas las peticiones POST al backend devuelven 302 (redirección a página de login HTML) en lugar de la respuesta JSON esperada. El frontend no puede autenticar usuarios.

**Ruta:** `src/main/java/PortalVotacionBack/config/SecurityConfig.java`

**Contenido completo:**

```java
package PortalVotacionBack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
```

**Verificar que existe antes de desplegar:**

```bash
ls ~/Vote4TechVotacionBack/src/main/java/PortalVotacionBack/config/SecurityConfig.java
```

Si no existe, crearlo con el contenido de arriba o copiarlo desde tu PC con `scp` (ver Paso 3).

---

## Variables de Entorno Críticas

| Variable | Descripción |
|----------|-------------|
| `SPRING_DATASOURCE_URL` | URL de PostgreSQL |
| `COUCHDB_URL` | URL de CouchDB para almacenar votos |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos — incluir IP y Cloudflare |
| `JWT_SECRET` | Clave para firmar JWT |

> **`CORS_ALLOWED_ORIGINS` debe incluir siempre:**
> 1. `http://10.43.97.237:4201` — acceso directo por IP
> 2. La URL de Cloudflare actual de VotacionFront

---

## Despliegue Completo (Primera Vez)

### Prerequisitos

- PostgreSQL corriendo en `10.43.101.13:5432`
- CouchDB corriendo en `10.43.101.13:5984`
- Acceso SSH al VM `10.43.100.131`

### Paso 1 — Limpiar contenedores anteriores (VM `10.43.100.131`)

```bash
docker rm -f vote4tech-votacion-back
```

### Paso 2 — Clonar el repositorio (VM `10.43.100.131`)

```bash
cd ~
git clone https://github.com/Zyntech-PUJ/Vote4TechVotacionBack.git
```

Si la carpeta ya existe:

```bash
cd ~/Vote4TechVotacionBack
git pull
```

### Paso 3 — Asegurar que `SecurityConfig.java` existe

**Opción A — Copiar desde tu PC (PowerShell):**

El archivo debe existir localmente en:
`C:\...\Vote4TechVotacionBack\src\main\java\PortalVotacionBack\config\SecurityConfig.java`

Si existe, copiarlo al VM:

```powershell
$base = "C:\Users\javie\OneDrive\Documentos\unijaveriana\SEMESTRE 7\Arquitectura de Software\Vote4TechVotacionBack"
scp "$base\src\main\java\PortalVotacionBack\config\SecurityConfig.java" estudiante@10.43.100.131:~/Vote4TechVotacionBack/src/main/java/PortalVotacionBack/config/SecurityConfig.java
```

**Opción B — Crear directamente en el VM:**

```bash
mkdir -p ~/Vote4TechVotacionBack/src/main/java/PortalVotacionBack/config
cat > ~/Vote4TechVotacionBack/src/main/java/PortalVotacionBack/config/SecurityConfig.java << 'EOF'
package PortalVotacionBack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
EOF
```

### Paso 4 — Configurar y copiar el docker-compose

**Desde tu PC (PowerShell)**, editar `Vote4TechVotacionBack\docker\docker-compose.prod.yml` con las variables correctas:

```yaml
environment:
  SPRING_DATASOURCE_URL: "jdbc:postgresql://10.43.101.13:5432/votacion"
  CORS_ALLOWED_ORIGINS: "http://10.43.97.237:4201,https://TU_URL_CLOUDFLARE.trycloudflare.com"
```

> La URL de Cloudflare de VotacionFront se obtiene con:
> ```bash
> docker logs vote4tech-votacion-cloudflared 2>&1 | grep trycloudflare
> # (en el VM 10.43.97.237)
> ```

Subir al VM:

```powershell
$base = "C:\Users\javie\OneDrive\Documentos\unijaveriana\SEMESTRE 7\Arquitectura de Software\Vote4TechVotacionBack"
scp "$base\docker\docker-compose.prod.yml" estudiante@10.43.100.131:~/Vote4TechVotacionBack/docker/docker-compose.prod.yml
```

### Paso 5 — Construir y levantar (VM `10.43.100.131`)

```bash
cd ~/Vote4TechVotacionBack
docker compose -f docker/docker-compose.prod.yml up -d --build
```

> **`--build` es obligatorio** — VotacionBack es Java y necesita compilarse con Maven dentro del contenedor.

Ver progreso:

```bash
docker logs -f vote4tech-votacion-back
```

Esperar:
```
Started VotacionBackApplication in X.XXX seconds
```

Verificar:

```bash
docker ps | grep votacion-back
curl http://localhost:8081/eleccion/activa
```

---

## Actualizar solo Variables de Entorno (sin recompilar)

Cuando solo cambia `CORS_ALLOWED_ORIGINS` u otra variable:

1. Editar `docker-compose.prod.yml` localmente
2. Subir con `scp`
3. En el VM:

```bash
cd ~/Vote4TechVotacionBack
docker compose -f docker/docker-compose.prod.yml up -d
```

> **Sin `--build`** — solo recrea el contenedor con los nuevos env vars.

---

## Actualizar la URL de Cloudflare en CORS (flujo habitual)

1. **Obtener la nueva URL** (VM `10.43.97.237`):
   ```bash
   docker logs vote4tech-votacion-cloudflared 2>&1 | grep trycloudflare
   ```

2. **Editar localmente** `Vote4TechVotacionBack\docker\docker-compose.prod.yml`:
   ```yaml
   CORS_ALLOWED_ORIGINS: "http://10.43.97.237:4201,https://NUEVA_URL.trycloudflare.com"
   ```

3. **Subir al VM** (PowerShell local):
   ```powershell
   $base = "C:\Users\javie\OneDrive\Documentos\unijaveriana\SEMESTRE 7\Arquitectura de Software\Vote4TechVotacionBack"
   scp "$base\docker\docker-compose.prod.yml" estudiante@10.43.100.131:~/Vote4TechVotacionBack/docker/docker-compose.prod.yml
   ```

4. **Reiniciar** (VM `10.43.100.131`):
   ```bash
   cd ~/Vote4TechVotacionBack
   docker compose -f docker/docker-compose.prod.yml up -d
   ```

---

## Problemas Conocidos y Soluciones

### Problema: Login devuelve 302 o HTML en lugar de JWT

**Síntoma:** POST a `/login` devuelve código 302 con `Location: /login?error` o devuelve HTML de una página de formulario.

**Causa:** Spring Boot Spring Security activa su formulario de login por defecto cuando hay dependencia de `spring-boot-starter-security` en el `pom.xml`. Cualquier petición no autenticada a un endpoint protegido redirige al formulario.

**Diagnóstico:**

```bash
curl -v -X POST http://localhost:8081/api/login \
  -H "Content-Type: application/json" \
  -d '{"usuario":"test","password":"test"}'
# Si el response tiene "Location: /login" o es HTML, falta SecurityConfig.java
```

**Solución:** Crear el archivo `SecurityConfig.java` con el contenido del inicio de este documento (ver sección "Archivo Crítico"). Luego reconstruir:

```bash
cd ~/Vote4TechVotacionBack
docker compose -f docker/docker-compose.prod.yml up -d --build
```

---

### Problema: Error CORS en el navegador

**Síntoma:** `Access to XMLHttpRequest ... has been blocked by CORS policy`.

**Causa:** La URL de Cloudflare de VotacionFront cambió y no está en `CORS_ALLOWED_ORIGINS`.

**Solución:** Seguir el flujo "Actualizar la URL de Cloudflare en CORS" descrito arriba.

---

### Problema: Contenedor reinicia en bucle

**Diagnóstico:**

```bash
docker logs vote4tech-votacion-back --tail=30
```

**Causas comunes:**
- No puede conectar a PostgreSQL o CouchDB
- `SecurityConfig.java` tiene error de compilación (falta import, typo)
- Variable de entorno mal configurada

```bash
# Verificar conectividad
curl -v telnet://10.43.101.13:5432
curl -v telnet://10.43.101.13:5984
```

---

### Problema: `git pull` borra el `SecurityConfig.java`

**Causa:** Si `SecurityConfig.java` fue creado directamente en el VM y no está en el repositorio, `git pull` no lo toca. Sin embargo, si hay un merge que modifica el directorio `config/`, puede haber conflictos.

**Prevención:** Verificar que `SecurityConfig.java` existe **después** de cada `git pull`:

```bash
ls ~/Vote4TechVotacionBack/src/main/java/PortalVotacionBack/config/
```

Si no aparece, recrearlo (ver Paso 3 de este documento).

---

### Problema: Cambios de código Java no se reflejan

**Causa:** Se ejecutó `docker compose up -d` sin `--build`. Docker usó la imagen anterior.

**Solución:**

```bash
docker compose -f docker/docker-compose.prod.yml up -d --build
```

Si sigue sin reflejar cambios, forzar reconstrucción sin caché:

```bash
docker compose -f docker/docker-compose.prod.yml build --no-cache
docker compose -f docker/docker-compose.prod.yml up -d
```

---

### Problema: Puerto 8081 ya está en uso

```bash
sudo lsof -i :8081
sudo fuser -k 8081/tcp
```

---

## Comandos de Diagnóstico Rápido

```bash
# Ver todos los contenedores
docker ps

# Ver logs del backend
docker logs vote4tech-votacion-back --tail=50

# Ver logs en tiempo real
docker logs -f vote4tech-votacion-back

# Test rápido del endpoint (sin auth)
curl http://localhost:8081/eleccion/activa

# Test del login
curl -X POST http://localhost:8081/ciudadano/login \
  -H "Content-Type: application/json" \
  -d '{"cedula":"12345","pin":"1234"}'

# Verificar que SecurityConfig.java existe
ls ~/Vote4TechVotacionBack/src/main/java/PortalVotacionBack/config/

# Ver variables de entorno del contenedor
docker inspect vote4tech-votacion-back | grep -A 30 '"Env"'

# Reconstruir desde cero
docker compose -f docker/docker-compose.prod.yml down
docker compose -f docker/docker-compose.prod.yml up -d --build
```

---

## Cómo Funciona el Build Docker (multi-stage)

El `Dockerfile` tiene **2 etapas**. La primera compila el proyecto Java; la segunda crea la imagen final mínima.

```
┌─────────────────────────────────────────────────────────────┐
│  ETAPA 1: builder  (imagen maven:3.9-eclipse-temurin-21)    │
│                                                             │
│  1. COPY pom.xml .                                          │
│  2. RUN mvn dependency:go-offline  ← descarga deps Maven   │
│     (cacheado si pom.xml no cambió)                         │
│  3. COPY src ./src                 ← copia el código Java   │
│     (incluye SecurityConfig.java si existe)                 │
│  4. RUN mvn package -DskipTests    ← compila el .jar        │
│     (si SecurityConfig.java tiene error, FALLA AQUÍ)        │
│                                                             │
│  Resultado: /app/target/PortalVotacionBack-0.0.1-SNAPSHOT.jar │
└──────────────────────┬──────────────────────────────────────┘
                       │ solo se copia el .jar compilado
┌──────────────────────▼──────────────────────────────────────┐
│  ETAPA 2: production  (imagen eclipse-temurin:21-jre-alpine) │
│                                                             │
│  1. COPY app.jar .                                          │
│  2. EXPOSE 8081                                             │
│  3. ENTRYPOINT ["java", "-jar", "app.jar"]                  │
│                                                             │
│  Imagen final: ~200 MB (solo JRE + .jar)                    │
└─────────────────────────────────────────────────────────────┘
```

**Punto crítico para VotacionBack:** `SecurityConfig.java` debe existir en `src/` **antes** del `COPY src ./src` del Dockerfile. Si no está, Maven compila sin él → Spring Security activa el form login → 302 en todos los POST.

**¿Cuándo es obligatorio `--build`?**
- Cualquier cambio en archivos `.java` (incluyendo agregar `SecurityConfig.java`)
- Cualquier cambio en `pom.xml`

**¿Cuándo NO hace falta `--build`?**
- Cuando solo cambian variables de entorno en `docker-compose.prod.yml`
- `docker compose up -d` recrea el contenedor con los nuevos env vars sin recompilar

**Si el build falla con error de Maven:**

```bash
# Ver el error completo del build
docker compose -f docker/docker-compose.prod.yml build 2>&1 | tail -50
# El error aparece en la sección "ETAPA 1: builder"
# Errores comunes:
# - "package PortalVotacionBack.config does not exist" → falta un import en SecurityConfig.java
# - "cannot find symbol" → typo en el nombre de clase o método
```

---

## Gestión de Imágenes Docker

```bash
# Ver todas las imágenes locales
docker images

# Ver solo la imagen de este proyecto
docker images | grep vote4tech-votacion

# Eliminar imágenes intermedias sin usar
docker image prune

# Ver cuánto espacio usa Docker
docker system df

# Limpieza completa (cuidado: elimina caché de build)
docker system prune
```

Flujo cuando el VM se queda sin espacio:

```bash
# Verificar espacio
df -h

# Ver desglose de Docker
docker system df -v

# Liberar solo capas intermedias (seguro)
docker image prune -f
```

> La imagen `maven:3.9-eclipse-temurin-21` es la más pesada (~500 MB). Si el disco lo permite, dejarla en caché para que futuros `--build` no descarguen todo desde cero.

---

## Diferencias Importantes vs. RegistraduriaBack

| Aspecto | RegistraduriaBack | VotacionBack |
|---------|------------------|--------------|
| Puerto | 8080 | 8081 |
| Base de datos | Solo PostgreSQL | PostgreSQL + CouchDB |
| SecurityConfig | No fue necesario fix | **Obligatorio** — Spring redirigía a login |
| Frontend que consume | RegistraduriaFront (8090) | VotacionFront (4201) |
| CORS origen IP | `http://10.43.97.237:8090` | `http://10.43.97.237:4201` |
