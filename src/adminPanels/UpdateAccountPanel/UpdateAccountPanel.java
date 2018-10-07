package adminPanels.UpdateAccountPanel;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;


import LoginGUI.Connector;
import LoginGUI.Main;
import adminPanels.AdminPanel.AdminPanelController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class UpdateAccountPanel {
	
	@FXML private TextField id = new TextField();
	@FXML private TextField name = new TextField();
	@FXML private DatePicker dateOfBirth = new DatePicker();
	@FXML private TextField dni = new TextField();
	@FXML private TextField email = new TextField();
	@FXML private TextField telephone = new TextField();
	@FXML private TextField address = new TextField();
	@FXML private TextField nationality = new TextField();
	@FXML private TextField balance = new TextField();
	@FXML private TextArea description = new TextArea();
	final ToggleGroup gender = new ToggleGroup();
	final ToggleGroup maritialStatus = new ToggleGroup();
	@FXML private RadioButton rbMale = new RadioButton("Male");
	@FXML private RadioButton rbFemale = new RadioButton("Female");
	@FXML private RadioButton rbMarried = new RadioButton("Married");
	@FXML private RadioButton rbUnmarried = new RadioButton("Unmarried");

	@FXML private TextField search = new TextField();
	
	@FXML ComboBox<String> comboBox = new ComboBox<>();
	final Tooltip tooltip = new Tooltip();
	
    //TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;
    @FXML private TableView tableview;

	Connector conn;

	public void initialize() {
		
		rbMale.setUserData("Male");
		rbMale.setToggleGroup(gender);
		
		rbFemale.setUserData("Female");
		rbFemale.setToggleGroup(gender);
		
		rbMarried.setUserData("Married");
		rbMarried.setToggleGroup(maritialStatus);
		
		rbUnmarried.setUserData("Unmarried");
		rbUnmarried.setToggleGroup(maritialStatus);
		
		comboBox.getItems().removeAll(comboBox.getItems());
	    comboBox.getItems().addAll("Account Nr", "Name", "IDNumber", "Address", "Email", "Telephone");
	    comboBox.getSelectionModel().select("Name");

	}
	
	public Connector getConn() {
		return conn;
	}
	
	public void setConn(Connector conn) {
		this.conn = conn;
	}

	@FXML
	protected void actionForAddAccountButton(ActionEvent e) throws IOException {

	}
	
	@FXML
	protected void actionForUploadPhotoButton(ActionEvent e) throws IOException {
		
	}
	
	@FXML
	protected void actionForUpdateButton(ActionEvent e) throws SQLException {
		
		 try {
				Statement statement = getConn().getStat();
				String st1 = "  SELECT * FROM Bank.dbo.BankAccounts WHERE IDNumber = '"
						+ dni.getText()
						+ "' EXCEPT (SELECT * FROM Bank.dbo.BankAccounts WHERE AccountNr = '"
						+ id.getText()
						+ "');";
				ResultSet rs = statement.executeQuery(st1);

				if (rs.isBeforeFirst() ) { 
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Existing IDNumber");
					alert.setHeaderText(null);
					alert.setContentText( "The ID Number already exist."+System.lineSeparator()+"Please introduce another ID Number.");
					alert.showAndWait();
				    return;
				}
			}catch(Exception exc) {
				System.err.println("Error!: " + exc.getMessage());
			}
		
		try {
			if(id.getText()!="") {
				
				String sql = "UPDATE Bank.dbo.BankAccounts SET "
						+ "Name = '" + name.getText() + "'"
						+ ",IDNumber = '" + dni.getText() + "'"
						+ ",DateOfBirth = '" + dateOfBirth.getValue() + "'"
						+ ",Nationality = '" + nationality.getText() + "'"
						+ ",Address = '" + address.getText() + "'"
						+ ",Gender = '" + gender.getSelectedToggle().getUserData().toString() + "'"
						+ ",MaritialStatus = '" + maritialStatus.getSelectedToggle().getUserData().toString() + "'"
						+ ",Email = '" + email.getText() + "'"
						+ ",Telephone = '" + telephone.getText() + "'"
						+ ",Balance = '" + balance.getText() + "'"
						+ "WHERE AccountNr= " + id.getText();
				
				Statement statement = getConn().getStat();
				ResultSet rs = statement.executeQuery(sql);
				buildData(rs);
			}
		}catch(Exception exc) {
			System.err.println("ERROR: " + exc.getMessage());
		}
		RefreshAction();
	}
	
	@FXML  // Al volver atras la conexion no se queda guardada, y no se puede interactuar con la database
	protected void actionForBackArrowButton(ActionEvent e) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPanel/AdminPanel.fxml"));
    	Scene scene = new Scene(loader.load(), 1024, 600);
   	   	AdminPanelController controller = loader.<AdminPanelController>getController();
    	controller.setConn(conn);

    	Stage primaryStage = Main.getPrimaryStage();
    	primaryStage.setScene(scene);
    	primaryStage.setTitle("Database");
    	primaryStage.setResizable(true);
    	primaryStage.show();
	}
	
	@FXML
	protected void actionForRefreshButton(ActionEvent e) throws SQLException {
		RefreshAction();
	}
	
	@FXML
	protected void actionForSelectedRow(MouseEvent e) {
        System.out.println("Selected row: " + tableview.getSelectionModel().getSelectedItem());
        
        String row = tableview.getSelectionModel().getSelectedItem().toString();
        String sId = row.substring(1, ordinalIndexOf(row, ",", 1));
        String sName = row.substring(ordinalIndexOf(row, ",", 1)+2, ordinalIndexOf(row, ",", 2));
        String sDni = row.substring(ordinalIndexOf(row, ",", 2)+2, ordinalIndexOf(row, ",", 3));
        String sDateOfBirth = row.substring(ordinalIndexOf(row, ",", 3)+2, ordinalIndexOf(row, ",", 4));
        String sNationality = row.substring(ordinalIndexOf(row, ",", 4)+2, ordinalIndexOf(row, ",", 5));
        String sAddress = row.substring(ordinalIndexOf(row, ",", 5)+2, ordinalIndexOf(row, ",", 6));
        String sGender = row.substring(ordinalIndexOf(row, ",", 6)+2, ordinalIndexOf(row, ",", 7));
        String sMaritialStatus = row.substring(ordinalIndexOf(row, ",", 7)+2, ordinalIndexOf(row, ",", 8));
        String sEmail = row.substring(ordinalIndexOf(row, ",", 8)+2, ordinalIndexOf(row, ",", 9));
        String sTelephone = row.substring(ordinalIndexOf(row, ",", 9)+2, ordinalIndexOf(row, ",", 10));
        String sBalance = row.substring(ordinalIndexOf(row, ",", 10)+2, row.length()-1);

        
		id.setText(sId);
		name.setText(sName);
		address.setText(sAddress);
		dni.setText(sDni);
		email.setText(sEmail);
		nationality.setText(sNationality);
		telephone.setText(sTelephone);
		balance.setText(sBalance);
		
		// set gender
		if(sGender.equals("Male")) {
			gender.selectToggle(rbMale);
		}
		if(sGender.equals("Female")) {
			gender.selectToggle(rbFemale);
		}
		
		// set martial status
		if(sMaritialStatus.equals("Married")) {
			maritialStatus.selectToggle(rbMarried);
		}
		if(sMaritialStatus.equals("Unmarried")) {
			maritialStatus.selectToggle(rbUnmarried);
		}
		
		//set date of birth
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		LocalDate localDate = LocalDate.parse(sDateOfBirth, formatter);
		dateOfBirth.setValue(localDate);
		
	}
	
	private static int ordinalIndexOf(String str, String substr, int n) {
	    int pos = str.indexOf(substr);
	    while (--n > 0 && pos != -1)
	        pos = str.indexOf(substr, pos + 1);
	    return pos;
	}
	
    //CONNECTION DATABASE
    public void buildData(ResultSet rs){
          
          data = FXCollections.observableArrayList();
          try{

            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;                
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });

                tableview.getColumns().addAll(col); 
                System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);
          }catch(Exception e){
              e.printStackTrace();
              System.out.println("Error on Building Data");             
          }
      }
	
	private void RefreshAction() throws SQLException {
		try {
		tableview.getColumns().clear();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		String sql = "SELECT * FROM Bank.dbo.BankAccounts";
        Statement statement = getConn().getStat();
        ResultSet rs = statement.executeQuery(sql);
        buildData(rs);
	}
	
	@FXML
	protected void actionForSearchButton(ActionEvent e) {
		tableview.getColumns().clear();
		try {			
			Statement statement = getConn().getStat();
			String sql = "SELECT * FROM Bank.dbo.BankAccounts";
			switch(comboBox.getValue()) {
			case "Account Nr": sql = "SELECT AccountNr, Name, IDNumber, DateOfBirth, Nationality, Address, Gender, MaritialStatus, Email, Telephone, Balance FROM Bank.dbo.BankAccounts WHERE AccountNr LIKE '%" + search.getText() + "%'";
				break;
			case "Name": sql = "SELECT AccountNr, Name, IDNumber, DateOfBirth, Nationality, Address, Gender, MaritialStatus, Email, Telephone, Balance FROM Bank.dbo.BankAccounts WHERE Name LIKE '%" + search.getText() + "%'";
				break;
			case "IDNumber": sql = "SELECT AccountNr, Name, IDNumber, DateOfBirth, Nationality, Address, Gender, MaritialStatus, Email, Telephone, Balance FROM Bank.dbo.BankAccounts WHERE IDNumber LIKE '%" + search.getText() + "%'";
				break;
			case "Address": sql = "SELECT AccountNr, Name, IDNumber, DateOfBirth, Nationality, Address, Gender, MaritialStatus, Email, Telephone, Balance FROM Bank.dbo.BankAccounts WHERE Address LIKE '%" + search.getText() + "%'";
				break;
			case "Email": sql = "SELECT AccountNr, Name, IDNumber, DateOfBirth, Nationality, Address, Gender, MaritialStatus, Email, Telephone, Balance FROM Bank.dbo.BankAccounts WHERE Email LIKE '%" + search.getText() + "%'";
				break;
			case "Telephone": sql = "SELECT AccountNr, Name, IDNumber, DateOfBirth, Nationality, Address, Gender, MaritialStatus, Email, Telephone, Balance FROM Bank.dbo.BankAccounts WHERE Telephone LIKE '%" + search.getText() + "%'";
				break;
			default: 
				break;
			}
			ResultSet rs = statement.executeQuery(sql);
			buildData(rs);
		}catch(Exception exc) {
			System.err.println("ERROR");
		}
	}
	

}
