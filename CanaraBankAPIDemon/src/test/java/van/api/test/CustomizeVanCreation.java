package van.api.test;

import org.testng.annotations.Test;

import van.api.CanarabankLiveVan;

public class CustomizeVanCreation {

	@Test
	public void customVanCreation() throws Exception {
         
//		CanarabankVan client = new CanarabankVan();
		CanarabankLiveVan client = new CanarabankLiveVan();
		
		/*
		 * externalRef should be unique for each request,
		 * validFrom should be in YYYYMMDD,
		 * validTill should be in YYYYMMDD,
		 * vanNumbers can be any characters
		 */
		String externalRef = "LiveCustomizeVan004";
		long validFrom = 20241203;
		long validTill = 20251020;
		String [] customVanNums = {"PHEDORAGURU001"};
		
		//To create customize Van
		client.vanCreationCustomized(externalRef, validFrom, validTill,customVanNums);
	}

}
