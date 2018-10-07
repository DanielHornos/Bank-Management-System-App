package LoginGUI;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.Statement;

public class Connector {

	Connection conn;
	Statement stat;
	Boolean isAdmin;
	
	static String url, database, username, password, hostname, port, driver, accountNr;
	
	public Statement getStat() {
		return stat;
	}

	public String getAccountNr() {
		return accountNr;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Connector(String user, String pass) {
		username = user;
		password = pass;
		System.out.println("Username is: " + username);
		System.out.println("Password is: " + password);
//		driver = "com.mysql.jdbc.Driver";
//		url = "jdbc:mysql://"+hostname+":"+port+"/"+database;
		
		String hostname = getHostname();
		
		driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//		url = "jdbc:sqlserver://"+hostname+"\\SQLEXPRESS;databaseName=SQL_WORKSHOP;integratedSecurity=true"; // This will use windows authentication
		url = "jdbc:sqlserver://"+hostname+"\\SQLEXPRESS;databaseName=Bank";
		
		isAdmin = open();
		
	}
	
	public boolean open() {
		try {
			DriverManager.registerDriver((java.sql.Driver) Class.forName(driver).newInstance());

//			conn = DriverManager.getConnection(url, username, password);
			conn = DriverManager.getConnection(url, "danilo", "qwerty&1234");
			
			stat = conn.createStatement();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(conn == null)
				return false;
		}
		System.out.println("Connection successful");
		
		
		try {
		String st = " SELECT AccountType,AccountNr FROM Bank.dbo.UserPassword where Username = '"
				+ username
				+ "' AND Password = '"
				+ password
				+ "';";
			ResultSet rs = stat.executeQuery(st);
			rs.next();
			String userOrAdmin = rs.getString(1);
			accountNr = rs.getString(2);
			System.out.println("user or admin: " + userOrAdmin);
			System.out.println("Account Number is: " + accountNr);
			
			if(userOrAdmin.equals("User")) {
				isAdmin = false;
				System.out.println("User is normal user");
			}else 
			if(userOrAdmin.equals("Admin")){
				isAdmin = true;
				System.out.println("User is admin");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public ResultSet executeQuery(String s) throws SQLException {
		return stat.executeQuery(s);
	}
	
	public void executeUpdate(String s) throws SQLException {
		stat.executeUpdate(s);
	}
	
	public String getHostname() {
		try
		{
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    System.out.println("The hostname is: " + addr.getHostName());
		    return addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
		    System.out.println("Hostname can not be resolved");
		}
		return null;
	}
}
