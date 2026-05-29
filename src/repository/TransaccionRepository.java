package repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Cuenta;
import model.TipoTransaccion;
import model.Transaccion;

public class TransaccionRepository {

	private ConexionDB conexion;
	
	public TransaccionRepository() {
		this.conexion = ConexionDB.getInstancia();
	}
	
	// --------------- Metodo que Guarda la clase  -----------------------
	
	public Transaccion guardarTransaccion(Transaccion t) throws SQLException {
		
		String sql = "INSERT INTO transacciones (numero_transaccion, monto, tipo, fecha_hora, cuenta_origen_id, cuenta_destino_id) VALUES (?, ?, ?, ?, ?, ?)";
		
		conexion.getConexion().setAutoCommit(false);
		
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
			
			ps.setString(1, t.getNumeroTransaccion());
			ps.setString(2, t.getMonto().toString());
			ps.setString(3, t.getTipo().toString());
			ps.setString(4, t.getFechaYHora().toString());
			
			if (t.getIdCuentaOrigen() != null) {
			    ps.setLong(5, t.getIdCuentaOrigen());
			} else {
			    ps.setNull(5, Types.INTEGER);
			}
			
			if (t.getIdCuentaDestino() != null) {
			    ps.setLong(6, t.getIdCuentaDestino());
			} else {
			    ps.setNull(6, Types.INTEGER);
			}
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()) {
	            t.setId(rs.getLong(1)); 
	        }
	        
	        this.conexion.getConexion().commit();
			
		}catch(SQLException e) {
			e.printStackTrace();
			this.conexion.getConexion().rollback();
		}
		
		return t;
	}
	
	// --------------- Metodo que Trae todas las clases -----------------------
	
	public List<Transaccion> buscarTodos(){
		
		String sql = "SELECT * FROM transacciones";
				
		List<Transaccion> listaTransacciones = new ArrayList<>();
				
		try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
					
				ResultSet rs = ps.executeQuery();
					
				while(rs.next()) {
						
					Transaccion tr = this.construirTransaccion(rs);
						
					listaTransacciones.add(tr);
				}
					
		}catch(SQLException e) {
			e.printStackTrace();
		}
	return listaTransacciones;
	}
	// --------------- Metodo que busca por ID -----------------------
	
	public Optional<Transaccion> buscarTransaccionPorId(long id) {
		
		String sql_id = "SELECT * FROM transacciones WHERE id = ?";
		
		try (PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql_id)) {
	        ps.setLong(1, id);
	        ResultSet rs = ps.executeQuery(); 

	        if (rs.next()) {
	        		        	
	        	Transaccion tr = this.construirTransaccion(rs);
	            
	            return Optional.of(tr);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		
	    return Optional.empty(); 
		
	}

	// --------------- Metodo que busca por CUENTA -----------------------
	
		public List<Transaccion> buscarPorCuenta(long idCuenta){
			
			List<Transaccion> listaTransacciones = new ArrayList<>();
			String sql = "SELECT * FROM transacciones WHERE cuenta_origen_id = ? OR cuenta_destino_id = ?";
			
			try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
				ps.setLong(1, idCuenta);
				ps.setLong(2, idCuenta);
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					
					Transaccion tr = this.construirTransaccion(rs);
		            
		            listaTransacciones.add(tr);
					
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
			return listaTransacciones;
		}
		
		// --------------- Metodo que busca por NUMERO CUENTA -----------------------
		
		public List<Transaccion> buscarPorNumeroCuenta(String numeroCuenta){
			
			List<Transaccion> listaTransacciones = new ArrayList<>();
			String sql = "SELECT t.* FROM transacciones t\r\n"
					+ "JOIN cuentas c ON t.cuenta_origen_id = c.id OR t.cuenta_destino_id = c.id\r\n"
					+ "WHERE c.numero_cuenta = ?";
			
			try(PreparedStatement ps = this.conexion.getConexion().prepareStatement(sql)){
				ps.setString(1, numeroCuenta);
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					
					Transaccion tr = this.construirTransaccion(rs);
					
					listaTransacciones.add(tr);
					
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
			return listaTransacciones;
		}
	
	// --------------- Metodo PRIVADO para construir las clases -----------------------
	
		private Transaccion construirTransaccion(ResultSet rs) throws SQLException {
			
			long id = rs.getLong("id");
			
			long origenId = rs.getLong("cuenta_origen_id");
			Long idOrigen = rs.wasNull() ? null : origenId;
			
			long destinoId = rs.getLong("cuenta_destino_id");
			Long idDestino = rs.wasNull() ? null : destinoId;
			
			Transaccion resultado = new Transaccion(
					
					idOrigen,
					idDestino,
					TipoTransaccion.valueOf(rs.getString("tipo")),
					new BigDecimal(rs.getString("monto")),
					ZonedDateTime.parse(rs.getString("fecha_hora"))
					);
			
			resultado.setId(id);
			return resultado;
		}
}
