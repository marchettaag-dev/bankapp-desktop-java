package ui;

import java.io.IOException;

import controller.ConfiguracionController;
import controller.DashboardAdminController;
import controller.DashboardClienteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Cliente;
import model.Cuenta;
import model.Usuario;

public class Navegador {

	private static Stage stagePrincipal;
	
	// Se ejecuta una sola vez en el Main para guardar la ventana principal
	public static void 	setStagePrincipal(Stage stage) {
		stagePrincipal = stage;
	}
	
	// Método genérico para ir a pantallas simples (como Login o Registro)
    public static void irA(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(rutaFxml));
            Parent root = loader.load();
            stagePrincipal.setScene(new Scene(root));
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método específico para ir al Dashboard del CLIENTE PASANDO DATOS
    public static void irADashboardCliente(Cliente clienteLogeado, Cuenta cuenta) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegador.class.getResource("/fxml/DashboardCliente.fxml"));
            Parent root = loader.load();

            // Obtenemos el controlador e inyectamos los datos directamente acá
            DashboardClienteController controller = loader.getController();
            controller.iniciarDatos(clienteLogeado, cuenta);

            stagePrincipal.setScene(new Scene(root));
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Método específico para ir al Dashboard del ADMIN
    public static void irADashboardAdmin() {
    	try {
    		FXMLLoader loader = new FXMLLoader(Navegador.class.getResource("/fxml/DashboardAdmin.fxml"));
            Parent root = loader.load();


            stagePrincipal.setScene(new Scene(root));
            
            // Obtenemos el controlador e inyectamos los datos directamente acá
            DashboardAdminController controller = loader.getController();
            controller.iniciarDatos();
            
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void irAConfiguracio(Cliente cliente, Cuenta cuenta) {
    	try {
    		FXMLLoader loader = new FXMLLoader(Navegador.class.getResource("/fxml/configuracion.fxml"));
            Parent root = loader.load();


            stagePrincipal.setScene(new Scene(root));
            
            // Obtenemos el controlador e inyectamos los datos directamente acá
            ConfiguracionController controller = loader.getController();
            controller.iniciarDatos(cliente, cuenta);
            
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
