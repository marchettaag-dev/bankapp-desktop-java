package repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Cuenta;
import model.Usuario;

public class CuentaRepository {
	
	private ConexionDB conexion;
	
	public CuentaRepository() {
		this.conexion = ConexionDB.getInstancia();
	}
	
	// --------------- Metodo que Guarda la clase  -----------------------
	
	public Cuenta guardarCuenta(Cuenta c) throws SQLException{
		
		if (c.getClienteTitular() == null || c.getClienteTitular().getId() == 0) {
	        throw new IllegalStateException("El cliente debe estar guardado en la BD antes de crear una cuenta");
	    }
		
		String sql = "INSERT INTO cuentas (numero_cuenta, saldo, fecha_creacion, alias, cliente_id) VALUES (?, ?, ?, ?, ?)";
		
		conexion.getConexion().setAutoCommit(false);
		
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			
			ps.setString(1, c.getNumeroCuenta());
			ps.setString(2, c.getSaldo().toString());
			ps.setString(3, c.getFechaCreacion().toString());
			ps.setString(4, c.getAlias());
			ps.setLong(5, c.getClienteTitular().getId());
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()) {
	            c.setId(rs.getLong(1)); 
	        }
	        
	        this.conexion.getConexion().commit();
			
		}catch(SQLException e) {
			this.conexion.getConexion().rollback();
			e.printStackTrace();
		}
		
		return c;
	}
	
	// --------------- Metodo que Trae todas las clases -----------------------
	
	public List<Cuenta> buscarTodos(){
				
		String sql = "SELECT * FROM cuentas";
				
		List<Cuenta> listaCuentas = new ArrayList<>();
				
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
					
				ResultSet rs = ps.executeQuery();
					
				while(rs.next()) {
						
					Cuenta cu = this.construirCuenta(rs);
						
					listaCuentas.add(cu);
				}
					
		}catch(SQLException e) {
			e.printStackTrace();
		}
	return listaCuentas;
	}
	
	// --------------- Metodo que busca por ID -----------------------
	
	public Optional<Cuenta> buscarPorId(long id) {
		
		String sql_id = "SELECT * FROM cuentas WHERE id = ?";
		
		try (PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql_id)) {
	        ps.setLong(1, id);
	        ResultSet rs = ps.executeQuery(); 

	        if (rs.next()) {
	        		        	
	        	Cuenta cu = this.construirCuenta(rs);
	            
	            return Optional.of(cu);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		
	    return Optional.empty(); 
	}
	
	// --------------- Metodo que busca por NUMERO_CUENTA -----------------------
	
	public Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta){
		
		String sql = "SELECT * FROM cuentas WHERE numero_cuenta = ?";
		
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
			ps.setString(1, numeroCuenta);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				
				Cuenta cu;
				
				cu = construirCuenta(rs);
				
				return Optional.of(cu);
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return Optional.empty();
	}
	
	// --------------- Metodo que busca por CLIENTE ID -----------------------
	
	public List<Cuenta> buscarPorNumeroCliente(long id){
		
		List<Cuenta> listaCuentas = new ArrayList<>();
		String sql = "SELECT * FROM cuentas WHERE cliente_id = ?";
		
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				
				Cuenta cu;
				
				cu = construirCuenta(rs);
				
				listaCuentas.add(cu);
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return listaCuentas;
	}
	
	// --------------- Metodo que busca por ALIAS-----------------------
	
	public Optional<Cuenta> buscarPorAlias(String alias) {
		
		String sql = "SELECT * FROM cuentas WHERE alias = ?";
		
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
			ps.setString(1, alias);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				
				Cuenta cu;
				
				cu = construirCuenta(rs);
				
				return Optional.of(cu);
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return Optional.empty();
	}
	
	// --------------- Metodo PRIVADO para construir las clases -----------------------
	
	private Cuenta construirCuenta(ResultSet rs) throws SQLException {
		
		long id = rs.getLong("id");
		
		Cuenta resultado = new Cuenta(
				
				rs.getString("numero_cuenta"),
				rs.getLong("cliente_id"),
				new BigDecimal(rs.getString("saldo")),
				ZonedDateTime.parse(rs.getString("fecha_creacion")),
				rs.getString("alias")
				);
		
		resultado.setId(id);
		return resultado;
	}
	
	// --------------- Metodo que Actualiza la clase -----------------------
	
	public boolean actualizarCuenta(Cuenta c) {
		
		String sql = "UPDATE cuentas SET numero_cuenta = ?, saldo = ?, fecha_creacion = ?, cliente_id = ?, alias=? WHERE id = ?";
		
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
			
			ps.setString(1, c.getNumeroCuenta().toString());
			ps.setString(2, c.getSaldo().toString());
			ps.setString(3, c.getFechaCreacion().toString());
			ps.setLong(4, c.getIdCliente());
			ps.setString(5, c.getAlias());
			ps.setLong(6, c.getId());
			
			int rs = ps.executeUpdate();
			
			if(rs> 0) {
				
				return true;
			} else {
				return false;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// --------------- Metodo que Elimina la clase -----------------------
	
	public boolean eliminarCuenta(long id) {
		
		String sql = "DELETE FROM cuentas WHERE id = ?";
		
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
			ps.setLong(1, id);
			int rs = ps.executeUpdate();
			
			if(rs > 0) {
				return true;
			} else {
				return false;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
