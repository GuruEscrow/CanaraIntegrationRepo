package mysql.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchDataFromDB {

	// JDBC URL, username, and password of the MySQL database
	static final String jdbcUrl = "jdbc:mysql://3.109.189.244:3306/canarabank?useSSL=false";
	static final String username = "Guruprasad";
	static final String password = "MySql@#123";

	public static Map<String, List> getStmtNotFoundUTR() throws ClassNotFoundException, SQLException {

		Map<String,List> txnWoStm_utr_map = new HashMap<String,List>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		String payout_timeStamp = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			String query = "SELECT * FROM canarabank.stm_status_notfound";

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String utr = resultSet.getString("utr");
				String user_ref = resultSet.getString("user_ref");
				String reason = resultSet.getString("reason");
				
				List <String> userref_reason_list = new ArrayList<String>();
				userref_reason_list.add(user_ref);
				userref_reason_list.add(reason);
				
				txnWoStm_utr_map.put(utr, userref_reason_list);
			}
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
		
		return txnWoStm_utr_map;
	}
	
	public static Map<String, String> getUserRefWithTxnMode() throws ClassNotFoundException, SQLException {

		Map<String, String> txnWoStm_utr_map = new HashMap<String, String>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		String payout_timeStamp = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			
//			SELECT * FROM tpslog.payouts_log where txn_status is null
			String query = "SELECT * FROM canarabank.payouts_log where utr != 'null' and status is null";

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String user_ref = resultSet.getString("user_ref_no");
				String utr = resultSet.getString("utr");
				String mode = resultSet.getString("mode");

				List<String> userref_reason_list = new ArrayList<String>();
				userref_reason_list.add(utr);
				userref_reason_list.add(mode);

				txnWoStm_utr_map.put(user_ref, mode);
			}
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

		return txnWoStm_utr_map;
	}
}
