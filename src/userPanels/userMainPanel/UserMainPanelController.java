package userPanels.userMainPanel;


import java.io.IOException;

import LoginGUI.Connector;
import LoginGUI.Main;
import adminPanels.AddAccountPanel.AddAccountPanel;
import adminPanels.AllUsersPanel.AllUsersPanel;
import adminPanels.BalanceSheetPanel.BalanceSheetPanel;
import adminPanels.DepositPanel.DepositPanel;
import adminPanels.TransferPanel.TransferPanel;
import adminPanels.UpdateAccountPanel.UpdateAccountPanel;
import adminPanels.WidthdrawPanel.WidthdrawPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import userPanels.UserWidthdrawPanel.UserWidthdrawPanel;
import userPanels.userBalanceSheetPanel.UserBalanceSheetPanel;
import userPanels.userChangePasswordPanel.UserChangePasswordPanel;
import userPanels.userDepositPanel.UserDepositPanel;
import userPanels.userTransferPanel.UserTransferPanel;

public class UserMainPanelController {
	
	Connector conn;

	public void initialize() {

		
		
	}
	
	public Connector getConn() {
		return conn;
	}
	
	public void setConn(Connector conn) {
		this.conn = conn;
	}

	
	@FXML
	protected void actionForWidthdrawButton(ActionEvent e) throws IOException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/userPanels/UserWidthdrawPanel/UserWidthdrawPanel.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Widthdraw");
            stage.setScene(new Scene(loader.load(), 1024, 600));
            stage.show();
            // Hide this current window (if this is what you want)
//            ((Node)(e.getSource())).getScene().getWindow().hide();
            
            UserWidthdrawPanel userWidthdrawPanel = loader.<UserWidthdrawPanel>getController();
            userWidthdrawPanel.setConn(conn);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	@FXML
	protected void actionForDepositButton(ActionEvent e) throws IOException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/userPanels/userDepositPanel/UserDepositPanel.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Deposit");
            stage.setScene(new Scene(loader.load(), 1024, 600));
            stage.show();
            // Hide this current window (if this is what you want)
//            ((Node)(e.getSource())).getScene().getWindow().hide();
            
            UserDepositPanel userDepositPanel = loader.<UserDepositPanel>getController();
            userDepositPanel.setConn(conn);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }		
	}
	@FXML
	protected void actionForTransferButton(ActionEvent e) throws IOException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/userPanels/userTransferPanel/UserTransferPanel.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Transfer");
            stage.setScene(new Scene(loader.load(), 1024, 600));
            stage.show();
            // Hide this current window (if this is what you want)
//            ((Node)(e.getSource())).getScene().getWindow().hide();
            
            UserTransferPanel userTransferPanel = loader.<UserTransferPanel>getController();
            userTransferPanel.setConn(conn);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }	
	}
	@FXML
	protected void actionForBalanceSheetButton(ActionEvent e) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/userPanels/userBalanceSheetPanel/UserBalanceSheetPanel.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Balance Sheet");
			stage.setScene(new Scene(loader.load(), 1024, 600));
			stage.show();
			// Hide this current window (if this is what you want)
//            ((Node)(e.getSource())).getScene().getWindow().hide();
			
            UserBalanceSheetPanel userBalancePanel = loader.<UserBalanceSheetPanel>getController();
            userBalancePanel.setConn(conn);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}	
	}
	@FXML
	protected void actionForChangePasswordButton(ActionEvent e) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/userPanels/userChangePasswordPanel/UserChangePasswordPanel.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Change Password");
			stage.setScene(new Scene(loader.load(), 1024, 600));
			stage.show();
			// Hide this current window (if this is what you want)
//            ((Node)(e.getSource())).getScene().getWindow().hide();
			
            UserChangePasswordPanel userChangePasswordPanel = loader.<UserChangePasswordPanel>getController();
            userChangePasswordPanel.setConn(conn);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}	
	}
	@FXML
	protected void actionForAboutButton(ActionEvent e) throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About Bank Management System App");
		alert.setHeaderText(null);
		alert.setContentText("This application was developed by Daniel Hornos Valiente" + System.lineSeparator() + 
				"For more software developed by me, please visit: https://github.com/DanielHornos");
		alert.showAndWait();	
	}

	@FXML
	protected void actionForExitButton(ActionEvent e) throws IOException {
		System.exit(0);
	}
	
	

   	
 
}
