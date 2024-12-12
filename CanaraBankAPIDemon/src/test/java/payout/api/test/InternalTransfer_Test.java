package payout.api.test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.codec.DecoderException;
import org.jose4j.lang.JoseException;
import org.testng.annotations.Test;

import ezpayouts.CanarabankLive;

public class InternalTransfer_Test {

	@Test
	public static void internalTransfer()
			throws NoSuchAlgorithmException, JoseException, InvalidKeySpecException, DecoderException, IOException {
		CanarabankLive client = new CanarabankLive();
		try {
			String accName = "Guruprasad";
			String accNum = "711103051268";
			// String ifscNum = "";
			String amt = "1";
			String txnNote = "GuruImpsTest";
			String date = "31-08-2024";

			client.internalTransfer(accName, accNum, amt, txnNote, date);
			

			// Printing the timestamp
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			System.out.println("\ntimestamp: " + LocalDateTime.now().format(format));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public static void getInternalStatus() {
		CanarabankLive client = new CanarabankLive();

		try {
			client.internalTransferStatus("PHED1732187282444");

			// Printing the timestamp
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			System.out.println("\ntimestamp: " + LocalDateTime.now().format(format));
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
