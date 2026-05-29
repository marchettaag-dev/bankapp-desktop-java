package controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.Cliente;
import model.Cuenta;
import model.Exception.AliasIncorrectoException;
import model.Exception.AliasInvalidoException;
import model.Exception.CuentaNoInicializadoException;
import service.CuentaService;
import ui.Navegador;

public class ConfiguracionController implements Initializable{

	@FXML
	private TextField txtNombreApellido;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtCVU;
	
	@FXML
	private TextField txtAliasEstablecido;
	
	@FXML
	private TextField txtAlias;
	
	@FXML
	private Button btnAplicarCambios;
	
	@FXML
	private Button btnVolver;
	
	private CuentaService cs;
	
	private Cliente cliente;
	
	private Cuenta cuenta;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public void iniciarDatos(Cliente cliente, Cuenta cuenta) {
		this.cliente = cliente;
		this.cuenta = cuenta;
		this.cs = new CuentaService();
		
		String nombreYapellido = this.cliente.getNombre()+" - "+this.cliente.getApellido();
		this.txtNombreApellido.setText(nombreYapellido);
		
		this.txtEmail.setText(this.cliente.getEmail());
		
		this.txtAliasEstablecido.setText(cuenta.getAlias());
		
		this.txtCVU.setText(cuenta.getNumeroCuenta());
	}
	
	@FXML
	private void aplicarCambios(ActionEvent a) throws AliasInvalidoException {
		
		try {
			System.out.println("El saldo de la cuenta es: "+cuenta.getSaldo());
			String alias = this.txtAlias.getText();
			
			this.verificarAliasExiste(alias);
			
			this.cuenta.setAlias(alias);
			
			this.cs.actualizar(cuenta);
			
			// Refrescar la cuenta desde la BD antes de volver
	        Cuenta cuentaActualizada = this.cs.buscarPorNumeroCuenta(this.cuenta.getNumeroCuenta());
	        System.out.println("El saldo de la cuenta es: "+cuentaActualizada.getSaldo());
			
			Navegador.irADashboardCliente(cliente, cuentaActualizada);
			
		}catch (AliasIncorrectoException e) {
			this.mostrarAlerta(AlertType.ERROR, "Error Alias", e.getMessage());
		}catch(AliasInvalidoException e) {
			this.mostrarAlerta(AlertType.ERROR, "Error Alias", e.getMessage());
		} catch (CuentaNoInicializadoException e) {
			this.mostrarAlerta(AlertType.ERROR, "Error Cuenta", e.getMessage());
		}
	}

	@FXML
	private void volver(ActionEvent a) throws CuentaNoInicializadoException {
		 Cuenta cuentaActualizada = this.cs.buscarPorNumeroCuenta(this.cuenta.getNumeroCuenta());
		 Navegador.irADashboardCliente(cliente, cuentaActualizada);
	}
	
	// ================================= Metodos Privados ================================================

	private boolean validarAlias(String alias) {
		if (alias == null) return false;
	    return alias.matches("^[a-z0-9.-]{6,20}$");
	}
	
	private void verificarAliasExiste(String alias) throws AliasIncorrectoException, AliasInvalidoException {
		

			Optional<Cuenta> c = this.cs.buscarPorAlias(alias);
			
			if(c.isPresent()) {
				throw new AliasIncorrectoException("Este Alias ya esta en uso");
			}
				
			if(this.validarAlias(alias) == false) {
				throw new AliasInvalidoException("El alias ingresado no cumple con los estandares");
			}
	}
	
	
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
