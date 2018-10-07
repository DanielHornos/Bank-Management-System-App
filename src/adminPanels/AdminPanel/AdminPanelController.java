package adminPanels.AdminPanel;


import java.io.IOException;

import LoginGUI.Connector;
import adminPanels.AddAccountPanel.AddAccountPanel;
import adminPanels.AllUsersPanel.AllUsersPanel;
import adminPanels.BalanceSheetPanel.BalanceSheetPanel;
import adminPanels.ChangePasswordPanel.AdminChangePasswordPanel;
import adminPanels.DepositPanel.DepositPanel;
import adminPanels.TransferPanel.TransferPanel;
import adminPanels.UpdateAccountPanel.UpdateAccountPanel;
import adminPanels.WidthdrawPanel.WidthdrawPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AdminPanelController {
	
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
	protected void actionForAddAccountButton(ActionEvent e) throws IOException {
		
        try {
            
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminPanels/AddAccountPanel/AddAccountPanel.fxml"));
        	
            Stage stage = new Stage();
            stage.setTitle("Add Account");
            stage.setScene(new Scene(loader.load(), 1024, 600));
            stage.show();
            // Hide this current window (if this is what you want)
//            ((Node)(e.getSource())).getScene().getWindow().hide();
        	
        	AddAccountPanel addAccountPanel = loader.<AddAccountPanel>getController();
        	addAccountPanel.setConn(conn);
        		
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
		
	}
	
	@FXML
	protected void actionForUpdateAccountButton(ActionEvent e) throws IOException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminPanels/UpdateAccountPanel/UpdateAccountPanel.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Update Account");
            stage.setScene(new Scene(loader.load(), 1024, 600));
            stage.show();
            
        	UpdateAccountPanel updateAccountPanel = loader.<UpdateAccountPanel>getController();
        	updateAccountPanel.setConn(conn);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	@FXML
	protected void actionForAllUsersButton(ActionEvent e) throws IOException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminPanels/AllUsersPanel/AllUsersPanel.fxml"));
            Stage stage = new Stage();
            stage.setTitle("All Accounts");
            stage.setScene(new Scene(loader.load(), 1024, 600));
            stage.show();

            
        	AllUsersPanel allUsersPanel = loader.<AllUsersPanel>getController();
        	allUsersPanel.setConn(conn);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	@FXML
	protected void actionForWidthdrawButton(ActionEvent e) throws IOException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminPanels/WidthdrawPanel/WidthdrawPanel.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Widthdraw");
            stage.setScene(new Scene(loader.load(), 1024, 600));
            stage.show();
            
            WidthdrawPanel widthdrawPanel = loader.<WidthdrawPanel>getController();
            widthdrawPanel.setConn(conn);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	@FXML
	protected void actionForDepositButton(ActionEvent e) throws IOException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminPanels/DepositPanel/DepositPanel.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Deposit");
            stage.setScene(new Scene(loader.load(), 1024, 600));
            stage.show();
            
            DepositPanel depositPanel = loader.<DepositPanel>getController();
            depositPanel.setConn(conn);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }		
	}
	@FXML
	protected void actionForTransferButton(ActionEvent e) throws IOException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminPanels/TransferPanel/TransferPanel.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Transfer");
            stage.setScene(new Scene(loader.load(), 1024, 600));
            stage.show();
            
            TransferPanel transferPanel = loader.<TransferPanel>getController();
            transferPanel.setConn(conn);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }	
	}
	@FXML
	protected void actionForBalanceSheetButton(ActionEvent e) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminPanels/BalanceSheetPanel/BalanceSheetPanel.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Balance Sheet");
			stage.setScene(new Scene(loader.load(), 1024, 600));
			stage.show();
			
            BalanceSheetPanel balancePanel = loader.<BalanceSheetPanel>getController();
            balancePanel.setConn(conn);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}	
	}
	@FXML
	protected void actionForChangePasswordButton(ActionEvent e) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminPanels/ChangePasswordPanel/AdminChangePasswordPanel.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Change Password");
			stage.setScene(new Scene(loader.load(), 1024, 600));
			stage.show();

			AdminChangePasswordPanel changePasswordPanel = loader.<AdminChangePasswordPanel>getController();
			changePasswordPanel.setConn(conn);
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
