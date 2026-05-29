package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import model.Cuenta;
import model.Rol;
import model.Transaccion;
import model.Usuario;
import model.Exception.AccesoDenegadoException;
import model.Exception.CuentaNoInicializadoException;
import repository.TransaccionRepository;

public class TransaccionService {

	private TransaccionRepository repository;
	
	public TransaccionService() {
		this.repository = new TransaccionRepository();
	}
	
	// --------------- Metodo que REGISTRA la clase  -----------------------
	
	public void registrar(Transaccion t) throws SQLException {
		
		this.repository.guardarTransaccion(t);
	}
	
	// --------------- Metodo que BUSCA el todas las clases  -----------------------
	
	public List<Transaccion> buscarTodasLasTransacciones(){
		return this.repository.buscarTodos();
	}
	
	// --------------- Metodo que BUSCA el Historial de la clase  -----------------------
	
	public List<Transaccion> buscarHistorialCuenta(String numeroCuenta) throws CuentaNoInicializadoException{
		
		CuentaService cs = new CuentaService();
		
		Cuenta c = cs.buscarPorNumeroCuenta(numeroCuenta);
		
		return this.repository.buscarPorNumeroCuenta(numeroCuenta);
	}
	
	// --------------- Metodo que BUSCA el ADMIN de la clase  -----------------------
	
	public List<Transaccion> buscarTodos(Usuario usuario) throws AccesoDenegadoException{
		
		if(usuario.getRol() != Rol.ADMIN) {
			throw new AccesoDenegadoException("Solo el Admin puede ver todas las transacciones");
		}
		return this.repository.buscarTodos();
	}
	
	// --------------- Metodo que BUSCA el la trasnferencia por id  -----------------------
	
	public Optional<Transaccion> buscarTodosPorId(long id) {
		return this.repository.buscarTransaccionPorId(id);
	}
}
