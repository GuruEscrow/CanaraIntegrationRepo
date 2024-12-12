package payout.api.test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.codec.DecoderException;
import org.jose4j.lang.JoseException;
import org.testng.annotations.Test;

import payout.api.CanarabankLive;

public class Account_stats {

//	Method to get the avlBalance...................
	@Test
	public static void getAvlBalance() {

		CanarabankLive client = new CanarabankLive();

		try {
			client.getBalance();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//	Method to get the getAccStatement............
	@Test
	public static void getAccStatement()
			throws NoSuchAlgorithmException, JoseException, InvalidKeySpecException, DecoderException, IOException {

		CanarabankLive client = new CanarabankLive();

		try {
			String startDate = "03-12-2024";
			String endDate = "03-12-2024";

			client.getStatement(startDate,endDate );
			
			//Printing the timestamp
			DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			System.out.println("\ntimestamp: "+LocalDateTime.now().format(format));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
