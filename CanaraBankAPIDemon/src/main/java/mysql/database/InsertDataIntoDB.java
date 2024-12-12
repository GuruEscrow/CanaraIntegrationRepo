package mysql.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertDataIntoDB {

	// JDBC URL, username, and password of the MySQL database
	static final String jdbcUrl = "jdbc:mysql://3.109.189.244:3306/canarabank?useSSL=false";
	static final String username = "Guruprasad";
	static final String password = "MySql@#123";
	
	public static void insertPayloadWithUserRef(String userRef, String req_payload, String mode)
			throws SQLException, ClassNotFoundException {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");
			String query = "INSERT INTO canarabank.payouts_log (user_ref_no,req_payload,mode)" + "VALUES ('" + userRef
					+ "', '" + req_payload + "', '" + mode + "')";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}
	}

	public static void updateTxnResponse(String utr, String txn_response, String user_ref)
			throws SQLException, ClassNotFoundException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");

//			String query = "INSERT INTO canarabank.payouts_log (utr,txn_response)" + "VALUES ('" + utr + "', '"
//					+ txn_response + "')";
			String query = "UPDATE canarabank.payouts_log SET utr = '" + utr + "', txn_response = '" + txn_response
					+ "' WHERE user_ref_no='" + user_ref + "'";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}
	}

	public static void updateTimestampsWithDelay(String initiateTS, String responseTS, long delay, String user_ref)
			throws SQLException, ClassNotFoundException {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");

//			String query = "INSERT INTO canarabank.payouts_log (initiated_timestamp,response_timestamp,delay_in_response_secs)"
//					+ "VALUES ('" + initiateTS + "', '" + responseTS + "', '" + delay + "')";
			String query = "UPDATE canarabank.payouts_log SET initiated_timestamp = '" + initiateTS
					+ "', response_timestamp = '" + responseTS + "', delay_in_response_secs = '" + delay
					+ "' WHERE user_ref_no='" + user_ref + "'";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}
	}

	public static void updateTxnStatus(String txnStatus, String user_ref,String status) throws SQLException, ClassNotFoundException {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");

//			String query = "INSERT INTO canarabank.payouts_log (txn_status)" + "VALUES ('" + txnStatus + "')";
			String query = "UPDATE canarabank.payouts_log SET txn_status = '" + txnStatus + "', status = '" + status +"' WHERE user_ref_no='"
					+ user_ref + "'";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}
	}

	public static void updateBankStmWithTimeStamp(String statement, String stmTimeStamp, String user_ref)
			throws SQLException, ClassNotFoundException {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");

//			String query = "INSERT INTO canarabank.payouts_log (bank_statement,stm_gen_timestamp)" + "VALUES ('"
//					+ statement + "', '" + stmTimeStamp + "')";
			String query = "UPDATE canarabank.payouts_log SET bank_statement = '" + statement
					+ "', stm_gen_timestamp = '" + stmTimeStamp + "' WHERE user_ref_no='" + user_ref + "'";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}
	}

	public static void insertStmStatus(String utr, String user_ref, String reason)
			throws SQLException, ClassNotFoundException {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");
			String query = "INSERT INTO canarabank.stm_status_notfound (utr,user_ref,reason)" + "VALUES ('" + utr
					+ "', '" + user_ref + "', '" + reason + "')";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}
	}
	
	public static void deleteStmsFoundUTR(String utr)
			throws SQLException, ClassNotFoundException {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");
			String query = "Delete from canarabank.stm_status_notfound where (utr = '" + utr + "')";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}
	}

}
