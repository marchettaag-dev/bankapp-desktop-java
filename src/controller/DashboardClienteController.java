package controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import dto.DepositoRetiroDTO;
import dto.TransferenciaDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import model.Cliente;
import model.Cuenta;
import model.Exception.AliasIncorrectoException;
import model.Exception.CuentaNoInicializadoException;
import model.Exception.MontoIngresadoIncorrectoException;
import service.CuentaService;
import service.UsuarioService;
import ui.Navegador;

public class DashboardClienteController implements Initializable{
	
	@FXML
	private MenuButton mbtnCuenta;

	@FXML
	private MenuItem iCerrarSesion;
	
	@FXML
	private MenuItem iConfiguracion;
	
	@FXML
	private Label labelCuenta;
	
	@FXML
	private Label lbDinero;
	
	@FXML
	private Button btnRetirar;
	
	@FXML
	private Button btnTransferir;
	
	@FXML
	private Button btnDepositar;
	
	private UsuarioService usuarioS;
	
	private CuentaService cuentaS;
	
	private Cliente cliente;
	
	private Cuenta cuentaActual;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.usuarioS = new UsuarioService();
		this.cuentaS = new CuentaService();

	}
	
	public void iniciarDatos(Cliente cliente, Cuenta cuenta) {
		
		this.cliente = cliente;
		this.cuentaActual = cuenta;
		
		String saldoTransformado = this.transformarSaldo(this.cuentaActual.getSaldo().toString());
        this.lbDinero.setText(saldoTransformado);
        
        this.labelCuenta.setText("Cuenta: "+cliente.getEmail());
	}
	
	@FXML
	private void retirar(ActionEvent a) {
		
		TextInputDialog dialog = new TextInputDialog("0.00");
		
		dialog.setTitle("Retirar");
	    dialog.setHeaderText("Ingrese el monto a retirar:");
	    dialog.setContentText("Monto:");
	    
	    Optional<String> resultado = dialog.showAndWait();
	    
	    resultado.ifPresent(montoStr -> {
	        try {
	            BigDecimal monto = new BigDecimal(montoStr);
	            
				DepositoRetiroDTO dto = new DepositoRetiroDTO(
	                cuentaActual.getNumeroCuenta(), 
	                monto);
	            
	            this.cuentaS.retirar(dto);
	            
	            // Refrescar el objeto cuentaActual desde la BD
	            this.cuentaActual = this.cuentaS.buscarPorNumeroCuenta(dto.numeroCuenta());

	            // Actualizar el label
	            String saldoTransformado = this.transformarSaldo(
	                this.cuentaActual.getSaldo().toString()
	            );
	            this.lbDinero.setText(saldoTransformado);
	            
	        } catch (NumberFormatException e) {
	            mostrarAlerta(AlertType.ERROR, "Error", "Ingrese un número válido");
	        }catch(MontoIngresadoIncorrectoException e) {
	        	mostrarAlerta(AlertType.ERROR, "Error Monto", e.getMessage());
	        }catch(CuentaNoInicializadoException e) {
	        	mostrarAlerta(AlertType.ERROR, "Error Cuenta", e.getMessage());
	        }catch(SQLException e) {
	        	mostrarAlerta(AlertType.ERROR, "Error SQL", e.getMessage());
	        }catch(Exception e){
	        	mostrarAlerta(AlertType.ERROR, "Error", e.getMessage());
	        }
	    });
	}
	
	@FXML
	private void transferir(ActionEvent a) {
		
		TextInputDialog dialog = new TextInputDialog("Buscador...");
		
		dialog.setTitle("Transferir");
	    dialog.setHeaderText("Ingrese el alias de la cuenta:");
	    dialog.setContentText("Alias:");
		
	    Optional<String> resultado = dialog.showAndWait();
	    
	    resultado.ifPresent(aliasOcvu -> {
	    	
	        try {
	
	            Cuenta cuentaDestino = this.cuentaS.buscarPorNumeroCuentaOAlias(aliasOcvu);
	            
	            System.out.println("Usuario Encontrado");
	            
	            TextInputDialog dialogTransferencia = new TextInputDialog("0.00");
	    		
	            dialogTransferencia.setTitle("Transferir");
	            dialogTransferencia.setHeaderText("Ingrese el monto:");
	            dialogTransferencia.setContentText("Monto:");
	    		
	    	    Optional<String> resultadoTransferencia = dialogTransferencia.showAndWait();
	    	    
	    	    resultadoTransferencia.ifPresent(montoStr -> {
	    	        try {
	    	            BigDecimal monto = new BigDecimal(montoStr);
	    	            
	    				TransferenciaDTO dtoTransferencia = new TransferenciaDTO(
	    	                cuentaActual.getNumeroCuenta(), 
	    	                cuentaDestino.getNumeroCuenta(),
	    	                monto);
	    	            
	    	            this.cuentaS.transferir(dtoTransferencia);
	    	            
	    	            // Refrescar el objeto cuentaActual desde la BD
	    	            this.cuentaActual = this.cuentaS.buscarPorNumeroCuenta(
	    	                dtoTransferencia.numeroCuentaOrigen()
	    	            );

	    	            // Actualizar el label
	    	            String saldoTransformado = this.transformarSaldo(
	    	                this.cuentaActual.getSaldo().toString()
	    	            );
	    	            this.lbDinero.setText(saldoTransformado);
	    	            
	    	        } catch (NumberFormatException e) {
	    	            mostrarAlerta(AlertType.ERROR, "Error", "Ingrese un número válido");
	    	        }catch(MontoIngresadoIncorrectoException e) {
	    	        	mostrarAlerta(AlertType.ERROR, "Error Monto", e.getMessage());
	    	        }catch(CuentaNoInicializadoException e) {
	    	        	mostrarAlerta(AlertType.ERROR, "Error Cuenta", e.getMessage());
	    	        }catch(SQLException e) {
	    	        	mostrarAlerta(AlertType.ERROR, "Error SQL", e.getMessage());
	    	        }catch(Exception e){
	    	        	mostrarAlerta(AlertType.ERROR, "Error", e.getMessage());
	    	        }
	    	    });
	            
	        }catch (CuentaNoInicializadoException | AliasIncorrectoException e) {
	        	this.mostrarAlerta(AlertType.ERROR, "Error Cuenta", e.getMessage());
	        }
	        
	    });
	}
	
	@FXML
	private void depositar(ActionEvent a) {
		
		TextInputDialog dialog = new TextInputDialog("0.00");
		
	    dialog.setTitle("Depositar");
	    dialog.setHeaderText("Ingrese el monto a depositar:");
	    dialog.setContentText("Monto:");
	    
	    Optional<String> resultado = dialog.showAndWait();
	    
	    resultado.ifPresent(montoStr -> {
	        try {
	            BigDecimal monto = new BigDecimal(montoStr);
	            
				DepositoRetiroDTO dto = new DepositoRetiroDTO(
	                cuentaActual.getNumeroCuenta(), 
	                monto);
	            
	            this.cuentaS.depositar(dto);
	            
	            // Refrescar el objeto cuentaActual desde la BD
	            this.cuentaActual = this.cuentaS.buscarPorNumeroCuenta(dto.numeroCuenta());

	            // Actualizar el label
	            String saldoTransformado = this.transformarSaldo(
	                this.cuentaActual.getSaldo().toString()
	            );
	            this.lbDinero.setText(saldoTransformado);
	            
	        } catch (NumberFormatException e) {
	            mostrarAlerta(AlertType.ERROR, "Error", "Ingrese un número válido");
	        }catch(MontoIngresadoIncorrectoException e) {
	        	mostrarAlerta(AlertType.ERROR, "Error Monto", e.getMessage());
	        }catch(CuentaNoInicializadoException e) {
	        	mostrarAlerta(AlertType.ERROR, "Error Cuenta", e.getMessage());
	        }catch(SQLException e) {
	        	mostrarAlerta(AlertType.ERROR, "Error SQL", e.getMessage());
	        }catch(Exception e){
	        	mostrarAlerta(AlertType.ERROR, "Error", e.getMessage());
	        }
	    });
	}
	
	@FXML
	private void cerrarSesion(ActionEvent a) {
		
		Navegador.irA("/fxml/login.fxml");
	}
	
	@FXML
	private void cambiarConfiguracion(ActionEvent a) {
		
		Navegador.irAConfiguracio(cliente, cuentaActual);
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
	
	private String transformarSaldo(String saldo) {
		
		String saldoComa = saldo.replace(".", ",");
		
		String cadenaTransformada = "";
		
		int contador = 0;
		boolean coma = true;
		
		for(int i=saldoComa.length()-1; i >= 0; i--) {
			
			int resto = -1;
			contador += 1;
			
			if(saldoComa.charAt(i) != ',' || saldoComa.charAt(i) != '.') {
				resto = contador % 3;
			}
			
			
			cadenaTransformada = saldoComa.charAt(i) + cadenaTransformada;
			
			if(resto == 0 && coma == false && i != 0) {
				cadenaTransformada = "." + cadenaTransformada;
				
			}
			
			if(saldoComa.charAt(i) == ',') {
				coma = false;
			}
			//System.out.println(cadenaTransformada);
		}
		
		return cadenaTransformada;
	}
}

