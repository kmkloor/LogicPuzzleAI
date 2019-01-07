package puzzle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
/**
 * The Gridmanager class sets up and manages the database used to solve the puzzle. 
*
 */
public class GridManager {
	
	/** The conn. */
	Connection conn;

	/**
	 * Gets the connection.
	 *
	 * @param p the password
	 * @return the connection
	 */
	public Connection getConnection(String p){
		conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=" + p); 
			System.out.println("Connected to database");
			return conn;
		} catch(SQLException s) {
			System.out.println("Connection failed");	
		}
		return conn;
	}

	/**
	 * Builds the DB.
	 *
	 * @param puz the puzzle
	 */
	public void buildDB(Puzzle puz){
		passUpdate(conn, "CREATE DATABASE logicpuzzle;");
		passUpdate(conn, "USE logicpuzzle;");
		passUpdate(conn, "CREATE TABLE categories (category_id VARCHAR(50) PRIMARY KEY);");
		List<String> rows = new ArrayList<String>();
		for(Category cat:puz.getCategories()) {
			for(String item:cat.getItems()) {
				String columns = "ALTER TABLE categories ADD COLUMN _" + cleanText(item) + " INT(2);";
				passUpdate(conn, columns);
				rows.add("_" + cleanText(item));
			}
		}
		StringBuilder s = new StringBuilder();
		for (String row: rows) {
			s.append(",'0'");
		}
		for(String row: rows) {
			
			passUpdate(conn, "INSERT INTO categories VALUES ('" + row + "'" + s + ");");
		}
	}

	/**
	 * Destroy DB.
	 */
	public void destroyDB(){
		passUpdate(conn, "DROP DATABASE logicpuzzle;");
	}

	/**
	 * Pass update. 
	 *
	 * @param conn the connection
	 * @param query the query
	 */
	private void passUpdate(Connection conn, String query){
		Statement s = null;
		try {
			s = conn.createStatement();
			s.executeUpdate(query);
		} catch (SQLException e ) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			statementFinally(s);
		}
	}

	/**
	 * Update intersection.
	 *
	 * @param row the row
	 * @param column the column
	 * @param i the i
	 */
	public void updateIntersection(String row, String column, int i) {
		int j = checkIntersection(row, column);
		row = cleanText(row);
		column = cleanText(column);
		if(j == 0) {
			String update = "UPDATE categories SET _" + row + " = " + i + " WHERE category_id IN ('_" + column + "');";
			String inverse = "UPDATE categories SET _" + column + " = " + i + " WHERE category_id IN ('_" + row + "');";
			passUpdate(conn, update);
			passUpdate(conn, inverse);

		}
	}

	/**
	 * Check intersection.
	 *
	 * @param row the row
	 * @param column the column
	 * @return the int
	 */
	public int checkIntersection(String row, String column){
		String select = "SELECT category_id, _" + cleanText(column) + " FROM categories;";
		Statement s = null;
		int i = 0;
		try {
			s = conn.createStatement();
			ResultSet rs = s.executeQuery(select);
			while(rs.next()) {
				if(rs.getString(1).equals("_" + cleanText(row))) {
					i = rs.getInt(2);
				}
			}
		} catch (SQLException e ) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			statementFinally(s);
		}
		return i;
	}

	/**
	 * Reuse the "finally" path used in query try/catch/finally.
	 *
	 * @param s the s
	 */
	private void statementFinally(Statement s) {
		if (s != null) { try {
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	/**
	 * Clean text. Prepare text for database by stripping spaces and punctuation.
	 *
	 * @param str the str
	 * @return the string
	 */
	private String cleanText(String str) {
		String clean = str.replace(" ", "");
		clean = clean.replace(":", "");
		clean = clean.replace("$", "");
		clean = clean.replace(".", "");
		clean = clean.replace("\"", "");
		clean = clean.replace("'", "");
		clean = clean.replace("&", "");
		return clean;
	}
}
