package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
	
	/* Esta clase se encargara de */
	
	private static ConexionDB instancia;
	
	/* Driver  :    Protocolo Driver : Detalles de la conexion del driver  */
	/* jdbc    :     sqlite          :      banco.db                      */
	
	private static String url = "jdbc:sqlite:banco.db";
	
	private Connection conexion;
	
	private ConexionDB() {
		// solo la clase puede llamar esto
	}
	
	public static ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }
	
	public Connection getConexion() throws SQLException{
		if (this.conexion == null) {
			this.conexion= DriverManager.getConnection(url);
        }
        return this.conexion;
	}
}
