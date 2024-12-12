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

public class CanarabankVan {

	public static final String CLIENT_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----"
			+ "MIIJQgIBADANBgkqhkiG9w0BAQEFAASCCSwwggkoAgEAAoICAQCwEKsAgcC+DZh/"
			+ "prfCK9I3M+M0U/WeDGxuJFLSXcMDeo4NzJ/rJ689KzNnBP3N6iaCs4iW+Bc5s2Kf"
			+ "R7TGif3cwcMsGa1/a3vtdXtItbfcB4PljYfY7qn4cUHcu9kIZXHXn12JJSXVnlBI"
			+ "eWuoWeVBh7PgSsCVM3hynUabcEt0fYytSzH3s0++x2LTodx3vhfQtR6CAwDvLx21"
			+ "N7HEFut61xohkDiyTnvCR2ojQvAXFhzQ+mbjn9DK+gUPzBZCfIpq/id0BRGAZKJQ"
			+ "c6eJXpNMVh4ZK0qqgJz4a7E8m2EA1osp5QRUmK5TXooGRQVF3885QnDaWFtO1pSk"
			+ "rzI9UwfKCV4d9nKUVK7xy6/lDhVCUsd8Rc9EY/QJ9EG7BwzIjXneVEfY1wdoSeZA"
			+ "53tDkucl+nCoe7K1C4U+dGOCwUB/KnCKnp5uRs4/nsV/mt1NxYX+A2TrB44iGGvY"
			+ "DW6oz2hz5RVhctUO+Y8jRxKga6a7obJ3s1V0h4yfVxsz5jM83pehs5hONqRtRMm4"
			+ "zJE+Jmh72IsnMBD9G3Ts555nM62p8egwKSu6ze4+fNiIrmyNDKCqrHjT05Y8QxmQ"
			+ "d9UhI61X0xg9kV2XGZPTmTtShsuXNUYXVVRBK5V5p/uoLMZBlJHuHfMtv7/OtdVw"
			+ "pKqDHLSuFl8ONRt+yMo6G6i6HqkOwQIDAQABAoICAAQBTaH1f1/DZ3ujnGyU8WW3"
			+ "bw8EbKL4J7yTiL1+i+Lw1hTq6/fwWsWeDX/BL+uP6CAAzXFhcXPPdjx5Q24YDUn/"
			+ "1Dy5nhhtwh1CDdzbdRjJtoxtAeNaUIMDHotAlRyiUdh28aODa+y6w+RD4NR2B+eU"
			+ "9hQD++CY/4jlWlw46VXuXsfUQZt7IaLfsJy453ysQ81aN69EXrmJBpiV+m4jdzFb"
			+ "NKcBTfqVQBwa4pCstZqIRNkLJAjSeAvDIruyzEVCs1yqNnp46lRBVcoMXcDk09UJ"
			+ "wAp0454bzoQHKtW5gPwlkvd9wd9IWSTXSZpJaJWxAg+DbV7Yc+pshYxR7F4crz0a"
			+ "RJEsz0TdUBCTiGNppFQLpxOSCkkHRwBrunHxMcdgUHD/AlTDvZIW0cvWpM7cvKpb"
			+ "ZzUEZ0B0QHqXDH0VEeOCnE8LlGfWahwUncOutu9q/Ea3yrAkbkVrvIgSyt9wxIxN"
			+ "l0R5XN0k9fIUCbJihqvMGpZEEpHjLlBTv3RNqfP6Ljumatn2YqbpLTvTOnZg+mQa"
			+ "674QWRx4F6//Tn1zSxCMEFIy+CK0F0ORW+WfNKN+8TRzoxFI3c5G0HZTdzgelF09"
			+ "Shihb5YoKb2csIi/G7m8NpUy06LW9VBF7/s6zw73t60lj8RfnyllB5aLlgWrZoCV"
			+ "shlX5n3maTA1pkQCbyhtAoIBAQDb6ttNaXMpf4Z9zUJfOrMrcW8J9VYVO3J6XQEe"
			+ "ml17pjMNVtFU5PZJN46ReH6ic+cQZIrhMFCbCoPtvRwTOBF7LGzSz2ZYDyt/q8BN"
			+ "2kBCSbwLn7v0BlOJfTzMBNxds98LHFDaky7b3NvbAY7JaurRYfzEuOy1qlQ7E3CX"
			+ "ptqmM6eSpobr9r1SDZiKTLvlggjknRibfMt5kwbpTQybPCiKe4pzuYifFiZhbv5e"
			+ "gR+GCtdzf2q63YMM3FYkbYXFklqX+/riJNP2ysPoe/HRwPfPYI49wMSfwrIdeAvl"
			+ "5DMLJvowl+7gr5xajzDGxha/4LgZsu1aOcPdMdIPwKGriMWlAoIBAQDM8+SGbpQZ"
			+ "31BKgyCFrH0EJmFUrU2hbbHsj/kIeiuROTFXewfmmvJUy2havqOt/zXAnAdMUci0"
			+ "DKkOG/lu1vp+N3ZXfBfqMVOkhV5xjJNftKIqv+o3MfL5LysGhf//H27GFzPMit3M"
			+ "GlhmhLJwgXcUuhJtSZE3EhjPa6m5pMxqtFVFxX1pnfR346B4h65T4LA29N+CFUPH"
			+ "8KNvgAkJSJ8Iq9lwFp9hgzISDCXPKRIJF8KB297FaSzkWawKJa73NnO+pk3qBz+v"
			+ "r1Lq4SS6k/sDfztLjI1s/f59j5iI/mqRFWMlnWR41heSk5bUhBbAEDro6WZ0s49o"
			+ "GY3Vc7FfZ7HtAoIBAQDV2pKburmEV0B/wUSTuk9VJZ+m/W5Sg1aVay+VhOL3RO1M"
			+ "QWqatXRSSrZW++eZLkA6DquGxCb+PcGBRG6yuck2EENa1NhfNZFFO81rZ5spWu6E"
			+ "Fzb829IMVz7x3qA0dp+k/Eob4xikMSeS1vb1IwVpy3G0E2gMWLBEKmlI77ab/Ist"
			+ "7sYxW+VKj3IQuwHU8xiWx7WQLlDnznPgF070/hEmaXB/jKIwoad9BCRT3hi7jntp"
			+ "EzTEtnej3/fcwKxfLizustiLZOjCivbrcuv63z2b6fcoUfZirT/rrDtVuQ1yANs2"
			+ "uehPkjXnUmtKQIMK4Anm47owDRBMXxA4xUfxuLUJAoIBAAyxklbNJHkRvqxOwMXm"
			+ "r/uA0Qhoj2VPIPTw2dCvmGEvNKIhaZTbxeSAkJ8GLacKJm8eesk8S3zzR8kq7GD0"
			+ "CaGll7vFYMnNcku/QuEvfrUvv9d7+Wqfule6lySZSgioDrlQGJIzi5cnjpAS6eZT"
			+ "7mcnOxrvsoHBqi22klWAInT9ZEak+6Z99PUz8O1mIeLqCJ/uvKUW4hLREZ5HRMgB"
			+ "anAkDJyHmil08bXycKkK9yX/Bbn7Pynk4LXn4+LhAJwpEDQforW66zPbXPxVI2w2"
			+ "3BYUXDzKoLu+Y3OtYBoj/7+qnnBm9iERyXX3lCDnaW1N1Ag25HSX5mrhIkq1Fqg9"
			+ "COUCggEAPomg0uZqI7cyHJrcE7Dh3nyw9oPH8CNAaOXNYPpfOS1fF3bysD8qexQ6"
			+ "oD+ZGscuCZ78S+9LfiU9RwIpmEqemjG+yUakZdxp2o4xm2kZYmgQRRGjxDgKxV0Z"
			+ "MxLD3mVfm682YDowub3Wtka0MTOMZfX2Za1DNZV2oaH0xDHEeyz2oEDigO8nRWSr"
			+ "y+sxqW2wrRKVmThtJnDN/KgLipN5MsuyA6Kw5eCVp9V+botfSltNnppgFgJ9kMps"
			+ "Q7B+XE8+HD3lmsyZW9CMuU3jdvAJUyqzgrz2mbfiOtivffc2uD6TTziNNVSUsf0T" + "nx1uGqqXEvfrDTxkCDhRDQvXxnIsTQ=="
			+ "-----END PRIVATE KEY-----".replaceAll("\n", "");

	/** Client's public-key/certificate */
	private static final String CLIENT_PUBLIC_CERT = "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";

	/** App Key obtained from Developer Portal during API Subscription */
	private static final String API_KEY = "5Hdon6b1AAK7yQCdsl4S3w5hQCQ7q3hE";

	/** App Secret for the App Key */
	private static final String API_SECRET = "yepHSbaJkrhjatkJwoqHWJgZmcclwNbl";

	/**** Symmetric (Shared) Key used for encryption and decryption */
	static String SHARED_SYMMETRIC_KEY = "a4de60fb88ca907e1fe52c082b62d0487557c898b431df0123be51680bb76fb3";

//	Van API's

	/** Van creation api to generate a simple van number */
	public void vanCreationSampleRequest(int vanCount, long startdate, long enddate, String externalRefNo)
			throws Exception {

		String ENCRYPTBODY = "{\n" + "           \"accountNo\": \"04502020000655\",\n" + "           \"startDate\": "
				+ startdate + ",\n" + "			  \"endDate\": " + enddate + ",\n" + "           \"countVAN\": "
				+ vanCount + "\n" + "            }";

		String REQ_PAYLOAD = "{ \"Request\": { \"body\": { \"Service\": \"VANumResponse\", \"SessionContext\": { \"ExternalReferenceNo\": \""
				+ externalRefNo + "\" }, " + "\"encryptData\": \"%s\" } } }";

		String PLAIN_REQ_PAYLOAD = "{ \"Request\": { \"body\": { \"Service\": \"VANumResponse\", \"SessionContext\": { \"ExternalReferenceNo\": \""
				+ externalRefNo + "\" }, " + "\"encryptData\": { \"accountNo\": \"04502020000655\", \"startDate\":"
				+ startdate + " , \"endDate\": " + enddate + ", \"countVAN\": " + vanCount + " } } } }";

		// To print json object in poper format
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String payload = String.format(REQ_PAYLOAD, encrypt(ENCRYPTBODY));
		System.out.println("Encrypted payload:\n" + gson.toJson(payload));
		System.out.println("\nPlain payload:\n" + gson.toJson(PLAIN_REQ_PAYLOAD));

		com.google.gson.JsonObject json = JsonParser.parseString(PLAIN_REQ_PAYLOAD).getAsJsonObject();

		String sign = sign(json.toString());
		System.out.println("\nSignature : " + sign);

		String url = "https://uat-apibanking.canarabank.in/v1/van/creation";
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
		
		String ENCRYPTBODY = "{ " + " \"accountNo\": \"04502020000655\", " + " \"startDate\": " + startDate + ", "
				+ " \"endDate\": " + endDate + ", " + " \"countVAN\": " + countVan + ", "
				+ " \"virtualAccountDetails\": "+customVanNums+" "
				+ " }";

		String REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"VANumResponse\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + extrenalRefNo + "\" " + " }, "
				+ " \"encryptData\": \"%s\" " + " } " + " } " + "}";

		String PLAIN_REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"VANumResponse\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + extrenalRefNo + "\" " + " }, "
				+ " \"encryptData\": { " + " \"accountNo\": \"04502020000655\", " + " \"startDate\": " + startDate
				+ ", " + " \"endDate\": " + endDate + ", " + " \"countVAN\":" + countVan + ", "
				+ " \"virtualAccountDetails\": "+customVanNums+" "
				+ " } " + " } " + " } " + "}";

		// To print json object in poper format
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String payload = String.format(REQ_PAYLOAD, encrypt(ENCRYPTBODY));
		System.out.println("Encrypted payload:\n" + gson.toJson(payload));
		System.out.println("\nPlain payload:\n" + gson.toJson(PLAIN_REQ_PAYLOAD));

		com.google.gson.JsonObject json = JsonParser.parseString(PLAIN_REQ_PAYLOAD).getAsJsonObject();

		String sign = sign(json.toString());
		System.out.println("\nSignature : " + sign);

		String url = "https://uat-apibanking.canarabank.in/v1/van/creation";
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

		String ENCRYPTBODY = "{ " + " \"accountNo\": \"04502020000655\", " + " \"vanNo\": \"" + vanNo + "\", "
				+ " \"endDate\": " + endDate + " " + " }";

		String REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"VANModify\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, "
				+ " \"encryptData\": \"%s\"" + " } " + " } " + "} ";

		String PLAIN_REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { " + " \"Service\": \"VANModify\", "
				+ " \"SessionContext\": { " + " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, "
				+ " \"encryptData\": { " + " \"accountNo\": \"04502020000655\", " + " \"vanNo\": \"" + vanNo + "\", "
				+ " \"endDate\": " + endDate + " " + " } " + " } " + " } " + "} ";

		// To print json object in poper format
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String payload = String.format(REQ_PAYLOAD, encrypt(ENCRYPTBODY));
		System.out.println("Encrypted payload:\n" + gson.toJson(payload));
		System.out.println("\nPlain payload:\n" + gson.toJson(PLAIN_REQ_PAYLOAD));

		com.google.gson.JsonObject json = JsonParser.parseString(PLAIN_REQ_PAYLOAD).getAsJsonObject();

		String sign = sign(json.toString());
		System.out.println("\nSignature : " + sign);

		String url = "https://uat-apibanking.canarabank.in/v1/van/van-modify";
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

		String url = "https://uat-apibanking.canarabank.in/v1/van/van-dtls-enquiry";
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
		String ENCRYPTBODY = "{ " + " \"casaAccountNo\": \"04502020000655\", " + " \"no_VACNO\": " + NumOfVan + " "
				+ " }";

		String REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { "
				+ " \"Service\": \"virtualAccountNumberRetrieval\", " + " \"SessionContext\": { "
				+ " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, " + " \"encryptData\": \"%s\" " + " } "
				+ " } " + "} ";

		String PLAIN_REQ_PAYLOAD = "{ " + " \"Request\": { " + " \"body\": { "
				+ " \"Service\": \"virtualAccountNumberRetrieval\", " + " \"SessionContext\": { "
				+ " \"ExternalReferenceNo\": \"" + externalRef + "\" " + " }, " + " \"encryptData\": { "
				+ " \"casaAccountNo\": \"04502020000655\", " + " \"no_VACNO\": " + NumOfVan + " " + " } " + " } "
				+ " } " + "} ";

		// To print json object in poper format
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String payload = String.format(REQ_PAYLOAD, encrypt(ENCRYPTBODY));
		System.out.println("Encrypted payload:\n" + gson.toJson(payload));
		System.out.println("\nPlain payload:\n" + gson.toJson(PLAIN_REQ_PAYLOAD));

		com.google.gson.JsonObject json = JsonParser.parseString(PLAIN_REQ_PAYLOAD).getAsJsonObject();

		String sign = sign(json.toString());
		System.out.println("\nSignature : " + sign);

		String url = "https://uat-apibanking.canarabank.in/v1/van/van-retrieval";
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

		String url = "https://uat-apibanking.canarabank.in/v1/van/transactionInquiry";
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

		String url = "https://uat-apibanking.canarabank.in/v1/van/casaTransactionInquiry";
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
//		client.getTxnStmByUsingCasaAccNum("04502020000655", 20240202, 20240203, "StmFetch1");
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
				.header("Authorization", "Bearer " + "K54mBt89uU3frpyU3egtXTBj1L2Ni9zP").body(payload).asString();
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
