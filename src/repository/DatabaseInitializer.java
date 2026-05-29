package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import model.Admin;
import model.Rol;
import utils.HashUtils;

public class DatabaseInitializer {
	
	private static final String emailAdmin = "adminBanco@gmail.com";
	private static final String contraseña = "12345";
	
	private DatabaseInitializer() {}
	
	private static void crearAdminSiNoExiste() throws SQLException {
		
		UsuarioRepository repo = new UsuarioRepository();
		
		if(!repo.buscarUsuarioPorEmail(emailAdmin).isPresent()) {
			Admin admin = new Admin(
					"Admin",
					emailAdmin,
					contraseña,
					Rol.ADMIN
					);
			
			
			repo.guardarUsuario(admin);
		}
	}
	
	public static void inicializar() {
	    try {
	        // Paso 1: Obtener la única instancia de ConexionDB (patrón Singleton)
	        ConexionDB db = ConexionDB.getInstancia();

	        // Paso 2: Obtener la conexión activa a la BD (crea el archivo banco.db si no existe)
	        Connection conec = db.getConexion();

	        // Paso 3: Crear un Statement para ejecutar SQL que no devuelve datos
	        Statement stmt = conec.createStatement();

	        // Activar las foreign keys en SQLite (están desactivadas por defecto)
	        stmt.executeUpdate("PRAGMA foreign_keys = ON");

	        // Tabla principal de usuarios (tanto Clientes como Admins)
	        // el campo 'rol' distingue entre CLIENTE y ADMIN
	        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios "
	                + "("
	                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"  // ID generado automáticamente por SQLite
	                + "nombre TEXT NOT NULL,"
	                + "email TEXT NOT NULL UNIQUE,"            // no pueden existir dos usuarios con el mismo email
	                + "contrasena TEXT NOT NULL,"              // se guarda el HASH, nunca la contraseña real
	                + "rol TEXT NOT NULL"                      // valores posibles: CLIENTE o ADMIN
	                + ")");

	        // Datos específicos del Cliente (herencia de tabla por clase)
	        // Se une a 'usuarios' con un JOIN usando usuario_id
	        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS datos_clientes "
	                + "("
	                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
	                + "apellido TEXT NOT NULL,"
	                + "fecha_creacion TEXT NOT NULL,"          // guardado como String ISO-8601
	                + "usuario_id INTEGER,"                    // FK que apunta al id en la tabla usuarios
	                + "FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE"
	                + ")");

	        // Cuentas bancarias de los clientes
	        // Un cliente puede tener varias cuentas (relación uno a muchos)
	        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cuentas "
	                + "("
	                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
	                + "numero_cuenta TEXT NOT NULL,"           // UUID visible para el cliente
	                + "alias TEXT NOT NULL,"
	                + "saldo TEXT NOT NULL,"                   // BigDecimal guardado como String para evitar errores de precisión
	                + "fecha_creacion TEXT NOT NULL,"
	                + "cliente_id INTEGER,"                    // FK que apunta al id del dueño en la tabla usuarios
	                + "FOREIGN KEY (cliente_id) REFERENCES usuarios(id) ON DELETE CASCADE"
	                + ")");

	        // Historial de movimientos: depósitos, retiros y transferencias
	        // cuenta_origen_id y cuenta_destino_id pueden ser NULL según el tipo de movimiento:
	        //   DEPOSITO    → cuenta_origen_id = NULL
	        //   RETIRO      → cuenta_destino_id = NULL
	        //   TRANSFERENCIA → ambos tienen valor
	        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS transacciones "
	                + "("
	                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
	                + "numero_transaccion TEXT NOT NULL,"      // UUID visible de la transacción
	                + "monto TEXT NOT NULL,"                   // BigDecimal como String
	                + "tipo TEXT NOT NULL,"                    // DEPOSITO, RETIRO o TRANSFERENCIA
	                + "fecha_hora TEXT NOT NULL,"
	                + "cuenta_origen_id INTEGER NULL,"         // FK nullable → cuenta de donde sale el dinero
	                + "cuenta_destino_id INTEGER NULL,"        // FK nullable → cuenta que recibe el dinero
	                + "FOREIGN KEY (cuenta_origen_id) REFERENCES cuentas(id),"
	                + "FOREIGN KEY (cuenta_destino_id) REFERENCES cuentas(id)"
	                + ")");
	        
	        crearAdminSiNoExiste();

	    } catch (SQLException e) {
	        // Si algo falla al crear las tablas, se imprime el error completo con la traza
	        // Esto nos permite saber exactamente en qué línea ocurrió el problema
	        e.printStackTrace();
	    }
	}
}
