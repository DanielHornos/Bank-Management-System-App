package adminPanels.AllUsersPanel;



import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import LoginGUI.Connector;
import LoginGUI.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AllUsersPanel {

	@FXML private TextField search = new TextField();

	// Objects for Open action
	FileChooser fileChooser = new FileChooser();	
	Stage stage = new Stage();

    //TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;
    @FXML private TableView tableview;
    
	@FXML ComboBox<String> comboBox = new ComboBox<>();

	Connector conn;

	public void initialize() {
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
	protected void actionForRefreshButton(ActionEvent e) throws SQLException {
		RefreshAction();
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
