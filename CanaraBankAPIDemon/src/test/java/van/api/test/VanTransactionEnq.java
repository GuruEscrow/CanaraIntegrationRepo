package van.api.test;

import org.testng.annotations.Test;

import van.api.CanarabankLiveVan;

public class VanTransactionEnq {

	@Test
	public void vanTxnEnq() throws Exception {

		//CanarabankVan client = new CanarabankVan();
		CanarabankLiveVan client = new CanarabankLiveVan();
		/*
		 * vanNumbers existing one, starDate should be in YYYYMMDD, endDate should be in
		 * YYYYMMDD, externalRef should be unique every time
		 */
		String vanNumber = "PHEDORAGURU001"; //05573000000379994
		long starDate = 20241203;
		long endDate = 20241203;
		String externalRef = "LiveVanTxnEnq012";

		// To retrieve the transaction of VAN
		client.vanTransactionInquiry(vanNumber, starDate, endDate, externalRef);
	}

}
