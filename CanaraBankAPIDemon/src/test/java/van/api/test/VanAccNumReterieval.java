package van.api.test;

import org.testng.annotations.Test;

import van.api.CanarabankLiveVan;

public class VanAccNumReterieval {

	@Test
	public void vanReterivalByCasa() throws Exception {
		
		//CanarabankVan client = new CanarabankVan();
		CanarabankLiveVan client = new CanarabankLiveVan();
		
		/*
		 * countToFetchVanNum should be integer,
		 * externalRef should be unique every time
		 */
		int countToFetchVanNum = 4;
		String externalRef = "LiveVANRetrieval013";
		
		//To retrieve the VAN account numbers using casa number
		client.vanAccountNumRetrieval(countToFetchVanNum, externalRef);
	}
}
