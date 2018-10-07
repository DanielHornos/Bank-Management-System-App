package userPanels.userBalanceSheetPanel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import LoginGUI.Connector;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class UserBalanceSheetPanel {
	
	@FXML private TextField account = new TextField();
	@FXML private TextField balance = new TextField();
	@FXML private DatePicker dateFROM = new DatePicker();
	@FXML private DatePicker dateTO = new DatePicker();
	
	// Objects for Open action
	FileChooser fileChooser = new FileChooser();	
	Stage stage = new Stage();

    //TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;
    @FXML private TableView tableview;
    

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
	protected void actionForRefreshButton(ActionEvent e) throws SQLException {
		RefreshAction();
	}

	@FXML
	protected void actionForShowButton(ActionEvent e) {
		
	    account.setText(conn.getAccountNr());
		
		// Check if bank account is valid or not
		 try {
			Statement statement = getConn().getStat();
			String sql = "SELECT Balance FROM Bank.dbo.BankAccounts WHERE AccountNr = '" + conn.getAccountNr() + "'";

			ResultSet resultSetBalance = statement.executeQuery(sql);

			resultSetBalance.next();
		    balance.setText(resultSetBalance.getString(1));		

		}catch(Exception exc) {
			System.err.println("Error!: " + exc.getMessage());
		}
		 
		// Check if blank dates account is valid or not
		 try {
			 
			 // If both dateFROM and dateTO are blank, show all the results
			 if(dateFROM.getValue()==null && dateTO.getValue()==null){
					Statement statement = getConn().getStat();
					String sql = "SELECT  * FROM Bank.dbo.AccountMovements where AccountNr = '"
							+ account.getText()
							+ "';";
					
					ResultSet rs = statement.executeQuery(sql);
					buildData(rs);	
					return;
			 }
			 
				if(dateFROM.getValue()==null || dateTO.getValue()==null){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Empty fields");
					alert.setHeaderText(null);
					alert.setContentText( "Some fields are empty."+System.lineSeparator()+"Please fill all the fields.");
					alert.showAndWait();
				    return;
						
				}
				
			}catch(Exception exc) {
				System.err.println("Error!: " + exc.getMessage());
			}
		
		tableview.getColumns().clear();
		try {			
			Statement statement = getConn().getStat();
			String sql = "SELECT  * FROM Bank.dbo.AccountMovements where AccountNr = '"
					+ account.getText()
					+ "' AND Date between '"
					+ dateFROM.getValue()
					+ "' and '"
					+ dateTO.getValue()
					+ "';";
			
			ResultSet rs = statement.executeQuery(sql);
			buildData(rs);
		}catch(Exception exc) {
			System.err.println("Error!: " + exc.getMessage());
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
