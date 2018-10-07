package adminPanels.DepositPanel;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import LoginGUI.Connector;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class DepositPanel {
	
	@FXML private TextField account = new TextField();
	@FXML private TextField name = new TextField();
	@FXML private TextField balance = new TextField();
	@FXML private TextField depositAmount = new TextField();


    //TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;
    @FXML private TableView tableview;

	@FXML ComboBox<String> comboBox = new ComboBox<>();
	
	Connector conn;
	private Double newBalance;

	public void initialize() {
		comboBox.getItems().removeAll(comboBox.getItems());
	    comboBox.getItems().addAll("Cash", "Transfer");
	    comboBox.getSelectionModel().select("Cash");
	}
	
	public Connector getConn() {
		return conn;
	}
	
	public void setConn(Connector conn) {
		this.conn = conn;
	}

	// TODO: En este metodo tengo que poner que pasa si el numero de cuenta no es correcto.
	@FXML
	protected void actionForConfirmDepositButton(ActionEvent e) throws IOException {
		
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
			String sql = "SELECT Balance FROM Bank.dbo.BankAccounts WHERE AccountNr = '" + account.getText() + "'";
			ResultSet resultSetBalance = statement.executeQuery(sql);
			resultSetBalance.next();
			Double balanceAmmount = Double.parseDouble(resultSetBalance.getString(1));		
			
			System.out.println("Balance ammount is: " + resultSetBalance.getString(1));
			
			Double ammountToSubtract = Double.parseDouble(depositAmount.getText());
		
		 	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
			LocalDateTime now = LocalDateTime.now();  
			String currentDate = dtf.format(now); 
			
			newBalance = balanceAmmount + ammountToSubtract;
			String sqlNewBalance = "UPDATE Bank.dbo.BankAccounts SET "
					+ "Balance = '" + newBalance.toString() + "'"
					+ "WHERE AccountNr= '" + account.getText() 
					+ "';"
					+ "INSERT INTO Bank.dbo.AccountMovements VALUES ('"
					+ account.getText()
					+ "','"
					+ "Deposit"
					+ "', '"
					+ depositAmount.getText()
					+ "', '"
					+ comboBox.getValue()
					+ "', '"
					+ currentDate
					+ "')";
					
			ResultSet rs = statement.executeQuery(sqlNewBalance);
			buildData(rs);
			
			}catch(Exception exc) {
				System.err.println("Error!: " + exc.getMessage());				

				if(exc.getMessage().equals("The statement did not return a result set.")) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Deposit Confirmed");
					alert.setHeaderText(null);
					alert.setContentText("The deposit was done successfully!" + System.lineSeparator() + 
							"The new balance is: " + newBalance.toString());
					alert.showAndWait();
				}
			}
		
		actionForShowDetailsButton(new ActionEvent());
	}
	
	@FXML
	protected void actionForShowDetailsButton(ActionEvent e) throws IOException {
		try {			
			Statement statement = getConn().getStat();

			String sql = "SELECT Name, Balance FROM Bank.dbo.BankAccounts WHERE AccountNr = '" + account.getText() + "'";

			ResultSet resultSetBalance = statement.executeQuery(sql);
			
			if (!resultSetBalance.isBeforeFirst() ) { 
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Wrong Account Number");
				alert.setHeaderText(null);
				alert.setContentText("The account number doesn't exist."+System.lineSeparator()+"Please introduce another Account Number.");
				alert.showAndWait();
			    return;
			}
			
			while (resultSetBalance.next()) {
	        name.setText(resultSetBalance.getString(1));		
	        balance.setText(resultSetBalance.getString(2));		
			}

		}catch(Exception exc) {
		System.err.println("ERROR");
		System.out.println(exc.getMessage());
			}
		

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

//                tableview.getColumns().addAll(col); 
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
//            tableview.setItems(data);
            
            System.out.println("==>"+data.toString());
          }catch(Exception e){
              e.printStackTrace();
              System.out.println("Error on Building Data");             
          }
      }


	

}
