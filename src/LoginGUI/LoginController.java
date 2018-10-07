package LoginGUI;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import adminPanels.AdminPanel.*;
//import application.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import userPanels.userMainPanel.UserMainPanelController;

/**
 * Login Controller.
 */
public class LoginController extends AnchorPane {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML
    TextField userId;
    @FXML
    PasswordField password;
    @FXML
    Button login;
    @FXML
    Label errorMessage;
    Connector conn;

    public void setApp(Main application){
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        errorMessage.setText("");
    }

    public void processLogin(ActionEvent event) {
    	
    	try {
    		conn = new Connector(userId.getText(), password.getText());
    		if(conn.open()&&conn.isAdmin) {
    			System.out.println("The connection was succesful as Admin!");
    			adminNextWindow();
    		}else if(conn.open()&&!conn.isAdmin) {
    			System.out.println("The connection was succesfull as User!");
    			userNextWindow();   			
    		}else {
    			errorMessage.setText("Incorrect User or Password: " + userId.getText());
    		}		

    	}catch (Exception e) {
    		e.printStackTrace();
    	}

    }
    
    private void userNextWindow()  throws IOException{
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();

    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/userPanels/UserMainPanel/UserMainPanel.fxml"));
    	Scene scene = new Scene(loader.load(), 1024, 600);
   	   	UserMainPanelController userPanelcontroller = loader.<UserMainPanelController>getController();
   	   	userPanelcontroller.setConn(conn);

    	Stage primaryStage = Main.getPrimaryStage();
    	
    	primaryStage.setX(bounds.getMinX());
    	primaryStage.setY(bounds.getMinY());
    	primaryStage.setWidth(bounds.getWidth());
    	primaryStage.setHeight(bounds.getHeight());
    	
    	primaryStage.setScene(scene);
    	primaryStage.setTitle("User Panel");
    	primaryStage.setResizable(true);
    	primaryStage.show();
		
	}

	public void adminNextWindow() throws IOException {
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();

    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminPanels/AdminPanel/AdminPanel.fxml"));
    	Scene scene = new Scene(loader.load(), 1024, 600);
   	   	AdminPanelController controller = loader.<AdminPanelController>getController();
   	   	controller.setConn(conn);

    	Stage primaryStage = Main.getPrimaryStage();
    	
    	primaryStage.setX(bounds.getMinX());
    	primaryStage.setY(bounds.getMinY());
    	primaryStage.setWidth(bounds.getWidth());
    	primaryStage.setHeight(bounds.getHeight());
    	
    	primaryStage.setScene(scene);
    	primaryStage.setTitle("Admin Panel");
    	primaryStage.setResizable(true);
    	primaryStage.show();
    	
    }
}
