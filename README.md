# BankApp Desktop 🏦

Aplicación de banca digital de escritorio construida con **Java**, **JavaFX** y **SQLite**. Sistema completo de autenticación con hashing seguro, gestión de cuentas, depósitos, retiros y transferencias entre cuentas. Proyecto enfocado en aprender arquitectura profesional antes de avanzar con Spring Boot.

## 📋 Descripción del Proyecto

Sistema bancario con interfaz gráfica que simula operaciones bancarias reales. Implementa patrones profesionales de desarrollo como **MVC**, **Repository Pattern**, **Service Layer** y **DTO Pattern**. Destaca por ser un puente entre aplicaciones simples con persistencia en archivos JSON y frameworks empresariales como Spring Boot.

### Objetivos de Aprendizaje 🎓

Este proyecto fue diseñado como paso previo a Spring Boot para dominar:
- ✅ **Conexión a bases de datos** con JDBC
- ✅ **Hashing seguro** de contraseñas (SHA-256)
- ✅ **Transacciones ACID** en bases de datos
- ✅ **Patrón Repository** para acceso a datos
- ✅ **Patrón Service** para lógica de negocio
- ✅ **DTOs** para transferencia de datos
- ✅ **Inyección de dependencias** manual (base para @Autowired en Spring)
- ✅ **Singleton Pattern** para conexión de BD
- ✅ **Validaciones robustas** con excepciones personalizadas

## ✨ Características Principales

### Autenticación y Autorización
- **Login seguro** con SHA-256 hashing
- **Roles de usuario**: Cliente y Admin
- **Validación de contraseña** fuerte (8+ caracteres, mayúscula, minúscula, número, carácter especial)
- **Email único** en el sistema

### Gestión de Cuentas
- Alias personalizado para cada cuenta
- Número de cuenta único (UUID)
- Saldo con precisión de decimales (BigDecimal)
- Historial de transacciones

### Operaciones Bancarias
- **Depósitos**: Agregar dinero a la cuenta
- **Retiros**: Extraer dinero con validación de saldo
- **Transferencias**: Entre cuentas con transacción ACID
- **Historial**: Registro completo de movimientos

### Panel Admin
- Visualizar todos los clientes y sus cuentas
- Monitoreo de transacciones
- Gestión de usuarios

## 🛠️ Herramientas y Tecnologías

| Herramienta | Versión | Propósito |
|------------|---------|----------|
| **Java** | 11+ | Lenguaje principal |
| **JavaFX** | 21+ | Interfaz gráfica |
| **SQLite** | 3.x | Base de datos local |
| **JDBC** | Java.sql.* | Conexión a BD |
| **BigDecimal** | Java.math | Precisión monetaria |
| **UUID** | Java.util | Identificadores únicos |
| **MessageDigest** | Java.security | Hashing SHA-256 |
| **ZonedDateTime** | Java.time | Gestión de fechas |

## 🏗️ Arquitectura del Proyecto

### Patrón MVC + Capas

```
┌─────────────────────────────────────────────────┐
│              VISTA (JavaFX - FXML)              │
│  (Login, DashboardCliente, DashboardAdmin)      │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│           CONTROLADORES (Controllers)           │
│  (LoginController, DashboardClienteController)  │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│          SERVICIOS (Service Layer)              │
│  (UsuarioService, CuentaService, etc)           │
│  - Lógica de negocio                            │
│  - Validaciones                                 │
│  - Orquestación de datos                        │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│       REPOSITORIO (Repository Pattern)          │
│  (UsuarioRepository, CuentaRepository, etc)     │
│  - Acceso a datos                               │
│  - Queries SQL                                  │
│  - Mapeo de ResultSet a objetos                 │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│     BASE DE DATOS (SQLite - banco.db)           │
│  (Tablas: usuarios, cuentas, transacciones)     │
└─────────────────────────────────────────────────┘
```

### Estructura de Carpetas

```
src/
├── controller/                          # Controladores JavaFX
│   ├── LoginController.java            # Autenticación
│   ├── RegistroController.java         # Registro de clientes
│   ├── DashboardClienteController.java # Panel del cliente
│   ├── DashboardAdminController.java   # Panel del admin
│   └── ConfiguracionController.java    # Configuración
│
├── service/                            # Lógica de negocio
│   ├── UsuarioService.java            # Gestión de usuarios
│   ├── CuentaService.java             # Operaciones de cuentas
│   └── TransaccionService.java        # Registro de movimientos
│
├── repository/                         # Acceso a datos (JDBC)
│   ├── ConexionDB.java                # Singleton de conexión
│   ├── DatabaseInitializer.java       # Creación de tablas
│   ├── UsuarioRepository.java         # CRUD usuarios
│   ├── CuentaRepository.java          # CRUD cuentas
│   └── TransaccionRepository.java     # CRUD transacciones
│
├── model/                              # Modelos de datos
│   ├── Usuario.java                   # Clase abstracta base
│   ├── Cliente.java                   # Subtipo cliente
│   ├── Admin.java                     # Subtipo admin
│   ├── Cuenta.java                    # Modelo de cuenta
│   ├── Transaccion.java               # Modelo de transacción
│   ├── Rol.java                       # Enum de roles
│   ├── TipoTransaccion.java           # Enum tipos
│   └── Exception/                     # Excepciones personalizadas
│
├── dto/                                # Data Transfer Objects
│   ├── LoginDTO.java                  # Para login
│   ├── RegistroClienteDTO.java        # Para registro
│   ├── DepositoRetiroDTO.java         # Para depósitos/retiros
│   └── TransferenciaDTO.java          # Para transferencias
│
├── utils/                              # Utilitarios
│   ├── HashUtils.java                 # SHA-256 hashing
│   └── UUIDUtils.java                 # Manejo de UUIDs
│
└── ui/
    └── Navegador.java                 # Gestión de navegación

view/fxml/                             # Archivos JavaFX
├── login.fxml                         # Pantalla de login
├── registro.fxml                      # Pantalla de registro
├── DashboardCliente.fxml              # Panel cliente
├── DashboardAdmin.fxml                # Panel admin
└── configuracion.fxml                 # Configuración
```

## 💾 Almacenamiento y Base de Datos

### Conexión SQLite + JDBC

**Patrón Singleton para ConexionDB:**
```java
// Garantiza una única instancia de conexión
ConexionDB db = ConexionDB.getInstancia();
Connection conexion = db.getConexion();
```

**URL de Conexión:**
```
jdbc:sqlite:banco.db
```

### Estructura de Tablas

#### Tabla `usuarios`
Almacena tanto Clientes como Admins (polimorfismo en BD):
```sql
CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    contrasena TEXT NOT NULL,        -- Almacena HASH SHA-256, nunca la contraseña real
    rol TEXT NOT NULL                -- 'CLIENTE' o 'ADMIN'
);
```

#### Tabla `datos_clientes`
Datos específicos del cliente (herencia por tabla):
```sql
CREATE TABLE datos_clientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    apellido TEXT NOT NULL,
    fecha_creacion TEXT NOT NULL,    -- ISO-8601 format
    usuario_id INTEGER,              -- FK a usuarios.id
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
```

#### Tabla `cuentas`
Cuentas bancarias de clientes:
```sql
CREATE TABLE cuentas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_cuenta TEXT NOT NULL,     -- UUID visible
    alias TEXT NOT NULL,             -- Nombre personalizado
    saldo TEXT NOT NULL,             -- BigDecimal como String
    fecha_creacion TEXT NOT NULL,    -- ISO-8601
    cliente_id INTEGER,              -- FK a usuarios.id
    FOREIGN KEY (cliente_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
```

#### Tabla `transacciones`
Historial de depósitos, retiros y transferencias:
```sql
CREATE TABLE transacciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_transaccion TEXT NOT NULL, -- UUID de la transacción
    monto TEXT NOT NULL,              -- BigDecimal como String
    tipo TEXT NOT NULL,               -- 'DEPOSITO', 'RETIRO', 'TRANSFERENCIA'
    fecha_hora TEXT NOT NULL,         -- ISO-8601
    cuenta_origen_id INTEGER NULL,    -- FK nullable
    cuenta_destino_id INTEGER NULL,   -- FK nullable
    FOREIGN KEY (cuenta_origen_id) REFERENCES cuentas(id),
    FOREIGN KEY (cuenta_destino_id) REFERENCES cuentas(id)
);
```

### Consideraciones de Diseño

**BigDecimal como String:**
```java
// Las operaciones monetarias se guardan como String para evitar
// errores de precisión en cálculos con decimales
ps.setString(2, c.getSaldo().toString());  // "1234.56"
BigDecimal saldo = new BigDecimal(rs.getString("saldo"));
```

**UUID para identificadores públicos:**
```java
// Las cuentas tienen UUID público (cliente lo ve)
// pero también id numérico en BD (para FK y performance)
private UUID numeroCuenta;      // UUID: "123e4567-e89b-12d3-a456-426614174000"
private long id;                // ID: 1
```

**Transacciones ACID en transferencias:**
```java
// Garantiza consistencia: si falla, todo se revierte
con.setAutoCommit(false);
try {
    this.repository.actualizarCuenta(cuentaOrigen);
    this.repository.actualizarCuenta(cuentaDestino);
    this.transaccionService.registrar(transaccion);
    con.commit();
} catch (SQLException e) {
    con.rollback();  // Revierte todos los cambios
    throw e;
}
```

## 🔐 Seguridad

### Hashing SHA-256

**Nunca se guardan contraseñas en texto plano:**

```java
// HashUtils.java
public static String hashear(String texto) {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hash = md.digest(texto.getBytes(StandardCharsets.UTF_8));
    
    StringBuilder sb = new StringBuilder();
    for(byte b : hash) {
        sb.append(String.format("%02x", b));  // Convertir a hexadecimal
    }
    return sb.toString();
}
```

**En login:**
```java
// Comparar hash de entrada con hash almacenado
if(!usuario.getContraseña().equals(HashUtils.hashear(inputUsuario))) {
    throw new ContraseñaIncorrectaException("Contraseña incorrecta");
}
```

### Validaciones Robustas

**Contraseña fuerte:**
- Mínimo 8 caracteres
- Al menos una mayúscula
- Al menos una minúscula
- Al menos un número
- Al menos un carácter especial (.?/-)

**Nombre y Apellido:**
- 2-50 caracteres
- Sin números
- Sin espacios en blanco

**Email:**
- Único en la base de datos
- Validado antes de permitir registro

## 🔄 Patrón Repository

**Abstracción del acceso a datos:**

```java
// Repository maneja TODO lo relacionado a BD
public class UsuarioRepository {
    // INSERT
    public Usuario guardarUsuario(Usuario u) throws SQLException { ... }
    
    // SELECT
    public Optional<Usuario> buscarUsuarioPorEmail(String email) { ... }
    
    // UPDATE
    public boolean actualizarUsuario(Usuario u) throws SQLException { ... }
    
    // DELETE
    public boolean eliminarUsuario(long id) throws SQLException { ... }
}
```

**Beneficios:**
- Separación entre lógica y persistencia
- Fácil de testear
- Fácil de cambiar BD (SQLite → MySQL → PostgreSQL)
- Base para JPA/Hibernate en Spring Boot

## 📦 Patrón DTO (Data Transfer Object)

**Transferencia de datos sin exponer toda la clase:**

```java
// LoginDTO.java - Solo los datos que necesita el login
public record LoginDTO(String email, String contraseña) {}

// RegistroClienteDTO.java
public record RegistroClienteDTO(
    String nombre,
    String apellido,
    String email,
    String contraseña
) {}

// TransferenciaDTO.java
public record TransferenciaDTO(
    String numeroCuentaOrigen,
    String numeroCuentaDestino,
    BigDecimal monto
) {}
```

**Ventajas:**
- Validación centralizada
- Flexibilidad (cambiar BD sin afectar clientes)
- Seguridad (no exponer campos internos)
- Base para request/response en APIs REST con Spring

## 💡 Inyección de Dependencias Manual

**Preparación para Spring @Autowired:**

```java
public class CuentaService {
    private UsuarioService usuarioService;
    private TransaccionService transaccionService;
    
    // Constructor vacío
    public CuentaService() { }
    
    // Constructor con una dependencia
    public CuentaService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    // Constructor con dos dependencias
    public CuentaService(UsuarioService usuarioService, 
                        TransaccionService transaccionService) {
        this(usuarioService);
        this.transaccionService = transaccionService;
    }
}

// Uso
CuentaService service = new CuentaService(usuarioService, transaccionService);
```

**En Spring Boot esto se automatiza:**
```java
@Service
public class CuentaService {
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private TransaccionService transaccionService;
}
```

## 📊 Flujo de Operaciones

### Depositar Dinero
```
Cliente → DashboardClienteController
       ↓
       LoginController.handleDeposito()
       ↓
       CuentaService.depositar(DepositoRetiroDTO)
       ├→ Validar cuenta existe
       ├→ Validar monto > 0
       ├→ Aumentar saldo
       ├→ Crear Transaccion
       └→ CuentaRepository.actualizarCuenta()
               └→ BD: UPDATE cuentas SET saldo...
```

### Transferencia (Transacción ACID)
```
Cliente A → DashboardClienteController
          ↓
          CuentaService.transferir(TransferenciaDTO)
          ├→ Validar ambas cuentas existen
          ├→ Validar saldo suficiente en origen
          ├→ BEGIN TRANSACTION
          │  ├→ CuentaRepository.actualizarCuenta(origen)
          │  ├→ CuentaRepository.actualizarCuenta(destino)
          │  ├→ TransaccionRepository.guardarTransaccion()
          │  └→ COMMIT
          └→ Cliente B recibe dinero
```

## 🚀 Cómo Usar

### Requisitos
- Java 11+
- JavaFX SDK
- SQLite (incluido en JDBC)

### Compilación
```bash
javac -d build src/**/*.java -cp lib/*
```

### Ejecución
```bash
java -cp build:lib/* ui.Main
```

### Primer Login
- **Email:** adminBanco@gmail.com
- **Contraseña:** 12345

## 🎓 Conceptos Clave Aprendidos

### Database & JDBC
- ✅ Conexión JDBC a SQLite
- ✅ PreparedStatement (prevención de SQL Injection)
- ✅ ResultSet mapping a objetos
- ✅ Transacciones y commit/rollback

### Arquitectura
- ✅ Patrón MVC con capas
- ✅ Repository Pattern
- ✅ Service Layer
- ✅ DTO Pattern
- ✅ Singleton Pattern

### Seguridad
- ✅ Hashing SHA-256
- ✅ Validaciones robustas
- ✅ Manejo de excepciones

### Inyección de Dependencias
- ✅ Constructores parametrizados
- ✅ Base para @Autowired en Spring
- ✅ Loose coupling, high cohesion

## 📝 Notas Importantes

- Las transacciones monetarias usan **BigDecimal** para precisión
- Las contraseñas NUNCA se guardan en texto plano
- Cada operación genera transacción en BD (auditoria)
- UUID para números de cuenta (cliente los ve, seguros)
- Validación de datos en Service (no en Repository)
- Foreign Keys con ON DELETE CASCADE para integridad referencial

## 👨‍💻 Autor

**Agustín Marchetta**  
Proyecto: Sistema Bancario - Preparación para Spring Boot  
Año: 2026

---
