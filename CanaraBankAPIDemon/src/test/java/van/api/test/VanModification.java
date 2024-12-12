package van.api.test;

import van.api.CanarabankVan;

public class VanModification {

	public static void main(String[] args) throws Exception {
		
		CanarabankVan client = new CanarabankVan();
		
		/*
		 * vanNumber should be existing one,
		 * extendValidationDate should be in YYYYMMDD,
		 * externalRef should be unique for each request
		 */
		String vanNumber = "10456000000400628";
		long extendValidationDate = 20250922;
		String externalRef = "VanModify001";
		
		//To Modify the enddate of Van
		client.vanModification(vanNumber, extendValidationDate, externalRef);
	}
}
