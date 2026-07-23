# Externalized Configuration - Guia de Implementacion

## Que es Externalized Configuration?

Externalized Configuration es un patron de diseño que consiste en separar la configuracion de la aplicacion del codigo fuente. Esto permite:

- Cambiar configuracion sin recompilar la aplicacion
- Usar diferentes valores por ambiente (local, dev, prod)
- Mantener secretos fuera del codigo fuente
- Cumplir con principios de 12-Factor App

## Archivos Implementados

### 1. `.env.example`

**Proposito:** Documentar todas las variables de entorno requeridas por la aplicacion.

**Contenido:**
```bash
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/mydb
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Server Configuration
SERVER_PORT=8080

# Logging Configuration
LOG_LEVEL=INFO
```

**Como ayuda al patron:**
- Sirve como referencia para nuevos desarrolladores
- Documenta que variables son necesarias
- Los valores reales van en `.env` (que no se sube al repositorio)
- Permite configurar cada ambiente de forma independiente

---

### 2. `application.yaml` (Base)

**Proposito:** Configuracion comun para todos los perfiles.

**Cambios realizados:**
```yaml
spring:
  application:
    name: microservice-template
  profiles:
    active: local

server:
  port: ${SERVER_PORT:8080}  # <-- Nuevo: puerto configurable

management:                  # <-- Nuevo: Actuator endpoints
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: when_authorized
```

**Como ayuda al patron:**
- `server.port: ${SERVER_PORT:8080}` permite cambiar el puerto via variable de entorno
- Los endpoints de Actuator exponen metricas de la aplicacion
- La configuracion base se hereda por todos los perfiles

---

### 3. `application-local.yaml`

**Proposito:** Configuracion para desarrollo local con H2 en memoria.

**Cambios realizados:**
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:localdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password: local
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop

logging:                    # <-- Nuevo
  level:
    root: INFO
    com.template: DEBUG

management:                 # <-- Nuevo
  endpoints:
    web:
      exposure:
        include: health,info
```

**Como ayuda al patron:**
- Usa H2 en memoria (no requiere DB externa)
- Logging con nivel DEBUG para desarrollo
- H2 console habilitada para inspeccionar datos
- Configuracion local no afecta otros ambientes

---

### 4. `application-dev.yaml`

**Proposito:** Configuracion para ambiente de desarrollo con PostgreSQL.

**Cambios realizados:**
```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/dev_db}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate

logging:                    # <-- Nuevo
  level:
    root: ${LOG_LEVEL:INFO}
    com.template: DEBUG
```

**Como ayuda al patron:**
- Variables de entorno con valores por defecto para desarrollo
- Permite cambiar DB sin modificar el archivo
- Logging configurable via variable de entorno
- `ddl-auto: validate`确保 esquema esta correcto

---

### 5. `application-prod.yaml`

**Proposito:** Configuracion para ambiente de produccion.

**Cambios realizados:**
```yaml
spring:
  datasource:
    url: ${DB_URL}              # <-- Sin valor por defecto (requerido)
    username: ${DB_USERNAME}    # <-- Sin valor por defecto (requerido)
    password: ${DB_PASSWORD}    # <-- Sin valor por defecto (requerido)
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate

logging:                    # <-- Nuevo
  level:
    root: ${LOG_LEVEL:WARN}
    com.template: INFO

management:                 # <-- Nuevo
  endpoints:
    web:
      exposure:
        include: health     # <-- Solo health en produccion
```

**Como ayuda al patron:**
- **No tiene valores por defecto** - fuerza que se configuren via variables de entorno
- Logging reducido (WARN/INFO) para no impactar rendimiento
- Solo expone endpoint de health (por seguridad)
- Asegura que produccion use la configuracion correcta

---

### 6. `pom.xml`

**Proposito:** Agregar dependencia de Actuator.

**Cambio realizado:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**Como ayuda al patron:**
- Actuator expone endpoints para monitoreo
- `/actuator/health` verifica que la app este funcionando
- `/actuator/info` muestra informacion de la aplicacion
- Permite integracion con herramientas de monitoreo (Prometheus, etc.)

---

### 7. `.gitignore`

**Proposito:** Evitar subir archivos sensibles al repositorio.

**Cambio realizado:**
```gitignore
### Environment Variables ###
.env
.env.local
.env.*
!.env.example
```

**Como ayuda al patron:**
- `.env` contiene valores reales (credenciales, etc.)
- `.env.example` si se sube (es documentacion)
- Previene fugas de seguridad
- Cada desarrollador tiene su propio `.env`

---

## Flujo de Configuracion

```
┌─────────────────────────────────────────────────────────────┐
│                    FLUJO DE CONFIGURACION                   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. Desarrollador crea .env (copiado de .env.example)       │
│     ↓                                                       │
│  2. Ejecuta: ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev  │
│     ↓                                                       │
│  3. Spring carga:                                           │
│     - application.yaml (base)                               │
│     - application-dev.yaml (override por perfil)            │
│     - Variables de entorno del .env                          │
│     ↓                                                       │
│  4. La aplicacion usa la configuracion final                │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## Resumen de Variables de Entorno

| Variable | Descripcion | Ejemplo |
|----------|-------------|---------|
| `DB_URL` | URL de conexion a la base de datos | `jdbc:postgresql://localhost:5432/mydb` |
| `DB_USERNAME` | Usuario de la base de datos | `postgres` |
| `DB_PASSWORD` | Contrasena de la base de datos | `secret123` |
| `SERVER_PORT` | Puerto del servidor | `8080` |
| `LOG_LEVEL` | Nivel de logging | `INFO`, `DEBUG`, `WARN` |

## Beneficios del Patron

1. **Seguridad:** Credenciales fuera del codigo fuente
2. **Flexibilidad:** Cambiar configuracion sin recompilar
3. **Consistencia:** Mismo codigo, diferentes configuraciones por ambiente
4. **Maintainability:** La configuracion esta centralizada y documentada
5. **Testing:** Fácil configurar ambientes de prueba
6. **Deploy:** Los mismos artefactos funcionan en cualquier ambiente

## Proximos Pasos (Opcional)

- [ ] Agregar Spring Cloud Config Server para configuracion centralizada
- [ ] Integrar con HashiCorp Vault para secretos
- [ ] Agregar métricas de Prometheus via Actuator
- [ ] Configurar Alertmanager para monitoreo
