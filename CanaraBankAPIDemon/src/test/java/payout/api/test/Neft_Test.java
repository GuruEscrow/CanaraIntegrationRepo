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

public class Neft_Test {

	@Test
	public static void neftPayout()
			throws NoSuchAlgorithmException, JoseException, InvalidKeySpecException, DecoderException, IOException {
		CanarabankLive client = new CanarabankLive();
		try {
			String accName = "Guruprasad";
			String accNum = "4449682902";
			String ifscNum = "KKBK0008061";
			String amt = "1";
			String txnNote = "GuruImpsTest";
			String date = "06-09-2024";

			client.neftTransfer(accName, accNum, ifscNum, amt, txnNote, date);
			
			//Printing the timestamp
			DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			System.out.println("\ntimestamp: "+LocalDateTime.now().format(format));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public static void getNeftStatus() {
		CanarabankLive client = new CanarabankLive();

		try {
			client.neftStatus("PHED1732187257895");
			
			//Printing the timestamp
			DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			System.out.println("\ntimestamp: "+LocalDateTime.now().format(format));
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
