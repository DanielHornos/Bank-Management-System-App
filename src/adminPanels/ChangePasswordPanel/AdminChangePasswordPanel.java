package adminPanels.ChangePasswordPanel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;

import LoginGUI.Connector;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AdminChangePasswordPanel {

	@FXML private TextField account = new TextField();
	@FXML private PasswordField newPassword = new PasswordField();
	@FXML private PasswordField repeatNewPassword = new PasswordField();
	
    //TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;
    @FXML private TableView tableview;

	@FXML ComboBox<String> comboBox = new ComboBox<>();
	
	static Connector conn;
	private String currentPassword;
	
	public Connector getConn() {
		return conn;
	}
	
	public void setConn(Connector conn) {
		this.conn = conn;
	}

	@FXML
	protected void actionForConfirmPasswordChangeButton(ActionEvent e) throws IOException {
		
		 try {
			Statement statement = getConn().getStat();
			String st1 = " SELECT * FROM Bank.dbo.BankAccounts WHERE AccountNr = '"
					+ account.getText()
					+ "'";
			ResultSet rs = statement.executeQuery(st1);

			if (!rs.isBeforeFirst() ) { 
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Wrong Account Number");
				alert.setHeaderText(null);
				alert.setContentText("The account number doesn't exist."+System.lineSeparator()+"Please introduce another Account Number.");
				alert.showAndWait();
			    return;
			}
			}catch(Exception exc) {
				System.err.println("Error!: " + exc.getMessage());
			}
		 
		 
			try {			
				Statement statement = getConn().getStat();
				String sql = "SELECT Password FROM Bank.dbo.UserPassword WHERE AccountNr = '" + account.getText() + "'";
				ResultSet resultSetBalance = statement.executeQuery(sql);
				resultSetBalance.next();
				currentPassword = resultSetBalance.getString(1);	
				}catch(Exception exc) {
					System.err.println("ERROR: " + exc.getMessage());
				}
			
			
			if(newPassword.getText().equals(repeatNewPassword.getText())) {
				

				try {			
				Statement statement = getConn().getStat();
				String st1 = "UPDATE Bank.dbo.UserPassword SET Password = '"
						+ newPassword.getText()
						+ "' WHERE AccountNr = '"
						+ account.getText()
						+ "';";
				ResultSet rs = statement.executeQuery(st1);
				}catch(Exception exc) {
					if(exc.getMessage().equals("The statement did not return a result set.")) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Password changed");
					alert.setHeaderText(null);
					alert.setContentText("The account's password was changed successfully!");
					alert.showAndWait();
					}
					System.err.println("Error!: " + exc.getMessage());
				}
			}else
			if(!newPassword.getText().equals(repeatNewPassword.getText())){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Wrong Password");
				alert.setHeaderText(null);
				alert.setContentText("New Password and Repeat New Password does not match."+System.lineSeparator()+"Please introduce correct new password.");
				alert.showAndWait();
			    return;
			}


		
	}
}
