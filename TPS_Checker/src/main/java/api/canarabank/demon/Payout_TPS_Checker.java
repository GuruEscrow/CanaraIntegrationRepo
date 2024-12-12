package api.canarabank.demon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ezpayouts.CanarabankLive;
import mysql.database.InsertDataIntoDB;

public class Payout_TPS_Checker {

	public void payout(int key) {

		CanarabankLive client = new CanarabankLive();
		try {
			String accName = null;
			String accNum = null;
			String ifscNum = null;
			String amt = null;
			String txnNote = null;

			// Date Formats
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			switch (key) {
			case 1:
				// Valid bank account details for imps
				accName = "PhedoraAxisBank";
				accNum = "924020017800104";
				ifscNum = "UTIB0004575";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 2:
				// INValid bank account details for imps
				accName = "InvaidBank";
				accNum = "137104000182232";
				ifscNum = "IBKL0000137";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 3:
				// Closed bank account details for imps
				accName = "ClosedAccount";
				accNum = "39676197828";
				ifscNum = "SBIN0003474";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 4:
				// Frozen bank account details for imps
				accName = "FrozenAccount";
				accNum = "64701000054019";
				ifscNum = "IOBA0000647";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 5:
				// NRE bank account details for imps
				accName = "NREAccount";
				accNum = "99982107442353";
				ifscNum = "FDRL0002421";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

//			NEFT Account details
			case 6:
				// Valid bank account details for NEFT
				accName = "PhedoraAxisBank";
				accNum = "924020017800104";
				ifscNum = "UTIB0004575";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 7:
				// INValid bank account details for NEFT
				accName = "InvaidBank";
				accNum = "137104000182232";
				ifscNum = "IBKL0000137";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 8:
				// Closed bank account details for NEFT
				accName = "ClosedAccount";
				accNum = "39676197828";
				ifscNum = "SBIN0003474";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 9:
				// Frozen bank account details for NEFT
				accName = "FrozenAccount";
				accNum = "64701000054019";
				ifscNum = "IOBA0000647";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 10:
				// NRE bank account details for NEFT
				accName = "NREAccount";
				accNum = "99982107442353";
				ifscNum = "FDRL0002421";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

//				Internal Transfer
			case 11:
				// NRE bank account details for NEFT
				accName = "CanarBank";
				accNum = "120029437735";
				amt = "1";
				txnNote = "InternalBankTransfer";
				break;

			default:
				break;
			}

//			If key is 1 t0 5 imps payout will happen to other bank
			if (key >= 1 && key <= 5) {

				// Initiated time
				String initiatedTSString = LocalDateTime.now().format(format);
				LocalDateTime initiatedTS = LocalDateTime.parse(initiatedTSString, format);
				System.out.println("Initiated_timestamp: " + initiatedTSString + "\n");

				// Initiating the payout
				Map<String, String> payoutDetails = client.impsTransfer(accName, accNum, ifscNum, amt, txnNote);
				if(payoutDetails==null) {
					return;
				}

				// Response Time
				String responseTSString = LocalDateTime.now().format(format);
				LocalDateTime responseTS = LocalDateTime.parse(responseTSString, format);
				System.out.println("\nResponse_timestamp: " + responseTSString);

				// Fetching details in payout details map
				String user_ref = payoutDetails.get("user_ref");
				String utr = payoutDetails.get("utr");

				// Updated the initiated time and response time with delay into DB
				long delay = ChronoUnit.SECONDS.between(initiatedTS, responseTS);
				InsertDataIntoDB.updateTimestampsWithDelay(initiatedTSString, responseTSString, delay, user_ref);

				// Updating the payout status into DB
				// client.impsStatus(user_ref);

				// Updating the statement into DB
				String todayDate = LocalDate.now().format(dateFormat);

				// Calling get statement api
				String stmtFetchtime = LocalDateTime.now().format(format);
				Map<String, String> stmtMap = client.getStatement(todayDate, todayDate);

				List<String> stmtsList = new ArrayList<String>();
				// checking for dr statement
				if (stmtMap.containsKey(utr)) {
					stmtsList.add(stmtMap.get(utr));
				}
				// checking for credit statement if there
				String revsalUTR = "REV" + utr.substring(3);
				if (stmtMap.containsKey(revsalUTR)) {
					stmtsList.add(stmtMap.get(revsalUTR));
				}

				// checking if stmt present -> push to payout log, if not -> push txn without
				// stmt
				if (stmtsList.size() != 0) {
					InsertDataIntoDB.updateBankStmWithTimeStamp(stmtsList.toString(), stmtFetchtime, user_ref);
				} else {
					InsertDataIntoDB.insertStmStatus(utr, user_ref, "stmt not present");
				}

			}

//			If key is 6 t0 10 NEFT payout will happen
			if (key >= 6 && key <= 10) {
				String date = LocalDate.now().format(dateFormat);

				// Initiated time
				String initiatedTSString = LocalDateTime.now().format(format);
				LocalDateTime initiatedTS = LocalDateTime.parse(initiatedTSString, format);
				System.out.println("Initiated_timestamp: " + initiatedTSString + "\n");

				// Initiating the payout
				Map<String, String> payoutDetails = client.neftTransfer(accName, accNum, ifscNum, amt, txnNote, date);

				// Response Time
				String responseTSString = LocalDateTime.now().format(format);
				LocalDateTime responseTS = LocalDateTime.parse(responseTSString, format);
				System.out.println("\nResponse_timestamp: " + responseTSString);

				// Fetching details in payout details map
				String user_ref = payoutDetails.get("user_ref");
				String utr = payoutDetails.get("utr");

				// Updated the initiated time and response time with delay into DB
				long delay = ChronoUnit.SECONDS.between(initiatedTS, responseTS);
				InsertDataIntoDB.updateTimestampsWithDelay(initiatedTSString, responseTSString, delay, user_ref);

				// Updating the payout status into DB
				client.neftStatus(user_ref);

				// Updating the statement into DB
				String todayDate = LocalDate.now().format(dateFormat);

				// Calling get statement api
				String stmtFetchtime = LocalDateTime.now().format(format);
				Map<String, String> stmtMap = client.getStatement(todayDate, todayDate);

				List<String> stmtsList = new ArrayList<String>();
				// checking for dr statement
				if (stmtMap.containsKey(utr)) {
					stmtsList.add(stmtMap.get(utr));
				}
				// checking for credit statement if there
				String revsalUTR = "REV" + utr.substring(3);
				if (stmtMap.containsKey(revsalUTR)) {
					stmtsList.add(stmtMap.get(revsalUTR));
				}

				// checking if stmt present -> push to payout log, if not -> push txn without
				// stmt
				if (stmtsList.size() != 0) {
					InsertDataIntoDB.updateBankStmWithTimeStamp(stmtsList.toString(), stmtFetchtime, user_ref);

					// Pushing the neft transaction utr to DB
					InsertDataIntoDB.insertStmStatus(utr, user_ref, stmtMap.get(utr));
				} else {
					InsertDataIntoDB.insertStmStatus(utr, user_ref, "stmt not present");
				}

			}

//			If key is 11 internal payout will happen
			if (key == 11) {
				String date = LocalDate.now().format(dateFormat);

				// Initiated time
				String initiatedTSString = LocalDateTime.now().format(format);
				LocalDateTime initiatedTS = LocalDateTime.parse(initiatedTSString, format);
				System.out.println("Initiated_timestamp: " + initiatedTSString + "\n");

				Map<String, String> payoutDetails = client.internalTransfer(accName, accNum, amt, txnNote, date);

				// Response Time
				String responseTSString = LocalDateTime.now().format(format);
				LocalDateTime responseTS = LocalDateTime.parse(responseTSString, format);
				System.out.println("\nResponse_timestamp: " + responseTSString);

				// Fetching details in payout details map
				String user_ref = payoutDetails.get("user_ref");
				String utr = payoutDetails.get("utr");

				// Updated the initiated time and response time with delay into DB
				long delay = ChronoUnit.SECONDS.between(initiatedTS, responseTS);
				InsertDataIntoDB.updateTimestampsWithDelay(initiatedTSString, responseTSString, delay, user_ref);

				// Updating the payout status into DB
				client.internalTransferStatus(user_ref);

				// Updating the statement into DB
				String todayDate = LocalDate.now().format(dateFormat);

				// Calling get statement api
				String stmtFetchtime = LocalDateTime.now().format(format);
				Map<String, String> stmtMap = client.getStatement(todayDate, todayDate);

				List<String> stmtsList = new ArrayList<String>();
				// checking for dr statement
				if (stmtMap.containsKey(utr)) {
					stmtsList.add(stmtMap.get(utr));
				}
				// checking for credit statement if there
				String revsalUTR = "REV" + utr.substring(3);
				if (stmtMap.containsKey(revsalUTR)) {
					stmtsList.add(stmtMap.get(revsalUTR));
				}

				// checking if stmt present -> push to payout log, if not -> push txn without
				// stmt
				if (stmtsList.size() != 0) {
					InsertDataIntoDB.updateBankStmWithTimeStamp(stmtsList.toString(), stmtFetchtime, user_ref);
				} else {
					InsertDataIntoDB.insertStmStatus(utr, user_ref, "stmt not present");
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

//	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException {
//		Payout_TPS_Checker testDemon = new Payout_TPS_Checker();
//
//		// Creating scheduler for 24 hours
//		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//		// running the task with fixed delay for 24 hours
//		scheduler.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				// Again create scheduler for 1 minute
//				final ScheduledExecutorService taskScheduler = Executors.newScheduledThreadPool(1);
//
//				// Schedule the task to run every 333 milliseconds for 1 minute
//				taskScheduler.scheduleAtFixedRate(new Runnable() {
//					@Override
//					public void run() {
//						// This line execute the task every time in separate thread
//						Executors.newSingleThreadExecutor().submit(new Runnable() {
//							@Override
//							public void run() {
////								Random number = new Random();
////								int num = number.nextInt((11 - 1) + 1) + 1;
//								testDemon.payout(1);
//							}
//						});
//
//					}
//				}, 0, 333, TimeUnit.MILLISECONDS); // Run every 333 ms
//
//				// Stop the task scheduler after 1 minute
//				taskScheduler.schedule(new Runnable() {
//					@Override
//					public void run() {
//						if (!taskScheduler.isShutdown()) {
//							taskScheduler.shutdown();
//							System.out.println("Task stopped after 1 minute.");
//						}
//					}
//				}, 1, TimeUnit.MINUTES); // Stop after 1 minute
//			}
//		}, 0, 1, TimeUnit.HOURS); // Run the task every 1 hour
//
//		// Stop the main scheduler after 24 hours
//		scheduler.schedule(new Runnable() {
//			@Override
//			public void run() {
//				if (!scheduler.isShutdown()) {
//					scheduler.shutdown();
//					System.out.println("Scheduler shutdown after 24 hours.");
//				}
//			}
//		}, 24, TimeUnit.HOURS); // Stop after 24 hours
//
//	}
	
	public static void main(String[] args) {
		Payout_TPS_Checker testDemon = new Payout_TPS_Checker();
		
		// To schedule the task for 1 minute with mentioned delay

		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// Submit the task to another thread
				Executors.newSingleThreadExecutor().submit(new Runnable() {
					@Override
					public void run() {
//						Random number = new Random();
//						int num = number.nextInt((11 - 1) + 1) + 1;
						testDemon.payout(1);
					}
				});
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);
		
		//To stop the payouts after 1 minutes and it will be there to execute all the threads
		scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				if (scheduler != null) {
					scheduler.shutdown(); // Optionally shut down the scheduler
				}
			}
		}, 5, TimeUnit.MINUTES);
	}
}