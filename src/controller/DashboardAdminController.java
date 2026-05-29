package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import model.Categoria;
import model.Cliente;
import model.Cuenta;
import model.TipoTransaccion;
import model.Transaccion;
import model.Usuario;
import service.CuentaService;
import service.TransaccionService;
import service.UsuarioService;
import ui.Navegador;

public class DashboardAdminController implements Initializable{

	@FXML
	private TextField txtBuscador;
	
	@FXML
	private MenuBar mbtCuenta;
	
	@FXML
	private MenuButton mbtnCuenta;
	
	@FXML
	private MenuItem iCambiarCuenta;
	
	@FXML
	private MenuItem iCerrarSesion;
	
	@FXML
	private Button btnControl;
	
	@FXML
	private TabPane tablaMuestra;
	
	@FXML
	private TableView<Cliente> tablaClientes;
	
	@FXML
	private TableColumn<Cliente, Long> colID;
	
	@FXML
	private TableColumn<Cliente, String> colNombre;
	
	@FXML
	private TableColumn<Cliente, String> colApellido;
	
	@FXML
	private TableColumn<Cliente, String> colEmail;
	
	@FXML
	private ListView<Cuenta> listViewCuentasClientes;
	
	@FXML
	private TableView<Cuenta> tablaCuentas;
	
	@FXML
	private TableColumn<Cuenta, Long> colNumeroCuenta;
	
	@FXML
	private TableColumn<Cuenta, String> colSaldo;
	
	@FXML
	private TableColumn<Cuenta, String> colFechaCreacion;
	
	@FXML
	private TableColumn<Cuenta, Long> colClienteID;
	
	@FXML
	private TableView<Transaccion> tablaTransacciones;
	
	@FXML
	private TableColumn<Transaccion, Long> colNumeroTransaccion;
	
	@FXML
	private TableColumn<Transaccion, String> colTipo;
	
	@FXML
	private TableColumn<Transaccion, String> colMonto;
	
	@FXML
	private TableColumn<Transaccion, String> colFechaHora;
	
	@FXML
	private TableView<Cuenta> tablaCuentas1;
	
	@FXML
	private TableColumn<Cuenta, Long> colNumeroCuenta1;
	
	@FXML
	private TableColumn<Cuenta, String> colSaldo1;
	
	@FXML
	private TableColumn<Cuenta, String> colFechaCreacion1;
	
	@FXML
	private TableColumn<Cuenta, Long> colClienteID1;
	
	@FXML
	private TableView<Cuenta> tablaCuentas2;
	
	@FXML
	private TableColumn<Cuenta, Long> colNumeroCuenta2;
	
	@FXML
	private TableColumn<Cuenta, String> colSaldo2;
	
	@FXML
	private TableColumn<Cuenta, String> colFechaCreacion2;
	
	@FXML
	private TableColumn<Cuenta, Long> colClienteID2;
	
	private UsuarioService usuarioService;
	
	private CuentaService cuentaService;
	
	private TransaccionService transaccionService;
	
	ObservableList<Cliente> listaOriginalCliente = FXCollections.observableArrayList();
	
	FilteredList<Cliente> listaFiltradaCliente = new FilteredList<>(listaOriginalCliente, p -> true);
	
	ObservableList<Cuenta> listaOriginalCuenta = FXCollections.observableArrayList();
	
	FilteredList<Cuenta> listaFiltradaCuenta = new FilteredList<>(listaOriginalCuenta, p -> true);
	
	ObservableList<Transaccion> listaOriginalTransaccion = FXCollections.observableArrayList();
	
	FilteredList<Transaccion> listaFiltradaTransaccion = new FilteredList<>(listaOriginalTransaccion, p -> true);
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		 // 1. Configurar columnas de la Tabla de Clientes
	    colID.setCellValueFactory(new PropertyValueFactory<>("Id"));
	    colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
	    colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
	    colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
	    
	    // 2. Configurar columnas de la Tabla de Cuentas
	    colNumeroCuenta.setCellValueFactory(new PropertyValueFactory<>("numeroCuenta")); // Ajustá al nombre real en tu clase Cuenta
	    colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
	    colFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
	    colClienteID.setCellValueFactory(new PropertyValueFactory<>("IdCliente"));
	    
	    colNumeroCuenta1.setCellValueFactory(new PropertyValueFactory<>("numeroCuenta")); 
	    colSaldo1.setCellValueFactory(new PropertyValueFactory<>("saldo"));
	    colFechaCreacion1.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
	    colClienteID1.setCellValueFactory(new PropertyValueFactory<>("IdCliente"));
	    
	    colNumeroCuenta2.setCellValueFactory(new PropertyValueFactory<>("numeroCuenta")); 
	    colSaldo2.setCellValueFactory(new PropertyValueFactory<>("saldo"));
	    colFechaCreacion2.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
	    colClienteID2.setCellValueFactory(new PropertyValueFactory<>("IdCliente"));	    
	    
	    // 3. Configurar columnas de la Tabla de Transaccion
	    colNumeroTransaccion.setCellValueFactory(new PropertyValueFactory<>("NumeroTransaccion"));
	    colTipo.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
	    colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
	    colFechaHora.setCellValueFactory(new PropertyValueFactory<>("FechaYHora"));
	    
	    // ESCUCHAR LOS CAMBIOS DE PESTAÑA (Al TabPane directamente)
	    this.tablaMuestra.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
	        
	        // newValue nos da el número de pestaña seleccionada (0, 1, 2...)
	        int pestanaSeleccionada = newValue.intValue();
	        
	        
	        //System.out.println("La seleccion :"+tablaClientes.getSelectionModel().getSelectedItem());
	        
	        switch (pestanaSeleccionada) {
	            case 0:
	                //System.out.println("Se seleccionó la pestaña de Clientes");
	                this.recibirBuscador(Categoria.CLIENTES);
	                this.tablaClientes.setItems(this.listaFiltradaCliente);
	                break;
	                
	            case 1:
	                //System.out.println("Se seleccionó la pestaña de Cuentas");
	                this.recibirBuscador(Categoria.CUENTAS);
	                this.tablaCuentas.setItems(this.listaFiltradaCuenta);
	                break;
	                
	            case 2:
	                //System.out.println("Se seleccionó la pestaña de Transacciones");
	                this.recibirBuscador(Categoria.TRANSACCIONES);
	                this.tablaTransacciones.setItems(this.listaFiltradaTransaccion);
	                break;
	        }
	    });
		
	}
	
	public void iniciarDatos() {
		
		this.usuarioService = new UsuarioService();
		this.transaccionService = new TransaccionService();
		this.cuentaService = new CuentaService(usuarioService, transaccionService);
		
		//System.out.println("Mostrando Datos...");
		this.recibirBuscador(Categoria.CLIENTES);
		
		//System.out.println("La lista filtrada es: "+this.listaFiltradaCliente);
		this.tablaClientes.setItems(this.listaFiltradaCliente);
	}
	
	@FXML
	private void control(ActionEvent a) {
		
		if( this.tablaMuestra.getSelectionModel().getSelectedIndex() == 0) {
			Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();
			this.mostrarCuentasClientes(cliente);
		}
		
		if(this.tablaMuestra.getSelectionModel().getSelectedIndex()== 2) {
			Transaccion transferencia = tablaTransacciones.getSelectionModel().getSelectedItem();
			if(transferencia.getTipo().equals(TipoTransaccion.TRANSFERENCIA)) {
				System.out.println("Entro");
				this.mostrarCuentasClientes(transferencia.getId());
			}
		}
	}
	
	@FXML
	private void cerrarSesion(ActionEvent a) {
		
		Navegador.irA("/fxml/login.fxml");
	}
		
	// ================================= Metodos Privados ================================================
	
	private void recibirBuscador(Categoria categoria) {
		
		switch(categoria) {
		case CLIENTES:
			
			// 1. Traer los datos del servicio
			List<Cliente> clientes = this.usuarioService.buscarTodosClientes();
			
			// 2. Modificar la lista existente en vez de reemplazarla
            this.listaOriginalCliente.setAll(clientes); // Modifica el contenido, no la instancia
			
            
			this.txtBuscador.textProperty().addListener((observable, oldValue, newValue) -> {
			    this.listaFiltradaCliente.setPredicate(usuario -> {
			    	
			        // Si el TextField está vacío, mostrar todas los clientes
			        if (newValue == null || newValue.isEmpty()) {
			            return true;
			        }
			        
			        // Agarramos el la palabra q se quiere buscar
			        String filtro = newValue.toLowerCase();

			        return usuario.getNombre().toLowerCase().contains(filtro)
			        		|| usuario.getEmail().toLowerCase().contains(filtro);
			    });
			});
			break;
		case CUENTAS:
			
			// 1. Traer los datos del servicio
			List<Cuenta> cuentas = this.cuentaService.buscarTodasLasCuentas();
			
			// 2. Modificar la lista existente en vez de reemplazarla
			this.listaOriginalCuenta.setAll(cuentas);
			
			this.txtBuscador.textProperty().addListener((observable, oldValue, newValue) -> {
			    this.listaFiltradaCuenta.setPredicate(cuenta -> {
			    	
			        // Si el TextField está vacío, mostrar todas las cuentas
			        if (newValue == null || newValue.isEmpty()) {
			            return true;
			        }
			        
			        // Agarramos el la palabra q se quiere buscar
			        String filtro = newValue.toLowerCase();

			        return cuenta.getNumeroCuenta().contains(filtro);        
			    });
			});
			
			break;
		case TRANSACCIONES:
			
			// 1. Traer los datos del servicio
			List<Transaccion> transacciones = this.transaccionService.buscarTodasLasTransacciones();
						
			// 2. Modificar la lista existente en vez de reemplazarla
			this.listaOriginalTransaccion.setAll(transacciones);
						
			this.txtBuscador.textProperty().addListener((observable, oldValue, newValue) -> {
				this.listaFiltradaTransaccion.setPredicate(transaccion -> {
						    	
					// Si el TextField está vacío, mostrar todas las cuentas
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
						        
					// Agarramos el la palabra q se quiere buscar
					String filtro = newValue.toLowerCase();
				
					return transaccion.getTipoMensaje().contains(filtro);
				});
			});
			break;
		}
	}
	
	private void mostrarCuentasClientes(Cliente cliente) {
		
		List<Cuenta> listaCuentasUsuario = this.cuentaService.buscarPorCliente(cliente.getId());
		
		ObservableList<Cuenta> listaCuentas = FXCollections.observableArrayList();
		
		listaCuentas.setAll(listaCuentasUsuario);
		
		this.tablaCuentas1.setItems(listaCuentas);
	}
	
	private void mostrarCuentasClientes(Long id) {
		Transaccion t = this.transaccionService.buscarTodosPorId(id).get();
		
		List<Cuenta> listaCuentas = new ArrayList<>();
		
		Long id1 = t.getIdCuentaDestino();
		long id2 = t.getIdCuentaOrigen();
		
		Cuenta cuenta1 = this.cuentaService.buscarPorIdCuenta(id1).get();
		Cuenta cuenta2 = this.cuentaService.buscarPorIdCuenta(id2).get();
		
		listaCuentas.add(cuenta1);
		listaCuentas.add(cuenta2);
		
		System.out.println(listaCuentas.getFirst());
		
		ObservableList<Cuenta> listaCuentasObservable = FXCollections.observableArrayList();
		
		listaCuentasObservable.setAll(listaCuentas);
		
		this.tablaCuentas2.setItems(listaCuentasObservable);
	}
}
