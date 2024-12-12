package van.api.test;

import org.testng.annotations.Test;

import van.api.CanarabankLiveVan;

public class VanAccountNumEnq {

	@Test
	public void vanEnquiry() throws Exception {
		
		//CanarabankVan client = new CanarabankVan();
		CanarabankLiveVan client = new CanarabankLiveVan();
		/*
		 * vanNumber should be exiting one,
		 * externalRef should be unique every time
		 */
		String vanNumber = "PHEDORAGURU001";
		String externalRef = "LiveVanNumEnq011";
		
		//To Van account number enquiry
		client.vanAccountNumberEnq(vanNumber, externalRef);
	}
}
