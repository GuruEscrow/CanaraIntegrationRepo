package payout.api.test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.codec.DecoderException;
import org.jose4j.lang.JoseException;

import ezpayouts.CanarabankLive;

public class Rtgs_Test {

	public static void main(String[] args)
			throws NoSuchAlgorithmException, JoseException, InvalidKeySpecException, DecoderException, IOException {
		CanarabankLive client = new CanarabankLive();
		try {
			String accName = "";
			String accNum = "";
			String ifscNum = "";
			String amt = "";
			String txnNote = "";
			String date = "";

			client.rtgsTransfer(accName, accNum, ifscNum, amt, txnNote,date);
			
			//Printing the timestamp
			DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			System.out.println("\ntimestamp: "+LocalDateTime.now().format(format));
			
			client.rtgsStatus("PHED1724405195621");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
