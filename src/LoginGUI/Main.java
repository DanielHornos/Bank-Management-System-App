package LoginGUI;
	
import java.util.Properties;

import javax.swing.JFrame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	
	
	private final double MINIMUM_WINDOW_WIDTH = 420.0;
    private final double MINIMUM_WINDOW_HEIGHT = 450.0;
    public static Scene mainLoginScene;
    public static Stage mainLoginStage;

    private static Stage primaryStage; // **Declare static Stage**

    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return Main.primaryStage;
    }
    
	@Override
	public void start(Stage primaryStage) {
		try {
			setPrimaryStage(primaryStage); // **Set the Stage**
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			primaryStage.setScene(new Scene(root, MINIMUM_WINDOW_WIDTH, MINIMUM_WINDOW_HEIGHT));
	        primaryStage.setTitle("Bank Management System Application");
	        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("key-icon.png")));
	        primaryStage.show();	
//	        mainLoginScene = primaryStage.getScene();
	        mainLoginStage = primaryStage;       
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}	
    
	public static void main(String[] args) {
		launch(args);
	}
	
	public void closeStage() {
		mainLoginStage.close();
	}
	

}