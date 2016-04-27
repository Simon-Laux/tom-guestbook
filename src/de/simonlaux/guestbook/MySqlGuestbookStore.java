package de.simonlaux.guestbook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.*;

public class MySqlGuestbookStore implements GuestbookStore {
	private Connection con;
	private String dbName="";

	public MySqlGuestbookStore(String dbHost, int Port, String dbName, String benutzer, String pin, String table) {
		this.dbName=dbName;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + Port + "/" + dbName + "?" + "user="
					+ benutzer + "&" + "password=" + pin
					+ "&useUnicode=true&characterEncoding=UTF-8&useSSL=true&autoReconnect=true&failOverReadOnly=false&maxReconnects=3");
			System.out.println("SQL Verbindung geglückt!");
		} catch (ClassNotFoundException e) {
			System.out.println("Treiber nicht gefunden");
		} catch (SQLException e) {
			System.out.println("Verbindung nicht moglich");
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "select * from INFORMATION_SCHEMA.TABLES where TABLE_NAME='book'";
				ResultSet result = query.executeQuery(sql);
				
				if (result.first()) {
					System.out.println("Tabelle vorhanden!");
				} else {System.out.println("Tabelle nicht vorhanden, versuche sie zu erstellen");
					create_DB();
					System.out.println("Fertig, ist über mir ne Fehlermeldung?\nwenn nicht, dann hats geklappt");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Fehler,Datenbank antwortet nicht");
		}
	}

	@Override
	public void add(GuestbookEntry entry) {
		// TODO Auto-generated method stub
		try {
			Statement rs=con.createStatement();
			String querry="INSERT INTO `guestbook`.`book` (`date`, `email`, `inhalt`) VALUES ('"+new java.sql.Timestamp(entry.getDate().getTime())+"', '"+entry.getEmail()+"', '"+entry.getInhalt()+"');";
			rs.executeUpdate(querry);
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	@Override
	public List<GuestbookEntry> getAll() {
		List<GuestbookEntry> liste = new ArrayList<GuestbookEntry>();
		// TODO Auto-generated method stub
		if(con != null){
		      // Abfrage-Statement erzeugen.
		      Statement query;
		      try {
		          query = con.createStatement();

		          String sql =
		                "SELECT date, email , inhalt FROM "+dbName+".book order by date DESC";
		          ResultSet result = query.executeQuery(sql);
		          while (result.next()) {
		          liste.add(new GuestbookEntry((result.getTimestamp("date")), result.getString("email"), result.getString("inhalt")));
		          }
		      } catch (SQLException e) {
		        e.printStackTrace();
		      }}
		return liste;
	}

	private void create_DB() throws SQLException {
		Statement query;
		query = con.createStatement();

		// Tabelle anzeigen
		String sql = "CREATE TABLE `book` (`idbook` int(11) NOT NULL AUTO_INCREMENT,`date` DATETIME(6) NOT NULL,`email` varchar(255) DEFAULT NULL,`inhalt` mediumtext, PRIMARY KEY (`idbook`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='die Einträge';";
		query.executeUpdate(sql);
	}

	public boolean close() {
		try {
			this.con.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String args[]) {
		MySqlGuestbookStore test = new MySqlGuestbookStore("127.0.0.1", 3306, "guestbook", "guestbook", "123456",
				"book");
		GuestbookEntry g=new GuestbookEntry(new Date(), "em@a.il", "inhalt");
		
		test.add(g);
		
		if (test.close()) {
			System.out.println("Verbindung erfolgreich getrennt");
		} else {
			System.out.println("Verbindung zum Gästebuchspeicher konnte NICHT getrennt werden!");
		}
	}

}
