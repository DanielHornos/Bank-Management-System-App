package adminPanels.TransferPanel;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TransferPanel {
	
	@FXML private TextField account1 = new TextField();
	@FXML private TextField ownerAccount1 = new TextField();
	@FXML private TextField balanceAccount1 = new TextField();
	@FXML private TextField account2 = new TextField();
	@FXML private TextField ownerAccount2 = new TextField();
	@FXML private TextField balanceAccount2 = new TextField();
	@FXML private TextField transferAmount = new TextField();

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
	protected void actionForConfirmTransferButton(ActionEvent e) throws IOException {
		
		 try {
			Statement statement = getConn().getStat();
			String st1 = " SELECT * FROM Bank.dbo.BankAccounts WHERE AccountNr = '"
					+ account1.getText()
					+ "'";
			ResultSet rs = statement.executeQuery(st1);

			if (!rs.isBeforeFirst() ) { 
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Wrong Account Number");
				alert.setHeaderText(null);
				alert.setContentText("The account number 1 doesn't exist."+System.lineSeparator()+"Please introduce another Account Number.");
				alert.showAndWait();
			    return;
			}
		}catch(Exception exc) {
			System.err.println("Error!: " + exc.getMessage());
		}
		
		 try {
			Statement statement = getConn().getStat();
			String st2 = " SELECT * FROM Bank.dbo.BankAccounts WHERE AccountNr = '"
					+ account2.getText()
					+ "'";
			ResultSet rs = statement.executeQuery(st2);

			if (!rs.isBeforeFirst() ) { 
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Wrong Account Number");
				alert.setHeaderText(null);
				alert.setContentText("The account number 2 doesn't exist."+System.lineSeparator()+"Please introduce another Account Number.");
				alert.showAndWait();
			    return;
			}
		}catch(Exception exc) {
			System.err.println("Error!: " + exc.getMessage());
		}
		 
		try {	
		 	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
			LocalDateTime now = LocalDateTime.now();  
			String currentDate = dtf.format(now); 
			
			Double ammountToTransfer = Double.parseDouble(transferAmount.getText());


			// create sql for account1
			Statement statement1 = getConn().getStat();
			String account1String = account1.getText();
			String sql1 = "SELECT Balance FROM Bank.dbo.BankAccounts WHERE AccountNr LIKE '%" + account1.getText() + "%'";
			ResultSet resultSetBalance1 = statement1.executeQuery(sql1);
			resultSetBalance1.next();
			Double balanceAmmount1 = Double.parseDouble(resultSetBalance1.getString(1));		
			
			System.out.println("Balance account1 is: " + resultSetBalance1.getString(1));
			
			// TODO: hay que tener en cuenta si se ha dado dinero en cash o transferencia
			Double newBalanceAccount1 = balanceAmmount1 - ammountToTransfer;
			
			String sqlNewBalanceAccount1 = "UPDATE Bank.dbo.BankAccounts SET "
					+ "Balance = '" + newBalanceAccount1.toString() + "'"
					+ "WHERE AccountNr= '" + account1.getText() 
					+ "';"
					+ "INSERT INTO Bank.dbo.AccountMovements VALUES ('"
					+ account1.getText()
					+ "','"
					+ "Transfer beetween accounts"
					+ "', '"
					+ "-"+transferAmount.getText()
					+ "', '"
					+ "Transfer"
					+ "', '"
					+ currentDate
					+ "')";
			
			// create sql for account2
			Statement statement2 = getConn().getStat();
			String account2String = account2.getText();
			String sql2 = "SELECT Balance FROM Bank.dbo.BankAccounts WHERE AccountNr LIKE '%" + account2.getText() + "%'";
			ResultSet resultSetBalance2 = statement2.executeQuery(sql2);
			resultSetBalance2.next();
			Double balanceAmmount2 = Double.parseDouble(resultSetBalance2.getString(1));		
			
			System.out.println("Balance account2 is: " + resultSetBalance2.getString(1));
			
			
			// TODO: hay que tener en cuenta si se ha dado dinero en cash o transferencia
			Double newBalanceAccount2 = balanceAmmount2 + ammountToTransfer;

			String sqlNewBalanceAccount2 = "UPDATE Bank.dbo.BankAccounts SET "
					+ "Balance = '" + newBalanceAccount2.toString() + "'"
					+ "WHERE AccountNr= '" + account2.getText() 
					+ "';"
					+ "INSERT INTO Bank.dbo.AccountMovements VALUES ('"
					+ account2.getText()
					+ "','"
					+ "Deposit"
					+ "', '"
					+ transferAmount.getText()
					+ "', '"
					+ "Transfer"
					+ "', '"
					+ currentDate
					+ "')";
			
			// update both account
			String messageQuestion = "Do you confirm that you want to transfer " + ammountToTransfer + " PLN" + System.lineSeparator() +
					"From account NR: " + account1String + " (" + ownerAccount1.getText() +")"
					+ System.lineSeparator()+ 
					"To the account NR: " + account2String + account1String + " (" + ownerAccount2.getText() +")";
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Transaction confirmation");
			alert.setHeaderText(null);
			alert.setContentText(messageQuestion);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				String sqlNewBalanceBothAccounts = sqlNewBalanceAccount1 + System.lineSeparator() + sqlNewBalanceAccount2;
				ResultSet rs = statement2.executeQuery(sqlNewBalanceBothAccounts);
				buildData(rs);
			} else {
			    // ... user chose CANCEL or closed the dialog
			}
			
			}catch(Exception exc) {
		System.err.println("ERROR");
		System.out.println(exc.getMessage());
			}
		
		actionForShowDetailsButton(new ActionEvent());
	
	}
	
	@FXML
	protected void actionForShowDetailsButton(ActionEvent e) throws IOException {
		try {			
			
			// showing details for account1 (from)
			Statement statement1 = getConn().getStat();
			String sql1 = "SELECT Name, Balance FROM Bank.dbo.BankAccounts WHERE AccountNr = '" + account1.getText() + "'";
			ResultSet resultSetBalance1 = statement1.executeQuery(sql1);
			
			if (!resultSetBalance1.isBeforeFirst() ) { 
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Wrong Account Number");
				alert.setHeaderText(null);
				alert.setContentText("The account number 1 doesn't exist."+System.lineSeparator()+"Please introduce another Account Number.");
				alert.showAndWait();
			    return;
			}
			
			while (resultSetBalance1.next()) {
	        ownerAccount1.setText(resultSetBalance1.getString(1));		
	        balanceAccount1.setText(resultSetBalance1.getString(2));		
			}

			
			// showing details for account2 (to)
			Statement statement2 = getConn().getStat();
			String sql2 = "SELECT Name, Balance FROM Bank.dbo.BankAccounts WHERE AccountNr = '" + account2.getText() + "'";
			ResultSet resultSetBalance2 = statement2.executeQuery(sql2);
			
			if (!resultSetBalance2.isBeforeFirst() ) { 
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Wrong Account Number");
				alert.setHeaderText(null);
				alert.setContentText("The account number 2 doesn't exist."+System.lineSeparator()+"Please introduce another Account Number.");
				alert.showAndWait();
			    return;
			}
			
			while (resultSetBalance2.next()) {
	        ownerAccount2.setText(resultSetBalance2.getString(1));		
	        balanceAccount2.setText(resultSetBalance2.getString(2));		
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
