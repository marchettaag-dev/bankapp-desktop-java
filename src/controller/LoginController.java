package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dto.LoginDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Cliente;
import model.Cuenta;
import model.Rol;
import model.Usuario;
import model.Exception.ContraseñaIncorrectaException;
import model.Exception.DatosVaciosIngresadosException;
import model.Exception.EmailIncorrectoException;
import service.CuentaService;
import service.UsuarioService;
import ui.Navegador;

public class LoginController implements Initializable {

	@FXML
	private TextField txtEmail;
	
	@FXML
	private PasswordField txtContraseña;
	
	@FXML
	private Button btnLogin;
	
	@FXML
	private Button btnRegistro;
	
	private UsuarioService UsuarioS;
	
	private CuentaService cuentaS;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.UsuarioS = new UsuarioService();
		this.cuentaS = new CuentaService(this.UsuarioS);
	}
	
	@FXML
	private void handleLogin(ActionEvent a) {
		
		try {
			
			if(txtEmail.getText().equals("") || txtContraseña.getText().equals("")) {
				throw new DatosVaciosIngresadosException("Hay campos vacios, completelos");
			}
			
		//System.out.println("Los campos no estan vacios");
		LoginDTO dto = new LoginDTO(
				txtEmail.getText().trim(),
				txtContraseña.getText().trim());
		
		if(this.UsuarioS.login(dto).getRol().equals(Rol.CLIENTE)) {
			
			Usuario u = this.UsuarioS.login(dto);
			
			Cliente cliente = (Cliente) u;
			
			System.out.println("El Cliente es: "+cliente.getId());
			
			List<Cuenta> listaCuentas = this.cuentaS.buscarPorCliente(u.getId());
			
			//System.out.println("La lista de Cuentas del usuario es: " +listaCuentas);
			
			Navegador.irADashboardCliente(cliente, listaCuentas.getFirst());
		} else {
			
			//System.out.println("Hola!!! Bienvenido ADMIN");
			
			Navegador.irADashboardAdmin();
		}
		
		//System.out.println("El Usuario Existe");
		
		}catch(DatosVaciosIngresadosException e) {
			this.mostrarAlerta(AlertType.WARNING, "Campos Vacios", e.getMessage());
		}catch(EmailIncorrectoException e) {
			this.mostrarAlerta(AlertType.WARNING, "Error en el Login", e.getMessage());
		} catch (ContraseñaIncorrectaException e) {
			this.mostrarAlerta(AlertType.WARNING, "Error en el Login", e.getMessage());
		}
	}
	
	@FXML
	private void handleRegistro(ActionEvent a) {
		
		Navegador.irA("/fxml/registro.fxml");
	}
	
	// ================================= Metodos Privados ================================================
	
	private void mostrarAlerta(AlertType tipo, String titulo, String detalle) {
	    // 1. Crear la alerta definiendo el tipo (INFORMACIÓN, ERROR, ADVERTENCIA, etc.)
	    Alert alerta = new Alert(tipo);
	    
	    // 2. Configurar los textos
	    alerta.setTitle(titulo);
	    
	    alerta.setContentText(detalle);
	    
	    // 3. Mostrar la alerta y esperar a que el usuario presione "Aceptar"
	    alerta.showAndWait();
	}
}
