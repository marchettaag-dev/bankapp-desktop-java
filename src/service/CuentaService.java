package service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import dto.DepositoRetiroDTO;
import dto.TransferenciaDTO;
import model.Cliente;
import model.Cuenta;
import model.TipoTransaccion;
import model.Transaccion;
import model.Usuario;
import model.Exception.AliasIncorrectoException;
import model.Exception.CuentaNoInicializadoException;
import model.Exception.DatosInvalidosException;
import model.Exception.MontoIngresadoIncorrectoException;
import repository.ConexionDB;
import repository.CuentaRepository;

public class CuentaService {

	private CuentaRepository repository;
	
	private UsuarioService usuarioService;
	
    private TransaccionService transaccionService;
    
    // 1. Constructor vacío (Inicializa lo básico)
    public CuentaService() {
    	this.repository = new CuentaRepository();
    	
    	this.usuarioService = new UsuarioService();
    	this.transaccionService = new TransaccionService();
    }
    
    // 2. Constructor con una dependencia (Llama al primero)
    public CuentaService(UsuarioService usuarioService) {
    	this(); // Llama al constructor vacío para inicializar el repository
    	
    	this.usuarioService = usuarioService;			// Eso se llama inyección de dependencias.
    }
	
    // 3. Constructor con dos dependencias (Llama al segundo)
	public CuentaService(UsuarioService usuarioService, TransaccionService transaccionService) {
		this(usuarioService); // Llama al constructor anterior
		
        this.transaccionService = transaccionService;	// En Spring Boot esto lo maneja automáticamente 
        												// con @Autowired. Acá lo hacemos manual.
	}
	
	// --------------- Metodo que CREA la clase  -----------------------
	
	public Cuenta crearCuenta(Cliente cliente) throws SQLException {
		
		System.out.println("Buscando a Cliente... ");		
		Usuario u = this.usuarioService.buscarPorId(cliente.getId());
		
		System.out.println("El usuario es: " +u.getNombre()+" - "+u.getId());
		
		Cuenta cuenta = new Cuenta(cliente);	
		
		return this.repository.guardarCuenta(cuenta);
	}
	
	// --------------- Metodo que DEPOSITA en la clase  -----------------------
	
	public void depositar(DepositoRetiroDTO dto) throws MontoIngresadoIncorrectoException, CuentaNoInicializadoException, SQLException {
		
		Cuenta cuenta = this.validarCuentaCreada(dto.numeroCuenta());
		
		this.validarMontoConSaldo(cuenta, dto.monto(), "SUMA");
		
		cuenta.ingresarSaldo(dto.monto());
		
		Transaccion transaccion = new Transaccion(null, cuenta.getId(), TipoTransaccion.DEPOSITO, dto.monto());
		
		cuenta.agregarTransaccion(transaccion);
		
		//System.out.println("El saldo de la cuenta es: "+cuenta.getSaldo());
		
		this.repository.actualizarCuenta(cuenta);
		
		this.transaccionService.registrar(transaccion);
		
	}
	
	// --------------- Metodo que Retira en la clase  -----------------------
	
	public void retirar(DepositoRetiroDTO dto) throws CuentaNoInicializadoException, MontoIngresadoIncorrectoException, SQLException {
		
		Cuenta cuenta = this.validarCuentaCreada(dto.numeroCuenta());
		
		this.validarMontoConSaldo(cuenta, dto.monto(), "RESTA");
		
		cuenta.extraerSaldo(dto.monto());
		
		
		Transaccion transaccion = new Transaccion(cuenta.getId(), null, TipoTransaccion.RETIRO, dto.monto());
		
		cuenta.agregarTransaccion(transaccion);
		
		System.out.println("El saldo de la cuenta es: "+cuenta.getSaldo());
		
		this.repository.actualizarCuenta(cuenta);
		
		this.transaccionService.registrar(transaccion);
		
	}
	
	// --------------- Metodo que hace la Transferencia entre las clases  -----------------------
	
	public void transferir(TransferenciaDTO dto) throws CuentaNoInicializadoException, MontoIngresadoIncorrectoException, SQLException {
		
		if (dto.monto().compareTo(BigDecimal.ZERO) <= 0) {
			throw new MontoIngresadoIncorrectoException("El monto debe ser mayor a cero");
		}
		
		Cuenta cuentaDestino = this.validarCuentaCreada(dto.numeroCuentaDestino());
		
		Cuenta cuentaOrigen = this.validarCuentaCreada(dto.numeroCuentaOrigen());
		
		this.validarMontoConSaldo(cuentaOrigen, dto.monto(), "RESTA");
		
		
		cuentaDestino.ingresarSaldo(dto.monto());
		cuentaOrigen.extraerSaldo(dto.monto());
		
		
		Transaccion transaccion = new Transaccion(cuentaOrigen.getId(), cuentaDestino.getId(), TipoTransaccion.TRANSFERENCIA, dto.monto());
		
		cuentaDestino.agregarTransaccion(transaccion);
		cuentaOrigen.agregarTransaccion(transaccion);
		
		
		Connection con = ConexionDB.getInstancia().getConexion();
		
		con.setAutoCommit(false);
		try {
			
			this.repository.actualizarCuenta(cuentaOrigen);
			this.repository.actualizarCuenta(cuentaDestino);
			this.transaccionService.registrar(transaccion);
			
		    con.commit();
		} catch (SQLException e) {
		    con.rollback();
		    throw e;
		} finally {
		    con.setAutoCommit(true);
		}

	}
	
	// --------------- Metodo que BUSCA todas las clases  -----------------------
	
	public List<Cuenta> buscarTodasLasCuentas(){
		
		return this.repository.buscarTodos();
	}
	
	// --------------- Metodo que BUSCA por ID de la clase  -----------------------
	
	public Optional<Cuenta> buscarPorIdCuenta(Long id){
		return this.repository.buscarPorId(id);
	}
	
	// --------------- Metodo que BUSCA por ID del Cliente  -----------------------
	
	public List<Cuenta> buscarPorCliente(long clienteId){
		
		
		this.usuarioService.buscarPorId(clienteId);
		
		return this.repository.buscarPorNumeroCliente(clienteId);
	}
	
	// --------------- Metodo que BUSCA por Numero Cuenta las clases  -----------------------
	
	public Cuenta buscarPorNumeroCuenta(String numero) throws CuentaNoInicializadoException {
		
		return this.validarCuentaCreada(numero);
	}
	
	// --------------- Metodo que BUSCA por Numero Cuenta y Alias  -----------------------
	
	public Cuenta buscarPorNumeroCuentaOAlias(String busqueda) throws CuentaNoInicializadoException, AliasIncorrectoException {
		
		Optional<Cuenta> cOpt = this.repository.buscarPorNumeroCuenta(busqueda);
		
		if(cOpt.isPresent()) {
			return cOpt.get();
		}else {
			cOpt = this.repository.buscarPorAlias(busqueda);
			
			if(!cOpt.isPresent()) {
				throw new AliasIncorrectoException("El Alias no existe");
			}
		}
		return cOpt.get();
	}
	
	// --------------- Metodo que BUSCA por el Alias  -----------------------
	
	public Optional<Cuenta> buscarPorAlias(String alias) throws AliasIncorrectoException {
		return this.repository.buscarPorAlias(alias);
	}
	
	// --------------- Metodo que ACTUALIZA la cuenta -----------------------
	
	public void actualizar(Cuenta cuenta) {
		this.repository.actualizarCuenta(cuenta);
	}
	
	// --------------- Metodo PRIVADO que VALIDA la cuenta creada en la clase  -----------------------
	
	private Cuenta validarCuentaCreada(String numeroCuenta) throws CuentaNoInicializadoException {
		
		Optional<Cuenta> cOpt = this.repository.buscarPorNumeroCuenta(numeroCuenta);
		
		if(!cOpt.isPresent()) {
			throw new CuentaNoInicializadoException("La Cuenta no Existe");
		}
		return cOpt.get();
	}
	
	// --------------- Metodo PRIVADO que VALIDA Monto en la clase  -----------------------
	
	private void validarMontoConSaldo(Cuenta c, BigDecimal monto, String tipo) throws MontoIngresadoIncorrectoException {
		String mensaje = "";
		
		if(monto.compareTo(c.getSaldo()) == 1 && tipo.equals("RESTA")) {
			mensaje = "El Monto a retirar no es valido";
		}
		if(monto.compareTo(BigDecimal.ZERO) <= 0 && tipo.equals("SUMA")) {
			mensaje= "El Monto a depositar no es valido";
		}
		
		if(!mensaje.equals("")) {
			throw new MontoIngresadoIncorrectoException(mensaje);	
		}
	}
}
