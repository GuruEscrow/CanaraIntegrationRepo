package van.api.test;

import org.testng.annotations.Test;

import van.api.CanarabankLiveVan;

public class CasaTxmEnq {

	@Test
	public void casaTxnEnq() throws Exception {

		CanarabankLiveVan client = new CanarabankLiveVan();

		/*
		 * casaAccNum existing one, starDate should be in YYYYMMDD, endDate should be in
		 * YYYYMMDD, externalRef should be unique every time
		 */
		String casaAccNum = "120029248620";
		long starDate = 20241203;
		long endDate = 20241203;
		String externalRef = "LiveCasaTxnEnq006";

		// To retrieve the transaction of VAN using casa account
		client.getTxnStmByUsingCasaAccNum(casaAccNum, starDate, endDate, externalRef);
	}
}
