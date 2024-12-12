package api.canarabank.demon;

import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ezpayouts.CanarabankLive;
import mysql.database.FetchDataFromDB;
import mysql.database.InsertDataIntoDB;

public class StatementFetch_Daemon {

	public void stm_fetch() {

		try {
			CanarabankLive client = new CanarabankLive();

			// Date Formats
			String date = "22-11-2024";
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			// Fetching the transactions UTR which don't have the statements
			Map<String, List> txnWostm_map = FetchDataFromDB.getStmtNotFoundUTR();

			// Fetching the today's statement
			System.out.println("Statement fetch started........");
			String todayDate = LocalDate.parse(date,dateFormat).format(dateFormat); //LocalDate.now().format(dateFormat)
			String stmtFetchtime = LocalDateTime.now().format(format);
			Map<String, String> stmtMap = client.getStatement(todayDate, todayDate);

//			Iterating on txnWostm_map to find the statements from the fetched one
			for (Map.Entry<String, List> individualTxn : txnWostm_map.entrySet()) {
				String utr = individualTxn.getKey();
				String user_ref = (String) individualTxn.getValue().get(0);
				String reason = (String) individualTxn.getValue().get(1);

				List<String> stmtsFound = new ArrayList<String>();
				// checking for dr statement
				if (stmtMap.containsKey(utr)) {
					stmtsFound.add(stmtMap.get(utr));
				}
				// checking for credit statement if there
				String revsalUTR = "REV" + utr.substring(3);
				if (stmtMap.containsKey(revsalUTR)) {
					stmtsFound.add(stmtMap.get(revsalUTR));
				}

				// To Resolve the NEFT failed transaction
				for (Map.Entry<String, String> singleStm : stmtMap.entrySet()) {

					// To find the Reversal Neft
					JsonObject statement = JsonParser.parseString(singleStm.getValue()).getAsJsonObject();
					String description = statement.get("description").getAsString();
					String crDr = statement.get("creditDebitFlag").getAsString();

					if (description.contains("NEFT") && description.contains("RETURN") && description.contains(utr)
							&& crDr.equals("C")) {
						String neftRevUTR = statement.get("txnRefNumber").getAsString();
						if (!reason.equals("stmt not present")) {
							if (stmtsFound.size() == 0) {
								stmtsFound.add(stmtMap.get(neftRevUTR));
							}
						}
						stmtsFound.add(stmtMap.get(neftRevUTR));
					}

					if (description.contains("NEFT") && stmtsFound.size() == 1) {
						if (!reason.equals("stmt not present")) {
							stmtsFound.remove(0);
						}
					}
				}

				// checking if stmt present -> push to payout log, if not -> push txn without
				// stmt
				if (stmtsFound.size() != 0) {
					System.out.println("Resolved: " + utr);
					// Updating the payout log for transactions statements found
					InsertDataIntoDB.updateBankStmWithTimeStamp(stmtsFound.toString(), stmtFetchtime, user_ref);

					// Deleting the UTR from the DB for transactions statement found
					InsertDataIntoDB.deleteStmsFoundUTR(utr);

				}
			}
//			Iterating done....
			System.out.println("Statement fetch done.......");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void statusChecker() throws ClassNotFoundException, SQLException {

		CanarabankLive client = new CanarabankLive();

		Map<String, String> userRef_mode = FetchDataFromDB.getUserRefWithTxnMode();
		System.out.println(userRef_mode);

		for (Map.Entry<String, String> singleData : userRef_mode.entrySet()) {

			String userRef = singleData.getKey();
			String mode = singleData.getValue();

			if (mode.equals("imps")) {
				try {
					client.impsStatus(userRef);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(mode.equals("neft")) {
				try {
					client.neftStatus(userRef);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(mode.equals("internal")) {
				try {
					client.internalTransferStatus(userRef);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				
			}
		}

	}

	public static void main(String[] args) {

		StatementFetch_Daemon tps = new StatementFetch_Daemon();

		try {
			// Status check
			//tps.statusChecker();
			// Statement fetch
			tps.stm_fetch();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
