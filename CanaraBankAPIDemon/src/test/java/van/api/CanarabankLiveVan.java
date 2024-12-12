package van.api;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.apache.commons.codec.DecoderException;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kong.unirest.Unirest;

public class CanarabankLiveVan {

	public static final String CLIENT_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----"
			+ "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCtSlbcmD7Zap9f"
			+ "r+PgOcdrRLZHKhKzuFLGL1khnB18nVXPSaGjVucBKyL+x41+0yl1e/eri45uZwnf"
			+ "nUQX2hpIhh58Q//72ailPrWB3GstKagStjadB1bGpR8OCDM04soXKDmwJbf0nGxd"
			+ "OBbFMUQbNWeW4ENUmJNEWKTFh3/C3FhpRLorTgURHBb/AWnV3llRzxDNYY9cctep"
			+ "sdTcOF+8csOuJjKksbBiB5ibvlFVvuzCBdBn3ZLwpx1/wETdiOaQotQ69THrkCfE"
			+ "NQy9aT1RFxR3YJ7JWYs1rg2LuUc8w1Qr1+cWGZGVixJrfsrQzKk5kUhUZsXx+IYD"
			+ "IuCX6U8VAgMBAAECggEAEzWMIuhigSXTVF9SRpMJEtA7V3CnwZHTZSIvAm2EjEGB"
			+ "l1iNaSaTNVszhiQXQIGGVT5gcjzmVUEpREcx2uYDp1nsq7A7Ak86pVSp+KL3I3q8"
			+ "ZZSawbXkTeFUa7EHghuItN/x4x+PQHeNgJjc10+SgeSd3sjt1cUTAA7GdxoB1Nb6"
			+ "3p2aOlNlwSgOW4VHofQFf94a9DgkNgMt94nQNIn03dZmMBq1rhh8xuj2v5pncc+U"
			+ "lI7Iy2txYzAlIyB3Ru6zuyTW/zfXEKAC4+1GGK+gF+ct48+MlXS+pTTh+XddiuJI"
			+ "LqqaQtsgGZH1rG60koYvxouczhbqYe5ZkHHVPAPEaQKBgQDKGXtcA4raaEQdcaY+"
			+ "+EOrhIvJVZXtqwqIX/CLXJLJGvifDfIhZAPZcVCbceqOfMxK8OFhmLmUdKMTlGiA"
			+ "HY/zAbmoG3KyP+SmbSRHNcFvO0jFPup4AdL2ff3WBkCTzYnNnB6CgZ4BEEZS7jd6"
			+ "8LbPLg7fo27FLA6BKwjbiuZjKQKBgQDbgeNcn/4DKfb5vAZeeSaoRfOVnafv898V"
			+ "EzIal6XAFCSk9v/yviq0FYWa7KpGVTmNwKNFUTc5O0E6XyaquI4y7nv/gAZ+I5EX"
			+ "4/8pBxRWyuNB38/hsZ9DFIGHMUY9JauSvPpraLGfR1fkQYJfL++Sbo00wE6zLJdk"
			+ "rJFiZQLWDQKBgQC56CLgC6opKwytgSQKfNnS50SnWgFm9ZXkyUw/BlNIh3T46x02"
			+ "tggMd/5MX8gCdq5+qYLKAnGELJ7unfBbGdaXFZkxGL6zVa8BoRhkIQDlT0WrnCqX"
			+ "ZiYn9NIRlQLGc6Y160zthEpSdCQcmWGTx+aCQr4P3wlAezyEeY/WVLmFsQKBgHk+"
			+ "Yj4cJSrMOB2y7HWsR1z03lmKrmMMefDjHG6xypyww5jW0YLb5Sx5IsXy6Q5WLqcM"
			+ "e2JjPLSA9UNvoST1MZ4SOi1jIrLzpEXk6mBYB7T09dfB7soD2SstHWp2HgzSTNWN"
			+ "SmifeFS5DGQIhyFakeJ468fyXMX84FZ8NwV5M7rVAoGBAJ3nYeZG5nJFafD80UFy"
			+ "SWl//9ZR0rLSmv7zK9uioo/169BujexwYsJ6YZ/6MSwXcS1B3XxypMSLqT/MBfis"
			+ "3DwReH8AdI74XT3LjyfhYnxqVu8EOFVWP4KLiv/I2s9BQOtVo6CGUXSIJrhl6b4h" + "Qy5iJzhmvUXq6lPJTwLQJBrx"
			+ "-----END PRIVATE KEY-----".replaceAll("\n", "");

	/** Client's public-key/certificate */
	private static final String CLIENT_PUBLIC_CERT = "MIIFFjCCA/6gAwIBAgISA0l/eHDmauLZV9FEi8+Y/d9eMA0GCSqGSIb3DQEBCwUAMDMxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MQwwCgYDVQQDEwNSMTEwHhcNMjQwODEyMTM1NTU2WhcNMjQxMTEwMTM1NTU1WjAuMSwwKgYDVQQDEyNwaGVkb3JhdGVjaG5vbG9naWVzLmVhenlwYXlvdXRzLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAK1KVtyYPtlqn1+v4+A5x2tEtkcqErO4UsYvWSGcHXydVc9JoaNW5wErIv7HjX7TKXV796uLjm5nCd+dRBfaGkiGHnxD//vZqKU+tYHcay0pqBK2Np0HVsalHw4IMzTiyhcoObAlt/ScbF04FsUxRBs1Z5bgQ1SYk0RYpMWHf8LcWGlEuitOBREcFv8BadXeWVHPEM1hj1xy16mx1Nw4X7xyw64mMqSxsGIHmJu+UVW+7MIF0GfdkvCnHX/ARN2I5pCi1Dr1MeuQJ8Q1DL1pPVEXFHdgnslZizWuDYu5RzzDVCvX5xYZkZWLEmt+ytDMqTmRSFRmxfH4hgMi4JfpTxUCAwEAAaOCAicwggIjMA4GA1UdDwEB/wQEAwIFoDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwDAYDVR0TAQH/BAIwADAdBgNVHQ4EFgQUaUiS1wMte3FtjZtlPQvgt2cTruAwHwYDVR0jBBgwFoAUxc9GpOr0w8B6bJXELbBeki8m47kwVwYIKwYBBQUHAQEESzBJMCIGCCsGAQUFBzABhhZodHRwOi8vcjExLm8ubGVuY3Iub3JnMCMGCCsGAQUFBzAChhdodHRwOi8vcjExLmkubGVuY3Iub3JnLzAuBgNVHREEJzAlgiNwaGVkb3JhdGVjaG5vbG9naWVzLmVhenlwYXlvdXRzLmNvbTATBgNVHSAEDDAKMAgGBmeBDAECATCCAQQGCisGAQQB1nkCBAIEgfUEgfIA8AB2AEiw42vapkc0D+VqAvqdMOscUgHLVt0sgdm7v6s52IRzAAABkUcVVwUAAAQDAEcwRQIgKpdAB/ONBLcA3IlUK/9ml+QS2YMzcHNjjpTC4/1s3LYCIQCxsfiTLZw9BY4UK3t7uzCW/LekXSY9XZoosLhiZw3cHAB2AO7N0GTV2xrOxVy3nbTNE6Iyh0Z8vOzew1FIWUZxH7WbAAABkUcVVwYAAAQDAEcwRQIga9vPw7R2OrJOKBe2sfml6AVR2BGbzATW8+eTch9OA8YCIQD63s72jJvbsSwlXJadGNYO/CS9M+Ih69NpnACfvA7RhTANBgkqhkiG9w0BAQsFAAOCAQEAGZfdUeEcydnlw8QARhgqAmt2gIxHzCe279ct6jilKWqtk+3UiaKyAzO88Q00aeo4VF+i4JjztyWMlPuP9JM2y1wO3wa8dtVzyleuHpGLGPoljs19Yviak6tj8V+2fjcKsn9FU9TeatqlLZrl6cs74e1HULIYifBBnLr76awO3rN/H3ugyqRSWnj6mziNrwSJa7gk4Q8Z54eWjV3SY6iYPZyX5V15m6eAk2v6sftZhxjGV1PTUuYJn0qH67vM7W3fZeBai0wURr8/snvs/b0pabF3gS4iC7W59QwqguXAuAv79FQge7vFpEML7YWk/NsZ17YzEkhZbAfetOrVKZg8aA==";

	/** App Key obtained from Developer Portal during API Subscription */
	private static final String API_KEY = "dr3qPBlwZIYa2quLX40wAvAwPPpxuqrm";

	/** App Secret for the App Key */
	private static final String API_SECRET = "8tgd1usEYc52q5OyZHoAETI89GFRNsMR";

	/**** Symmetric (Shared) Key used for encryption and decryption */
	static String SHARED_SYMMETRIC_KEY = "c8a75e2e87b4520777615e71da03b2bf042b753099a309061574a8a725c33a79";
	
	private static final String BASE_URL = "https://apibanking.canarabank.in";

//	Van API's

	/** Van creation api to generate a simple van number */
	public void vanCreationSampleRequest(int vanCount, long startdate, long enddate, String externalRefNo)
			throws Exception {

		String ENCRYPTBODY = "{\n" + "           \"accountNo\": \"120029248620\",\n" + "           \"startDate\": "
				+ startdate + ",\n" + "			  \"endDate\": " + enddate + ",\n" + "           \"countVAN\": "
				+ vanCount + "\n" + "            }";

		String REQ_PAYLOAD = "{ \"Request\": { \"body\": { \"Service\": \"VANumResponse\", \"SessionContext\": { \"ExternalReferenceNo\": \""
				+ externalRefNo + "\" }, " + "\"encryptData\": \"%s\" } } }";

		String PLAIN_REQ_PAYLOAD = "{ \"Request\": { \"body\": { \"Service\": \"VANumResponse\", \"SessionContext\": { \"ExternalReferenceNo\": \""
				+ externalRefNo + "\" }, " + "\"encryptData\": { \"accountNo\": \"120029248620\", \"startDate\":"
				+ startdate + " , \"endDate\": " + enddate + ", \"countVAN\": " + vanCount + " } } } }";

		// To print json object in poper format
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String payload = String.format(REQ_PAYLOAD, encrypt(ENCRYPTBODY));
		System.out.println("Encrypted payload:\n" + payload);
		System.out.println("\nPlain payload:\n" + PLAIN_REQ_PAYLOAD);

		com.google.gson.JsonObject json = JsonParser.parseString(PLAIN_REQ_PAYLOAD).getAsJsonObject();

		String sign = sign(json.toString());
		System.out.println("\nSignature : " + sign);

		String url = BASE_URL+"/v1/van/creation";
		String response = invokeUniRequest(url, payload, sign);

		try {

			JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
			if (jsonResponse.get("Response") != null) {
				// System.out.println(response);
				String encryData = jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData")
						.getAsString();
				jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").addProperty("encryptData",
						decrypt(encryData));

				System.out.println("\nResposne: \n" + gson.toJson(jsonResponse));

				System.out.println("\nDecrypted Data: \n" + gson.toJson(JsonParser.parseString(decrypt(encryData))));

			} else {
				System.out.println("\nResponse is Null: " + jsonResponse);
			}

		} catch (Exception e) {
			System.out.println("\nError: " + response);
			e.printStackTrace();
		}

	}

	/** Van creation api to generate the customized van number */
	public void vanCreationCustomized(String extrenalRefNo, long startDate, long endDate,String [] customVanNumsArray)
			throws Exception {

		int countVan = customVanNumsArray.length;
		
		JsonArray customVanNums = new JsonArray();
		
	    for (int i = 0; i < customVanNumsArray.length; i++) {
	    	JsonObject object = new JsonObject();
	    	object.addProperty("vanNumber", customVanNumsArray[i]);
	    	
	    	customVanNums.add(object);
		}
		
		String ENCRYPTBODY = "{ " + " \"accountNo\": \"120029248620\", " + " \"startDate\": " + startDate + ", "
				+ " \"endDate\": " + endDate + ", " + " \"countVAN\": " + countVan + ", "
				+ " \"virtualAccountDetails\": "+customVanNums+" "
				+ " }";

		String REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"VANumResponse\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + extrenalRefNo + "\" " + " }, "
				+ " \"encryptData\": \"%s\" " + " } " + " } " + "}";

		String PLAIN_REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"VANumResponse\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + extrenalRefNo + "\" " + " }, "
				+ " \"encryptData\": { " + " \"accountNo\": \"120029248620\", " + " \"startDate\": " + startDate
				+ ", " + " \"endDate\": " + endDate + ", " + " \"countVAN\":" + countVan + ", "
				+ " \"virtualAccountDetails\": "+customVanNums+" "
				+ " } " + " } " + " } " + "}";

		// To print json object in poper format
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String payload = String.format(REQ_PAYLOAD, encrypt(ENCRYPTBODY));
		System.out.println("Encrypted payload:\n" + payload);
		System.out.println("\nPlain payload:\n" + PLAIN_REQ_PAYLOAD);

		com.google.gson.JsonObject json = JsonParser.parseString(PLAIN_REQ_PAYLOAD).getAsJsonObject();

		String sign = sign(json.toString());
		System.out.println("\nSignature : " + sign);

		String url = BASE_URL+"/v1/van/creation";
		String response = invokeUniRequest(url, payload, sign);

		try {

			JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
			if (jsonResponse.get("Response") != null) {
				// System.out.println(response);
				String encryData = jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData")
						.getAsString();
				jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").addProperty("encryptData",
						decrypt(encryData));

				System.out.println("\nResposne: \n" + gson.toJson(jsonResponse));

				System.out.println("\nDecrypted Data: \n" + gson.toJson(JsonParser.parseString(decrypt(encryData))));

			} else {
				System.out.println("\nResponse is Null: " + jsonResponse);
			}

		} catch (Exception e) {
			System.out.println("\nError: " + response);
			e.printStackTrace();
		}
	}

	/** This api to modify the expire date of van */
	public void vanModification(String vanNo, long endDate, String externalRef) throws Exception {

		String ENCRYPTBODY = "{ " + " \"accountNo\": \"120029248620\", " + " \"vanNo\": \"" + vanNo + "\", "
				+ " \"endDate\": " + endDate + " " + " }";

		String REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"VANModify\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, "
				+ " \"encryptData\": \"%s\"" + " } " + " } " + "} ";

		String PLAIN_REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"VANModify\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, "
				+ " \"encryptData\": { " + " \"accountNo\": \"120029248620\", " + " \"vanNo\": \"" + vanNo + "\", "
				+ " \"endDate\": " + endDate + " " + " } " + " } " + " } " + "} ";

		// To print json object in poper format
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String payload = String.format(REQ_PAYLOAD, encrypt(ENCRYPTBODY));
		System.out.println("Encrypted payload:\n" + gson.toJson(payload));
		System.out.println("\nPlain payload:\n" + gson.toJson(PLAIN_REQ_PAYLOAD));

		com.google.gson.JsonObject json = JsonParser.parseString(PLAIN_REQ_PAYLOAD).getAsJsonObject();

		String sign = sign(json.toString());
		System.out.println("\nSignature : " + sign);

		String url = BASE_URL+"/v1/van/van-modify";
		String response = invokeUniRequest(url, payload, sign);

		try {

			JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
			if (jsonResponse.get("Response") != null) {
				// System.out.println(response);
				String encryData = jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData")
						.getAsString();
				jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").addProperty("encryptData",
						decrypt(encryData));

				System.out.println("\nResposne: \n" + gson.toJson(jsonResponse));

				System.out.println("\nDecrypted Data: \n" + gson.toJson(JsonParser.parseString(decrypt(encryData))));

			} else {
				System.out.println("\nResponse is Null: " + jsonResponse);
			}

		} catch (Exception e) {
			System.out.println("\nError: " + response);
			e.printStackTrace();
		}
	}

	/** This api used to extract the van details like casa number, expiry of van */
	public void vanAccountNumberEnq(String vanNo, String externalRef) throws Exception {
		String ENCRYPTBODY = "{ " + " \"virtualAccountNumber\": \"" + vanNo + "\" " + " }";

		String REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { "
				+ " \"Service\": \"virtualAccountNumberEnquiry\", " + " \"SessionContext\": { "
				+ " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, " + " \"encryptData\": \"%s\"" + " } "
				+ " } " + "} " + "";

		String PLAIN_REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { "
				+ " \"Service\": \"virtualAccountNumberEnquiry\", " + " \"SessionContext\": { "
				+ " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, " + " \"encryptData\": { "
				+ " \"virtualAccountNumber\": \"" + vanNo + "\" " + " } " + " } " + " } " + "} " + "";

		// To print json object in poper format
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String payload = String.format(REQ_PAYLOAD, encrypt(ENCRYPTBODY));
		System.out.println("Encrypted payload:\n" + gson.toJson(payload));
		System.out.println("\nPlain payload:\n" + gson.toJson(PLAIN_REQ_PAYLOAD));

		JsonObject json = JsonParser.parseString(PLAIN_REQ_PAYLOAD).getAsJsonObject();

		String sign = sign(json.toString());
		System.out.println("\nSignature : " + sign);

		String url = BASE_URL+"/v1/van/van-dtls-enquiry";
		String response = invokeUniRequest(url, payload, sign);

		try {

			JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
			if (jsonResponse.get("Response") != null) {
				// System.out.println(response);
				String encryData = jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData")
						.getAsString();
				jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").addProperty("encryptData",
						decrypt(encryData));

				System.out.println("\nResposne: \n" + gson.toJson(jsonResponse));

				System.out.println("\nDecrypted Data: \n" + gson.toJson(JsonParser.parseString(decrypt(encryData))));
			} else {
				System.out.println("\nResponse is Null: " + jsonResponse);
			}

		} catch (Exception e) {
			System.out.println("\nError: " + response);
			e.printStackTrace();
		}
	}

	/**
	 * This api used to extract the van details by providing casa number and number
	 * of VANs
	 */
	public void vanAccountNumRetrieval(int NumOfVan, String externalRef) throws Exception {
		String ENCRYPTBODY = "{ " + " \"casaAccountNo\": \"120029248620\", " + " \"no_VACNO\": " + NumOfVan + " "
				+ " }";

		String REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { "
				+ " \"Service\": \"virtualAccountNumberRetrieval\", " + " \"SessionContext\": { "
				+ " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, " + " \"encryptData\": \"%s\" " + " } "
				+ " } " + "} ";

		String PLAIN_REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { "
				+ " \"Service\": \"virtualAccountNumberRetrieval\", " + " \"SessionContext\": { "
				+ " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, " + " \"encryptData\": { "
				+ " \"casaAccountNo\": \"120029248620\", " + " \"no_VACNO\": " + NumOfVan + " " + " } " + " } "
				+ " } " + "} ";

		// To print json object in poper format
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String payload = String.format(REQ_PAYLOAD, encrypt(ENCRYPTBODY));
		System.out.println("Encrypted payload:\n" + gson.toJson(payload));
		System.out.println("\nPlain payload:\n" + gson.toJson(PLAIN_REQ_PAYLOAD));

		com.google.gson.JsonObject json = JsonParser.parseString(PLAIN_REQ_PAYLOAD).getAsJsonObject();

		String sign = sign(json.toString());
		System.out.println("\nSignature : " + sign);

		String url = BASE_URL+"/v1/van/van-retrieval";
		String response = invokeUniRequest(url, payload, sign);

		try {

			JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
			if (jsonResponse.get("Response") != null) {
				// System.out.println(response);
				String encryData = jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData")
						.getAsString();
				jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").addProperty("encryptData",
						gson.toJson(JsonParser.parseString(decrypt(encryData))));

				System.out.println("\nDecrypted Data: \n" + gson.toJson(JsonParser.parseString(decrypt(encryData))));

				System.out.println("\nResposne: \n" + gson.toJson(jsonResponse));

			} else {
				System.out.println("\nResponse is Null: " + jsonResponse);
			}

		} catch (Exception e) {
			System.out.println("\nError: " + response);
			e.printStackTrace();
		}
	}

	/** This api use to extract the transactions details of van */
	public void vanTransactionInquiry(String vanNo, long fromDate, long toDate, String extrenalRef) throws Exception {
		String ENCRYPTBODY = "{ " + " \"vanNo\": \"" + vanNo + "\", " + " \"uniqRefNo\": \"\", " + " \"fromDate\": "
				+ fromDate + ", " + " \"toDate\": " + toDate + " " + " }";

		String REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"VANTxnInq\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + extrenalRef + "\" " + " }, "
				+ " \"encryptData\": \"%s\" " + " } " + " } " + "}";

		String PLAIN_REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"VANTxnInq\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + extrenalRef + "\" " + " }, "
				+ " \"encryptData\": { " + " \"vanNo\": \"" + vanNo + "\", " + " \"uniqRefNo\": \"\", "
				+ " \"fromDate\": " + fromDate + ", " + " \"toDate\": " + toDate + " " + " } " + " } " + " } " + "}";

		// To print json object in poper format
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String payload = String.format(REQ_PAYLOAD, encrypt(ENCRYPTBODY));
		System.out.println("Encrypted payload:\n" + gson.toJson(payload));
		System.out.println("\nPlain payload:\n" + gson.toJson(PLAIN_REQ_PAYLOAD));

		com.google.gson.JsonObject json = JsonParser.parseString(PLAIN_REQ_PAYLOAD).getAsJsonObject();

		String sign = sign(json.toString());
		System.out.println("\nSignature : " + sign);

		String url = BASE_URL+"/v1/van/transactionInquiry";
		String response = invokeUniRequest(url, payload, sign);

		try {

			JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
			if (jsonResponse.get("Response") != null) {
				// System.out.println(response);
				String encryData = jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData")
						.getAsString();
				jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").addProperty("encryptData",
						decrypt(encryData));

				System.out.println("\nResposne: \n" + gson.toJson(jsonResponse));

				System.out.println("\nDecrypted Data: \n" + gson.toJson(JsonParser.parseString(decrypt(encryData))));

			} else {
				System.out.println("\nResponse is Null: " + jsonResponse);
			}

		} catch (Exception e) {
			System.out.println("\nError: " + response);
			e.printStackTrace();
		}
	}

	/** This api is use to extract the transaction using casa account number */
	public void getTxnStmByUsingCasaAccNum(String casaAccNo, long fromDate, long toDate, String externalRef)
			throws Exception {
		String ENCRYPTBODY = "{ " + " \"acctNo\": \"" + casaAccNo + "\", " + " \"uniqRefNo\": \"\", "
				+ " \"fromDate\": " + fromDate + ", " + " \"toDate\": " + toDate + " " + " }";

		String REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"TxnInqVan\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, "
				+ " \"encryptData\": \"%s\" " + " } " + " } " + "}";

		String PLAIN_REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"TxnInqVan\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, "
				+ " \"encryptData\": { " + " \"acctNo\": \"" + casaAccNo + "\", " + " \"uniqRefNo\": \"\", "
				+ " \"fromDate\": " + fromDate + ", " + " \"toDate\": " + toDate + " " + " } " + " } " + " } " + "}";

		// To print json object in poper format
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String payload = String.format(REQ_PAYLOAD, encrypt(ENCRYPTBODY));
		System.out.println("Encrypted payload:\n" + gson.toJson(payload));
		System.out.println("\nPlain payload:\n" + gson.toJson(PLAIN_REQ_PAYLOAD));

		com.google.gson.JsonObject json = JsonParser.parseString(PLAIN_REQ_PAYLOAD).getAsJsonObject();

		String sign = sign(json.toString());
		System.out.println("\nSignature : " + sign);

		String url = BASE_URL+"/v1/van/casaTransactionInquiry";
		String response = invokeUniRequest(url, payload, sign);

		try {

			JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
			if (jsonResponse.get("Response") != null) {
				// System.out.println(response);
				String encryData = jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData")
						.getAsString();
				jsonResponse.getAsJsonObject("Response").getAsJsonObject("body").addProperty("encryptData",
						decrypt(encryData));

				System.out.println("\nResposne: \n" + gson.toJson(jsonResponse));

				System.out.println("\nDecrypted Data: \n" + gson.toJson(JsonParser.parseString(decrypt(encryData))));

			} else {
				System.out.println("\nResponse is Null: " + jsonResponse);
			}

		} catch (Exception e) {
			System.out.println("\nError: " + response);
			e.printStackTrace();
		}
	}

//	Main method to execute the code
	public static void main(String[] args) throws Exception {

		CanarabankVan client = new CanarabankVan();

		// To Create simple Van
//		client.vanCreationSampleRequest(1, 20240202, 20240921, "Guru00012");
		// To create customize Van
//		client.vanCreationCustomized(API_KEY, 0, 0, 0);

		// To Modify the enddate of Van
//		client.vanModification(API_SECRET, 0, API_KEY);

		// To Van account number enquiry
//		client.vanAccountNumberEnq(API_SECRET, API_KEY);

		// To retrieve the VAN account number using casa number
//		client.vanAccountNumRetrieval(1, "VANRetrieval001");

		// To retrieve the transaction of VAN
//		client.vanTransactionInquiry(API_SECRET, 0, 0, API_KEY);

		// To retrieve the transaction of VAN using casa account
//		client.getTxnStmByUsingCasaAccNum("120029248620", 20240202, 20240203, "StmFetch1");
	}

	/** Method to encrypt the given input per JWE specification */
	private String encrypt(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException, JoseException,
			InvalidKeySpecException, DecoderException {
		// System.out.println("String to encrypt:" + input);
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A256KW);
		jwe.setKey(new AesKey(digest()));
		// jwe.setCompressionAlgorithmHeaderParameter(CompressionAlgorithmIdentifiers.DEFLATE);
		jwe.setPayload(input);
		String encrypted = jwe.getCompactSerialization();
		// System.out.println(encrypted);
		return encrypted;
	}

	/** Method to decrypt the given input per JWE specification */
	private String decrypt(String input) throws JoseException, NoSuchAlgorithmException, UnsupportedEncodingException,
			InvalidKeySpecException, DecoderException {
		// System.out.println("String to decrypt:" + input);
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setCompactSerialization(input);
		AesKey aes = new AesKey(digest());
		jwe.setKey(aes);
		String plaintext = jwe.getPlaintextString();
		return plaintext;
		// And do whatever you need to do with the clear text message.
		// System.out.println("Decrypted Text: " + plaintext);

	}

	/**
	 * Method responsible for calling the specificed API and return the API response
	 */
	private String invokeUniRequest(String url, String payload, String signature) {
		// Unirest.setTimeouts(0, 0);
		kong.unirest.HttpResponse<String> response = Unirest.post(url).header("x-client-id", API_KEY)
				.header("x-client-secret", API_SECRET)
				.header("x-api-interaction-id", "fccfdade2a4c4616b76e4837f5ea4ae2").header("x-timestamp", "1675780846")
				.header("x-client-certificate", CLIENT_PUBLIC_CERT).header("x-signature", signature)
				.header("Content-Type", "application/json")
				.header("Cookie",
						"1122; TS01ea60e3=01aee67679c686b25f6fff23b7e82ee443fbcf7777927798d6167f014830f9fe80355ed560765eed2826bf9854c1f19b829f27dc2d; TS01ea60e3028=0192426cf8e858e9bfb109d227872f0e0517dd288d0a90c12f79c8fa30dbee07ff9189200d1d4ec8fcc47d9958f1016e268bb37d2f")
				.header("X-Forwarded-For", "weasdwd")
				.header("Authorization", "Bearer " + "kRr99ftSA7rLiri1AQDZGDOBeG5ANAhJ").body(payload).asString();
		return response.getBody();

	}

	public String sign(String input) {
		String realPK = CLIENT_PRIVATE_KEY.replaceAll("-----END PRIVATE KEY-----", "")
				.replaceAll("-----BEGIN PRIVATE KEY-----", "").replaceAll("\n", "");
		byte[] b1 = Base64.getDecoder().decode(realPK);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			Signature privateSignature = Signature.getInstance("SHA256withRSA");
			privateSignature.initSign(kf.generatePrivate(spec));
			privateSignature.update(input.getBytes("UTF-8"));
			byte[] s = privateSignature.sign();
			return Base64.getEncoder().encodeToString(s);
		} catch (InvalidKeyException e) {

			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* utility function to convert a given symmetric key into binary format */
	private byte[] digest()
			throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException, DecoderException {
		byte[] val = new byte[SHARED_SYMMETRIC_KEY.length() / 2];
		for (int i = 0; i < val.length; i++) {
			int index = i * 2;
			int j = Integer.parseInt(SHARED_SYMMETRIC_KEY.substring(index, index + 2), 16);
			val[i] = (byte) j;
		}
		return val;
	}
}
