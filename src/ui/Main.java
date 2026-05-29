package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.DatabaseInitializer;

public class Main extends Application {
	
	// Usamos init() para preparar la BD antes de que los controladores cobren vida
    @Override
    public void init() throws Exception {
        System.out.println("[INFO] Inicializando base de datos y creando tablas...");
        DatabaseInitializer.inicializar();
    }

    @Override
    public void start(Stage primaryStage) throws IOException { //Stage es la ventana de la applicacion, SOLO HAY UNA!!
    																
    	 // Inicializamos el navegador con la ventana principal
    	Navegador.setStagePrincipal(primaryStage);
    	
    	 // Cargamos la primera pantalla
        Navegador.irA("/fxml/login.fxml");
        
        primaryStage.setTitle("BankAPP");
        primaryStage.show();
        
    }

    public static void main(String[] args) {  	
        Application.launch(args);
    }
}