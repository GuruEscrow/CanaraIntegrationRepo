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

public class Imps_Test {

	@Test
	public static void impsPayout()
			throws NoSuchAlgorithmException, JoseException, InvalidKeySpecException, DecoderException, IOException {
		CanarabankLive client = new CanarabankLive();
		try {
			String accName = "Guruprasad";
			String accNum = "924020017800104";
			String ifscNum = "UTIB0004575";
			String amt = "1";
			String txnNote = "GuruImpsTest";

			client.impsTransfer(accName, accNum, ifscNum,amt , txnNote);
			
			//Printing the timestamp
			DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			System.out.println("\ntimestamp: "+LocalDateTime.now().format(format));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public static void getImpsStatus() {
		CanarabankLive client = new CanarabankLive();

		try {
			client.impsStatus("PHED1732187257895");
			
			//Printing the timestamp
			DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			System.out.println("\ntimestamp: "+LocalDateTime.now().format(format));
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
