package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dto.RegistroClienteDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.Cliente;
import model.Cuenta;
import model.Exception.ContraseñaIncorrectaException;
import model.Exception.DatosInvalidosException;
import model.Exception.DatosVaciosIngresadosException;
import model.Exception.EmailIncorrectoException;
import model.Exception.EmailYaRegistradoException;
import service.CuentaService;
import service.UsuarioService;
import ui.Navegador;

public class RegistroController implements Initializable{
	
	@FXML
	private TextField txtNombre;
	
	@FXML
	private TextField txtApellido;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtContraseña;
	
	@FXML
	private Button btnCrearCuenta;
	
	@FXML
	private Button btnIniciarSesion;
	
	private UsuarioService usuarioS;
	
	private CuentaService cuentaS;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.usuarioS = new UsuarioService();
		this.cuentaS = new CuentaService(this.usuarioS);
	}

	@FXML
	private void registrarse(ActionEvent a) {
		
		try {
			
			if(txtNombre.getText().equals("") || txtApellido.getText().equals("")) {
				throw new DatosVaciosIngresadosException("Hay campos vacios, completelos");
			}
			
			if(txtEmail.getText().equals("") || txtContraseña.getText().equals("")) {
				throw new DatosVaciosIngresadosException("Hay campos vacios, completelos");
			}
			
			RegistroClienteDTO registro = new RegistroClienteDTO(
					txtNombre.getText(),
					txtApellido.getText(),
					txtEmail.getText(),
					txtContraseña.getText());
			
			// Guardamos al Cliente y lo recuperamos
			Cliente cliente = (Cliente) this.usuarioS.registrar(registro);
			//System.out.println("Usuario Creado");
			
			// Le creamos una CUENTA
			
			Cuenta cuenta = this.cuentaS.crearCuenta(cliente);

			//System.out.println("Cuenta Creada");
			
			Navegador.irADashboardCliente(cliente, cuenta);
			
		}catch(DatosVaciosIngresadosException e) {
			this.mostrarAlerta(AlertType.WARNING, "Campos Vacios", e.getMessage());
		}catch(EmailYaRegistradoException e) {
			this.mostrarAlerta(AlertType.WARNING, "Error al Inicar Sesion", e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatosInvalidosException e) {
			this.mostrarAlerta(AlertType.WARNING, "Error al Inicar Sesion", e.getMessage());
		}
	}
	
	@FXML
	private void iniciarSesion(ActionEvent a) {
		
		Navegador.irA("/fxml/login.fxml");
	}
	
	// ================================= Metodos Privados ================================================

	
	private void mostrarAlerta(AlertType tipo, String titulo, String detalle) {
	    Alert alerta = new Alert(tipo);

	    alerta.setTitle(titulo);
	    
	    alerta.setContentText(detalle);

	    alerta.showAndWait();
	}
}
