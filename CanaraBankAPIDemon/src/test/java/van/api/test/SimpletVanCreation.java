package van.api.test;

import org.testng.annotations.Test;

import van.api.CanarabankLiveVan;

public class SimpletVanCreation {
	
	@Test
	public void simpleVanCreation() throws Exception {
		
		//CanarabankVan client = new CanarabankVan();
		CanarabankLiveVan client = new CanarabankLiveVan();
		/*
		 * vanCountToCreate should be integer,
		 * validFrom should be in YYYYMMDD,
		 * validTill should be in YYYYMMDD,
		 * externalRef should be unique for each request
		 */
		int vanCountToCreate = 1;
		long validFrom = 20241203;
		long validTill = 20251020;
		String externalRef = "LiveSimpleVanCreation004";
		
		//To Create simple Van
		client.vanCreationSampleRequest(vanCountToCreate, validFrom, validTill, externalRef);
    
	}

}
