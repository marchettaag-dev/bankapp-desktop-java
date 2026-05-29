package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dto.LoginDTO;
import dto.RegistroClienteDTO;
import model.Cliente;
import model.Rol;
import model.Usuario;
import model.Exception.ContraseñaIncorrectaException;
import model.Exception.DatosInvalidosException;
import model.Exception.EmailIncorrectoException;
import model.Exception.EmailYaRegistradoException;
import model.Exception.UsuarioNoInicializadoException;
import repository.UsuarioRepository;
import utils.HashUtils;

public class UsuarioService {

	private UsuarioRepository repository;
	
	public UsuarioService() {
		this.repository = new UsuarioRepository();
	}
	
	// --------------- Metodo que REGISTRA la clase  -----------------------
	
	public Usuario registrar(RegistroClienteDTO registro) throws SQLException, DatosInvalidosException, EmailYaRegistradoException {
		
		if(!this.validarNombreYApellido(registro.nombre())) {
			throw new DatosInvalidosException("El Nombre es invalido");
			
		}else if(!this.validarNombreYApellido(registro.apellido())){
			throw new DatosInvalidosException("El Apellidoe es invalido");
			
		}else if(this.verificarUsuario(registro.email())) {
			throw new EmailYaRegistradoException("El Email ya esta en uso");
			
		}else if(!this.validarContraseña(registro.contraseña())) {
			throw new DatosInvalidosException("La Contraseña es invalida - tiene que contener MINIMO 8 caracteres y: \n• una mayuscula,\n• una minuscula,\n• un numero,\n• un caracter especial (.?/-) )");
			
		}
		
		Cliente cliente = new Cliente(
			    registro.nombre(),
			    registro.email(),
			    registro.contraseña(),
			    Rol.CLIENTE,        // siempre CLIENTE en el registro
			    registro.apellido()
			);
		
		return this.repository.guardarUsuario(cliente);
	}
	
	// --------------- Metodo PRIVADO que VALIDA si un Usuario ya esta Ingresado  -----------------------
	
	private boolean verificarUsuario(String email) {
		
		Optional<Usuario> u = this.repository.buscarUsuarioPorEmail(email);
		boolean resultado;
		
		if(u.isEmpty()) {
			resultado = false;
		} else {
			resultado = true;
		}
		return resultado;
	}
	
	// --------------- Metodo que LOGUEA la clase  -----------------------
	
	
	public Usuario login(LoginDTO registro) throws ContraseñaIncorrectaException, EmailIncorrectoException {
		
		if(!this.verificarUsuario(registro.email())) {
			throw new EmailIncorrectoException("El Email es incorrecto");
		}
		
		Usuario u = this.repository.buscarUsuarioPorEmail(registro.email()).get();
		
		if(!u.getContraseña().equals(HashUtils.hashear(registro.contraseña()))) {
			throw new ContraseñaIncorrectaException("La Contraseña es incorrecta");
		}
		
		return u;
	}
	
	// --------------- Metodo que BUSCA por ID de la clase  -----------------------
	
	public Usuario buscarPorId(Long id) {
		
		Optional<Usuario> uOpt = this.repository.buscarUsuarioPorId(id);
		
		if(!uOpt.isPresent()) {
			throw new UsuarioNoInicializadoException("El Usuario no Existe");
		}
		
		return uOpt.get();
	}
	
	// --------------- Metodo que BUSCA Todos la clase  -----------------------
	
	public List<Usuario> buscarTodos(){
		
		return this.repository.buscarTodos();
	}
	
	// --------------- Metodo que BUSCA Todos la clases con rol CLIENTE  -----------------------
	
	public List<Cliente> buscarTodosClientes(){
		
		return this.repository.buscarTodosClientes();
	}
	
	// --------------- Metodo que ELIMINA la clase  -----------------------
	
	public Usuario eliminar(Long id) throws SQLException {

		Usuario u = this.buscarPorId(id);
		this.repository.eliminarUsuario(id);
		return u;
	}
	
	// --------------- Metodo que Cambia la Contraseña de la clase  -----------------------
	
	public void cambiarContraseña(Long id, String viejaContraseña, String nuevaContraseña) throws SQLException, ContraseñaIncorrectaException {
		
		Usuario u = this.buscarPorId(id);
		
		if(!u.getContraseña().equals(HashUtils.hashear(viejaContraseña))) {
			throw new ContraseñaIncorrectaException("La Contraseña es incorrecta");
		}
		
		this.validarContraseña(nuevaContraseña);
		
		u.setContraseña(nuevaContraseña);
		
		this.repository.actualizarUsuario(u);
	}
	
	// --------------- Metodo PRIVADO que VALIDA el Nombre  -----------------------
	
	private boolean validarNombreYApellido(String nYpAValidar) {
		
		String nombreSinEspacio = nYpAValidar.trim();
		nombreSinEspacio = nombreSinEspacio.replaceAll("\\s+", "");
		
		if(nombreSinEspacio.isBlank() || (nombreSinEspacio.length() < 2 || nombreSinEspacio.length() > 50 )) {
			return false;
		}
		
		for(Character c : nombreSinEspacio.toCharArray()) {
			
			if(Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
	
	// --------------- Metodo PRIVADO que VALIDA la Contraseña  -----------------------
	
	private boolean validarContraseña(String contraseña) {
		
		String contraseñaSinEspacio = contraseña.trim();
		
		if(contraseñaSinEspacio.isBlank() || (contraseñaSinEspacio.length() < 8)) {
			return false;
		}
		
		// 1. Validar al menos una minúscula
		if (!contraseñaSinEspacio.matches(".*[a-z].*")) {
		    System.out.println("Falta una minúscula");
		    return false;
		}

		// 2. Validar al menos una mayúscula
		if (!contraseñaSinEspacio.matches(".*[A-Z].*")) {
		    System.out.println("Falta una mayúscula");
		    return false;
		}

		// 3. Validar al menos un número
		if (!contraseñaSinEspacio.matches(".*\\d.*")) {
		    System.out.println("Falta un número");
		    return false;
		}

		// 4. Validar al menos un caracter especial permitido (.?/-)
		if (!contraseñaSinEspacio.matches(".*[.\\?/-].*")) {
		    System.out.println("Falta un caracter especial (.?/-)");
		    return false;
		}
		return true;
	}
}
