package de.simonlaux.guestbook;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class MySqlGuestbookStore implements GuestbookStore {
	private Connection con;
	private String dbName = "";

	public MySqlGuestbookStore(String dbHost, int port, String dbName, String benutzer, String pin, String table)
			throws StoreInitException {
		try {
			this.dbName = dbName;
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + port + "/" + dbName + "?" + "user="
					+ benutzer + "&" + "password=" + pin
					+ "&useUnicode=true&characterEncoding=UTF-8&useSSL=true&autoReconnect=true&failOverReadOnly=false&maxReconnects=3");
			System.out.println("SQL Verbindung geglückt!");
			if (con != null) {
				Statement query;
				query = con.createStatement();

				String sql = "select * from INFORMATION_SCHEMA.TABLES where TABLE_NAME='book'";
				ResultSet result = query.executeQuery(sql);

				if (result.first()) {
					System.out.println("Tabelle vorhanden!");
				} else {
					System.out.println("Tabelle nicht vorhanden, versuche sie zu erstellen");
					createDB();
				}
			} else {
				throw new StoreInitException("Fehler,Keine Verbindung zur Datenbank");
			}
		} catch (StoreInitException e) {
			throw e;
		} catch (Exception e) {
			throw new StoreInitException("Fehler,Keine Verbindung zur Datenbank", e);
		}
	}

	@Override
	public void add(GuestbookEntry entry) {
		try {
			// Statement rs = con.createStatement();
			// String querry = "INSERT INTO `" + this.dbName + "`.`book`
			// (`date`, `email`, `inhalt`) VALUES ('"
			// + new java.sql.Timestamp(entry.getDate().getTime()) + "', '" +
			// entry.getEmail() + "', '"
			// + entry.getInhalt() + "');";
			PreparedStatement rs = con
					.prepareStatement("INSERT INTO `" + this.dbName + "`.`book` (`date`, `email`, `inhalt`) VALUES ('"
							+ new java.sql.Timestamp(entry.getDate().getTime()) + "', ?, ?);");
			rs.setString(1, entry.getEmail());
			rs.setString(2, entry.getInhalt());

			rs.executeUpdate();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<GuestbookEntry> getAll() {
		List<GuestbookEntry> liste = new ArrayList<GuestbookEntry>();
		// TODO Auto-generated method stub
		if (con != null) {
			// Abfrage-Statement erzeugen.
			Statement query;
			try {
				query = con.createStatement();

				String sql = "SELECT date, email , inhalt FROM " + dbName + ".book order by date DESC";
				ResultSet result = query.executeQuery(sql);
				while (result.next()) {
					liste.add(new GuestbookEntry((result.getTimestamp("date")), result.getString("email"),
							result.getString("inhalt")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return liste;
	}

	private void createDB() throws StoreInitException {
		try {
			Statement query;
			query = con.createStatement();
			String sql = "CREATE TABLE `book` (`idbook` int(11) NOT NULL AUTO_INCREMENT,`date` DATETIME(6) NOT NULL,`email` varchar(255) DEFAULT NULL,`inhalt` mediumtext, PRIMARY KEY (`idbook`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='die Einträge';";
			query.executeUpdate(sql);
		} catch (SQLException e) {
			throw new StoreInitException("Fehler,Konnte die Tabelle nict erstellen\n alle Rechte richtig gesetzt?", e);
		}
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
	/*
	 * public static void main(String args[]) throws Exception {
	 * MySqlGuestbookStore test = new MySqlGuestbookStore("127.0.0.1", 3306,
	 * "guestbook", "guestbook", "123456", "book"); GuestbookEntry g = new
	 * GuestbookEntry(new Date(), "em@a.il", "inhalt");
	 * 
	 * test.add(g);
	 * 
	 * if (test.close()) { System.out.println("Verbindung erfolgreich getrennt"
	 * ); } else { System.out.println(
	 * "Verbindung zum Gästebuchspeicher konnte NICHT getrennt werden!"); } }
	 */
}
