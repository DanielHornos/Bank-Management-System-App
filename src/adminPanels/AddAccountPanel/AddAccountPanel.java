package adminPanels.AddAccountPanel;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AddAccountPanel {
	
//	@FXML private TextField id = new TextField();
	@FXML private TextField name = new TextField();
	@FXML private DatePicker dateOfBirth = new DatePicker();
	@FXML private TextField dni = new TextField();
	@FXML private TextField email = new TextField();
	@FXML private TextField telephone = new TextField();
	@FXML private TextField address = new TextField();
	@FXML private TextField nationality = new TextField();
	@FXML private TextField balance = new TextField();
	@FXML private TextArea description = new TextArea();
	
	@FXML private TextField username = new TextField();
	@FXML private PasswordField password = new PasswordField();
	@FXML ComboBox<String> accountType = new ComboBox<>();

	final ToggleGroup gender = new ToggleGroup();
	final ToggleGroup maritialStatus = new ToggleGroup();
	@FXML private RadioButton rbMale = new RadioButton("Male");
	@FXML private RadioButton rbFemale = new RadioButton("Female");
	@FXML private RadioButton rbMarried = new RadioButton("Married");
	@FXML private RadioButton rbUnmarried = new RadioButton("Unmarried");

	// Objects for Open action
	FileChooser fileChooser = new FileChooser();	
	Stage stage = new Stage();

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

		accountType.getItems().removeAll(accountType.getItems());
		accountType.getItems().addAll("User", "Admin");
		accountType.getSelectionModel().select("User");

	}
	
	public Connector getConn() {
		return conn;
	}
	
	public void setConn(Connector conn) {
		this.conn = conn;
	}

	@FXML
	protected void actionForAddAccountButton(ActionEvent e) throws IOException, SQLException {
	

		
		try {
			if(name.getText().isEmpty() || 
					dni.getText().isEmpty() || 
					dateOfBirth.getValue()==null ||
					nationality.getText().isEmpty() ||
					address.getText().isEmpty() ||
					gender.getSelectedToggle().getUserData().toString().isEmpty() ||
					maritialStatus.getSelectedToggle().getUserData().toString().isEmpty() ||
					email.getText().isEmpty() ||
					telephone.getText().isEmpty() ||
					balance.getText().isEmpty() ||
					username.getText().isEmpty() ||
					password.getText().isEmpty()) {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Empty fields");
				alert.setHeaderText(null);
				alert.setContentText("Some fields are empty."+System.lineSeparator()+"Please fill all the fields.");
				alert.showAndWait();
				
			    return;
					
			}
			
		}catch(Exception exc) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty fields");
			alert.setHeaderText(null);
			alert.setContentText("Some fields are empty."+System.lineSeparator()+"Please fill all the fields.");
			alert.showAndWait();
		    return;
		}

		
		 try {
			Statement statement = getConn().getStat();
			String st1 = " SELECT * FROM Bank.dbo.BankAccounts WHERE IDNumber = '"
					+ dni.getText()
					+ "'";
			ResultSet rs = statement.executeQuery(st1);

			if (rs.isBeforeFirst() ) { 
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Existing IDNumber");
				alert.setHeaderText(null);
				alert.setContentText("The ID Number already exist."+System.lineSeparator()+"Please introduce another ID Number.");
				alert.showAndWait();

			    return;
			}
		}catch(Exception exc) {
			System.err.println("Error!: " + exc.getMessage());
		}
		 
		 
		 try {
			Statement statement = getConn().getStat();
			String st2 = " SELECT * FROM Bank.dbo.UserPassword WHERE Username = '"
					+ username.getText()
					+ "'";
			ResultSet rs = statement.executeQuery(st2);

			if (rs.isBeforeFirst() ) { 
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Existing Username");
				alert.setHeaderText(null);
				alert.setContentText("The Username already exist."+System.lineSeparator()+"Please introduce another Username.");
				alert.showAndWait();
			    return;
			}
		}catch(Exception exc) {
			System.err.println("Error!: " + exc.getMessage());
		}


		 	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
			LocalDateTime now = LocalDateTime.now();  
			String currentDate = dtf.format(now); 
			
			String sql = "INSERT INTO Bank.dbo.BankAccounts VALUES ('"
					+ name.getText()
					+ "','"
					+ dni.getText()
					+ "','"
					+ dateOfBirth.getValue()
					+ "', '"
					+ nationality.getText()
					+ "','"
					+ address.getText()
					+ "','"
					+ gender.getSelectedToggle().getUserData().toString()
					+ "','"
					+ maritialStatus.getSelectedToggle().getUserData().toString()
					+ "','"
					+ email.getText()
					+ "','"
					+ telephone.getText()
					+ "','"
					+ balance.getText()
					+ "');"
					+ "INSERT INTO Bank.dbo.UserPassword VALUES ((SELECT IDENT_CURRENT ('Bank.dbo.BankAccounts')),'"
					+ username.getText()
					+ "','"
					+ password.getText()
					+ "', '"
					+ accountType.getValue()
					+ "');"
					+ "INSERT INTO Bank.dbo.AccountMovements VALUES ((SELECT IDENT_CURRENT ('Bank.dbo.BankAccounts')),'"
					+ "Open Account"
					+ "','"
					+ balance.getText()
					+ "', '"
					+ "Cash"
					+ "','"
					+ currentDate
					+ "');";
		 
		
		try {
		Statement statement = getConn().getStat();
		ResultSet rs = statement.executeQuery(sql);
		

	
		}catch(Exception exc) {
			if(exc.getMessage().equals("The statement did not return a result set.")) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Created Account");
				alert.setHeaderText(null);
				alert.setContentText("The account was created successfully!");
				alert.showAndWait();
			}
		System.err.println("Error!: " + exc.getMessage());
		}
		
		
		
//		RefreshAction();
		
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
	
	private void RefreshAction() throws SQLException {
	
		String sql = "SELECT * FROM dbo.BankAccounts";
        Statement statement = getConn().getStat();
        ResultSet rs = statement.executeQuery(sql);
        buildData(rs);
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

}
