package repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Admin;
import model.Cliente;
import model.Rol;
import model.Usuario;
import model.Exception.UsuarioNoInicializadoException;
import utils.HashUtils;

public class UsuarioRepository {
	
	private ConexionDB conexion;
	
	public UsuarioRepository() {
		this.conexion= ConexionDB.getInstancia();
	}
	
	// --------------- Metodo que Guarda la clase  -----------------------
	
	public Usuario guardarUsuario(Usuario u) throws SQLException {
		
		String sql = "INSERT INTO usuarios (nombre, email, contrasena, rol) VALUES (?, ?, ?, ?)";
		
		conexion.getConexion().setAutoCommit(false); // desactivar auto-commit
		
		// preparedStatement se usa cuando vamos a crear sql con valores que no sabemos (?)
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			// Completar cada ? en orden con los datos del objeto
			ps.setString(1, u.getNombre()); 						// Le asignamos el nombre a la tabla
			ps.setString(2, u.getEmail());    						// Le asignamos el email a la tabla
			ps.setString(3, HashUtils.hashear(u.getContraseña()));	// Le asignamos la contraseña a la tabla
			ps.setString(4, u.getRol().name());						// Le asignamos el nombre del Rol del enum a la tabla
			
			// Ejecutar el INSERT
			ps.executeUpdate();
			
			// Recuperar el ID que genero la BD automaticamente
	        ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()) {
	            u.setId(rs.getLong(1)); // asignar el ID al objeto
	        }
			
			if(u.getRol().equals(Rol.CLIENTE)) {
				
				Cliente c = (Cliente) u;
				
				String sql_datos_cliente = "INSERT INTO  datos_clientes (apellido, fecha_creacion, usuario_id) VALUES (?, ?, ?)";
				
				try(PreparedStatement pps = this.conexion.getConexion().prepareStatement(sql_datos_cliente)){
					
					pps.setString(1, c.getApellido());
					pps.setString(2, c.getFechaCreacion().toString());
					pps.setLong(3, u.getId());
					
					pps.executeUpdate();
					
				}
			}
	        
			this.conexion.getConexion().commit();
			
		}catch(SQLException e) {
			this.conexion.getConexion().rollback();
			e.printStackTrace();
		}
		
		return u;
	}
	
	// --------------- Metodo que Trae todas las clases -----------------------
	
		public List<Usuario> buscarTodos(){
			
			String sql = "SELECT * FROM usuarios";
			
			List<Usuario> listaUsuarios = new ArrayList<>();
			
			try (PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)) {
				
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
		        	
		        	Usuario us;
		        	
		        	us = this.construirUsuario(rs);
		        	
		        	listaUsuarios.add(us);
		            
		        }
				
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
			return listaUsuarios;
		}
	
	// --------------- Metodo que Trae todas las clases que su rol sea CLIENTE -----------------------
	public List<Cliente> buscarTodosClientes(){
		
		String sql = "SELECT * FROM usuarios WHERE rol = 'CLIENTE'";
		
		List<Cliente> listaClientes = new ArrayList<>();
		
		try (PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)) {
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
	        	
	        	Cliente cl;
	        	
	        	cl = (Cliente) this.construirUsuario(rs);
	        	
	        	listaClientes.add(cl);
	            
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return listaClientes;
		
	}
	
	// --------------- Metodo que busca por ID -----------------------
	
	public Optional<Usuario> buscarUsuarioPorId(long id){
		
		String sql_id = "SELECT * FROM usuarios WHERE id = ?";
		
		try (PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql_id)) {
	        ps.setLong(1, id);
	        ResultSet rs = ps.executeQuery(); // Ejecuta y devuelve el RS directamente

	        if (rs.next()) {
	        	
	        	Usuario us;
	        	
	        	us = this.construirUsuario(rs);
	            
	            return Optional.of(us);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		
	    return Optional.empty(); // Si no encontró nada o hubo error
	}
	
	// --------------- Metodo que busca por Email -----------------------
	
	public Optional<Usuario> buscarUsuarioPorEmail(String email){
		
		String sql_email = "SELECT * FROM usuarios WHERE email = ?";
		
		try (PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql_email)) {
	        ps.setString(1, email);
	        ResultSet rs = ps.executeQuery(); // Ejecuta y devuelve el RS directamente

	        if (rs.next()) {
	        	
	        	Usuario us;
	        	
	        	us = this.construirUsuario(rs);
	            
	            return Optional.of(us);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return Optional.empty(); // Si no encontró nada o hubo error
	}
	
	// --------------- Metodo PRIVADO para construir las clases -----------------------
	
	private Usuario construirUsuario(ResultSet rs) throws SQLException {
		
	    String rolStr = rs.getString("rol");
	    long id = rs.getLong("id"); // siempre está, sin importar cómo buscamos
	    Usuario resultado= null;

	    if (rolStr.equals("ADMIN")) {
            resultado = new Admin(
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("contrasena"),
                Rol.valueOf(rolStr)
            );
	    } else {
	    	 // Segundo SELECT para Clientes
            String sql_datos = "SELECT * FROM usuarios " +
                               "JOIN datos_clientes ON usuarios.id = datos_clientes.usuario_id " +
                               "WHERE usuarios.id = ?";
            
            try (PreparedStatement psCliente = this.conexion.getConexion().prepareStatement(sql_datos)) {
            	
            	psCliente.setLong(1, id);
                ResultSet rsCliente = psCliente.executeQuery();
                
                if (rsCliente.next()) { // ¡OBLIGATORIO para el segundo SELECT!
                    resultado = new Cliente(
                        rsCliente.getString("nombre"),
                        rsCliente.getString("email"),
                        rsCliente.getString("contrasena"),
                        Rol.valueOf(rsCliente.getString("rol")),
                        rsCliente.getString("apellido"),
                        ZonedDateTime.parse(rsCliente.getString("fecha_creacion"))
                    );
                    } 
            }
	    }
	    if(resultado == null) {
	    	throw new UsuarioNoInicializadoException("No se pudo inicializar la clase tras obtenerse en la BD");
	    } else {
	    	resultado.setId(rs.getLong("id"));
	    }
	    
    return resultado;
	}
	
	// --------------- Metodo que Actualiza la clase -----------------------
	
	public boolean actualizarUsuario(Usuario u) throws SQLException {
		
		String sql = "UPDATE usuarios SET nombre = ?, email = ?, contrasena = ? WHERE id = ?";
		
		this.conexion.getConexion().setAutoCommit(false);
		
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
			
			ps.setString(1, u.getNombre());
			ps.setString(2, u.getEmail());
			ps.setString(3, HashUtils.hashear(u.getContraseña()));
			ps.setLong(4, u.getId());
			
			int rs = ps.executeUpdate();
			int rsClientes = -100;
			
			if(u.getRol().equals(Rol.CLIENTE)) {
				
				Cliente c = (Cliente) u;
				
				String sql_clientes = "UPDATE datos_clientes SET apellido = ? WHERE usuario_id = ?";
				
				try(PreparedStatement psClintes = this.conexion.getConexion().prepareStatement(sql_clientes)){
					
					psClintes.setString(1, c.getApellido());
					psClintes.setLong(2, c.getId());
					
					rsClientes = psClintes.executeUpdate();
				}
			}
			
			if(rs> 0 && rsClientes > 0 || rsClientes == -100) {
				
				this.conexion.getConexion().commit();
				return true;
			} else {
				
				this.conexion.getConexion().rollback();
				return false;
			}
			
		}catch (SQLException e) {
			this.conexion.getConexion().rollback();
			e.printStackTrace();
			return false;
		}
	}
	
	// --------------- Metodo que Elimina la clase -----------------------
	
	public boolean eliminarUsuario(long id) throws SQLException {
		
		String sql = "DELETE FROM usuarios WHERE id = ?";
		
		this.conexion.getConexion().setAutoCommit(false);
		
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
			
			ps.setLong(1, id);
			
			int rs = ps.executeUpdate();
			
			if(rs > 0) {
				
				this.conexion.getConexion().commit();
				return true;
			} else {
				
				this.conexion.getConexion().rollback();
				return false;
			}
			
		} catch (SQLException e){
			this.conexion.getConexion().rollback();
			e.printStackTrace();
			return false;
		}
	}
}
