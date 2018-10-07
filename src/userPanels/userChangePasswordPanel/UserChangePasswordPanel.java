package userPanels.userChangePasswordPanel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;

import LoginGUI.Connector;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;

public class UserChangePasswordPanel {

	@FXML private PasswordField oldPassword = new PasswordField();
	@FXML private PasswordField newPassword = new PasswordField();
	@FXML private PasswordField repeatNewPassword = new PasswordField();
	
    //TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;
    @FXML private TableView tableview;

	@FXML ComboBox<String> comboBox = new ComboBox<>();
	
	static Connector conn;
	
	public Connector getConn() {
		return conn;
	}
	
	public void setConn(Connector conn) {
		this.conn = conn;
	}

	@FXML
	protected void actionForConfirmPasswordChangeButton(ActionEvent e) throws IOException {
		
		System.out.println(oldPassword.getText());
		System.out.println(newPassword.getText());
		System.out.println(repeatNewPassword.getText());
		
		System.out.println("is old equals old?" + oldPassword.getText().equals(conn.getPassword()));
		System.out.println("is old = old?" + oldPassword.getText() == conn.getPassword());

			if(oldPassword.getText().equals(conn.getPassword()) && newPassword.getText().equals(repeatNewPassword.getText())) {
				

				try {			
				Statement statement = getConn().getStat();
				String st1 = "UPDATE Bank.dbo.UserPassword SET Password = '"
						+ newPassword.getText()
						+ "' WHERE AccountNr = '"
						+ conn.getAccountNr()
						+ "';";
				ResultSet rs = statement.executeQuery(st1);
				}catch(Exception exc) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Password changed");
					alert.setHeaderText(null);
					alert.setContentText("The account's password was changed successfully!");
					alert.showAndWait();
					System.err.println("Error!: " + exc.getMessage());
				}
			}else 
			if(!oldPassword.getText().equals(conn.getPassword())){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Wrong Old Password");
				alert.setHeaderText(null);
				alert.setContentText("Old Password introduced is not correct."+System.lineSeparator()+"Please introduce correct old password.");
				alert.showAndWait();
			    return;
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
