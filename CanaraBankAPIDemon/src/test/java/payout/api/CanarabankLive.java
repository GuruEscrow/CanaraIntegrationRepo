package payout.api;
 

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpResponse;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kong.unirest.Unirest;
public class CanarabankLive {

  /** part of the request payload that needs to be encrypted */
  // private static final String DATA_TO_ENCRYPT = "{\"Authorization\":\"Basic
  // MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\"acctNumber\":\"120029248620 \",\"customerID\":\"325273791\"}";
   //120029248620
  /** URL to invoke the API */

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
+ "3DwReH8AdI74XT3LjyfhYnxqVu8EOFVWP4KLiv/I2s9BQOtVo6CGUXSIJrhl6b4h"
+ "Qy5iJzhmvUXq6lPJTwLQJBrx"
+ "-----END PRIVATE KEY-----".replaceAll("\n", "");

  /** Client's public-key/certificate */
  private static final String CLIENT_PUBLIC_CERT = "MIIFFjCCA/6gAwIBAgISA0l/eHDmauLZV9FEi8+Y/d9eMA0GCSqGSIb3DQEBCwUAMDMxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MQwwCgYDVQQDEwNSMTEwHhcNMjQwODEyMTM1NTU2WhcNMjQxMTEwMTM1NTU1WjAuMSwwKgYDVQQDEyNwaGVkb3JhdGVjaG5vbG9naWVzLmVhenlwYXlvdXRzLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAK1KVtyYPtlqn1+v4+A5x2tEtkcqErO4UsYvWSGcHXydVc9JoaNW5wErIv7HjX7TKXV796uLjm5nCd+dRBfaGkiGHnxD//vZqKU+tYHcay0pqBK2Np0HVsalHw4IMzTiyhcoObAlt/ScbF04FsUxRBs1Z5bgQ1SYk0RYpMWHf8LcWGlEuitOBREcFv8BadXeWVHPEM1hj1xy16mx1Nw4X7xyw64mMqSxsGIHmJu+UVW+7MIF0GfdkvCnHX/ARN2I5pCi1Dr1MeuQJ8Q1DL1pPVEXFHdgnslZizWuDYu5RzzDVCvX5xYZkZWLEmt+ytDMqTmRSFRmxfH4hgMi4JfpTxUCAwEAAaOCAicwggIjMA4GA1UdDwEB/wQEAwIFoDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwDAYDVR0TAQH/BAIwADAdBgNVHQ4EFgQUaUiS1wMte3FtjZtlPQvgt2cTruAwHwYDVR0jBBgwFoAUxc9GpOr0w8B6bJXELbBeki8m47kwVwYIKwYBBQUHAQEESzBJMCIGCCsGAQUFBzABhhZodHRwOi8vcjExLm8ubGVuY3Iub3JnMCMGCCsGAQUFBzAChhdodHRwOi8vcjExLmkubGVuY3Iub3JnLzAuBgNVHREEJzAlgiNwaGVkb3JhdGVjaG5vbG9naWVzLmVhenlwYXlvdXRzLmNvbTATBgNVHSAEDDAKMAgGBmeBDAECATCCAQQGCisGAQQB1nkCBAIEgfUEgfIA8AB2AEiw42vapkc0D+VqAvqdMOscUgHLVt0sgdm7v6s52IRzAAABkUcVVwUAAAQDAEcwRQIgKpdAB/ONBLcA3IlUK/9ml+QS2YMzcHNjjpTC4/1s3LYCIQCxsfiTLZw9BY4UK3t7uzCW/LekXSY9XZoosLhiZw3cHAB2AO7N0GTV2xrOxVy3nbTNE6Iyh0Z8vOzew1FIWUZxH7WbAAABkUcVVwYAAAQDAEcwRQIga9vPw7R2OrJOKBe2sfml6AVR2BGbzATW8+eTch9OA8YCIQD63s72jJvbsSwlXJadGNYO/CS9M+Ih69NpnACfvA7RhTANBgkqhkiG9w0BAQsFAAOCAQEAGZfdUeEcydnlw8QARhgqAmt2gIxHzCe279ct6jilKWqtk+3UiaKyAzO88Q00aeo4VF+i4JjztyWMlPuP9JM2y1wO3wa8dtVzyleuHpGLGPoljs19Yviak6tj8V+2fjcKsn9FU9TeatqlLZrl6cs74e1HULIYifBBnLr76awO3rN/H3ugyqRSWnj6mziNrwSJa7gk4Q8Z54eWjV3SY6iYPZyX5V15m6eAk2v6sftZhxjGV1PTUuYJn0qH67vM7W3fZeBai0wURr8/snvs/b0pabF3gS4iC7W59QwqguXAuAv79FQge7vFpEML7YWk/NsZ17YzEkhZbAfetOrVKZg8aA==";

  // "MIICsTCCAZkCAQAwDQYJKoZIhvcNAQEFBQAwITEfMB0GA1UEAwwWdGVzdC1pc3N1ZXItcm9vdC1hc3ltbTAeFw0yMDA1MDYxNzAzNTVaFw0yMTA1MDYxNzAzNTVaMBwxGjAYBgNVBAMMEXRlc3QtY2xpZW50LWFzeW1tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvCAjOba32a72K8C4lh5141+1hjsq1V5KwEcTKqKekI3ZVEjK5iCmZ1ldp14J9sG8DUOW4kHhyawc1z8ESdT5AC/ww3PMixYsgSkLc5wPd/2J2C1iiOoxiqYbO/OVmyZe7vloRxLqS9sA46iBifW5eAN1fpdByZOYI544qlp732WpEIEfRfEWHNJ9nPnuUmwnqEGa16RZ5TnB2SgZ4pW3fN/huiJIRqk+oXlSzDHSq2iSEZp2hdLrkjHHc463J8B3DtgafbZSvd1W3Xz3KNFjox1Q456CdGLmWIhQb8QYk8I5Jly3gnKZZH8EBM70XK34xxz0GUXywmwfaIQ2ZBKiJwIDAQABMA0GCSqGSIb3DQEBBQUAA4IBAQAYi+KNRBqWzXI4ea8XT6ixtIA49IoYmiuqRuZ/ivfcbX53hgpKozhc08gAMKZ3lU5XuAFzaTP7Tl7jgC4sR2E6Srfjvcdg6doSpmTNYZ54N+m5DSSNTwbNE7CRpJNLOqN28EAMJykXsFoTFEfCiyWUedSrfFl8qJwOivo3C6QPdiWDRnhUnA7WNGVuzXJVcBz9yLcgY8CIma1b+uUaEbiytYgWx1bl8qXhkAMKyl7wy66oHIMpbmnb7LWtoXhCDBxHXKKRSgH6z10Xp88P/fBwIq+ZIV9zEGd92a23otxyj0EM8qe5XumTZpfGyApkESAWtzMXiCBrEvbDTXreCwdR";

  /** App Key obtained from Developer Portal during API Subscription */
  private static final String API_KEY = "dr3qPBlwZIYa2quLX40wAvAwPPpxuqrm";// "dcf7b96c52d73411e47ed24a249221e0";

  /** App Secret for the App Key */
  private static final String API_SECRET = "8tgd1usEYc52q5OyZHoAETI89GFRNsMR";// "451a65004ac58632f442eac0f890e62d";

  /**
   * Request (json) payload before encryption
   * private static final String REQ_BODY = "{"
   * + "\"Request\": "
   * + "{ \"body\": {"
   * + "\"branchCode\": \"5573\","
   * + "\"encryptData\": \"%s\""
   * + "}"
   * + "}"
   * + "}";
   */

  // static String SHARED_SYMMETRIC_KEY =
  // "f2b0be3685954bf45ca797a1cb547924cdaa8514b93d25860570115bdebe2177";
  /**** Symmetric (Shared) Key used for encryption and decryption */
  static String SHARED_SYMMETRIC_KEY = "c8a75e2e87b4520777615e71da03b2bf042b753099a309061574a8a725c33a79";

  public void getBalance() throws Exception {
    String url = "https://apibanking.canarabank.in/v1/paymentinquiry/balanceinquiry";
    String DATA_TO_ENCRYPT = "{\n"
        + "                \"Authorization\":\"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"acctNumber\":\"120029248620\",\n"
        + "                \"customerID\":\"325273791\"\n"
        + "            }";

    String REQ_BODY = "{\n"
        + "    \"Request\":{\n"
        + "        \"body\":{\n"
        + "            \"branchCode\":\"5573\",\n"
        + "            \"encryptData\":\"%s\"\n"
        + "        }\n"
        + "    }\n"
        + "}";

    /** UNENCRYPTED PAYLOAD */
    String PAY_LOAD_PLAIN = "{\n"
        + "    \"Request\":{\n"
        + "        \"body\":{\n"
        + "            \"branchCode\":\"5573\",\n"
        + "            \"encryptData\":{\n"
        + "                \"Authorization\":\"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"acctNumber\":\"120029248620\",\n"
        + "                \"customerID\":\"325273791\"\n"
        + "            }\n"
        + "        }\n"
        + "    }\n"
        + "}";
    String encrypted = encrypt(DATA_TO_ENCRYPT);

    String payload = String.format(REQ_BODY, encrypted);

    com.google.gson.JsonObject json = JsonParser.parseString(PAY_LOAD_PLAIN).getAsJsonObject();

    String sign = sign(json.toString());

    System.out.println(DATA_TO_ENCRYPT);
    System.out.println("Payload With Encrypted Body");
    System.out.println(payload);
    String response = invokeUniRequest(url, payload, sign);
    JsonObject responseObj = JsonParser.parseString(response).getAsJsonObject();
    System.out.println(responseObj);
    if (responseObj.get("Response") != null) {
      String encr = responseObj.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData").toString();
      System.out.println(decrypt(encr.replaceAll("\"", "")));
    } else {
      System.err.println(responseObj);
    }
  }

  public void getStatement(String fromDate, String toDate) throws Exception {
    String url = "https://apibanking.canarabank.in/v1/paymentinquiry/acc-statement";
    String DATA_TO_ENCRYPT = "{\n"
        + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"acctNumber\": \"120029248620\",\n"
        + "                \"customerID\": \"325273791\",\n"
        + "                \"NUMBEROFTXN\": \"1\",\n"
        + "                \"FROMDATE\": \""+fromDate+"\",\n"
        + "                \"TODATE\": \""+toDate+"\",\n"
        + "                \"searchBy\": \"3\",\n"
        + "                \"branchCode\": \"5573\"\n"
        + "            }";

    String REQ_BODY = "{\n"
        + "    \"Request\":{\n"
        + "        \"body\":{\n"
        + "            \"encryptData\":\"%s\"\n"
        + "        }\n"
        + "    }\n"
        + "}";

    /** UNENCRYPTED PAYLOAD */
    String PAY_LOAD_PLAIN = "{\n"
        + "    \"Request\":{\n"
        + "        \"body\":{\n"
        + "            \"encryptData\": {\n"
        + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"acctNumber\": \"120029248620\",\n"
        + "                \"customerID\": \"325273791\",\n"
        + "                \"NUMBEROFTXN\": \"1\",\n"
        + "                \"FROMDATE\": \""+fromDate+"\",\n"
        + "                \"TODATE\": \""+toDate+"\",\n"
        + "                \"searchBy\": \"3\",\n"
        + "                \"branchCode\": \"5573\"\n"
        + "            }\n"
        + "        }\n"
        + "    }\n"
        + "}";
    String encrypted = encrypt(DATA_TO_ENCRYPT);

    String payload = String.format(REQ_BODY, encrypted);

 // To print json object in poper format
 	Gson gson = new GsonBuilder().setPrettyPrinting().create();
 		
    com.google.gson.JsonObject json = JsonParser.parseString(PAY_LOAD_PLAIN).getAsJsonObject();

    String sign = sign(json.toString());
    
    System.out.println(DATA_TO_ENCRYPT);
    System.out.println("Payload With Encrypted Body");
    System.out.println(payload);
    System.out.println("URL : "+url);
    String response = invokeUniRequest(url, payload, sign);
    JsonObject responseObj = JsonParser.parseString(response).getAsJsonObject();
    System.out.println(responseObj);
    if (responseObj.get("Response") != null) {
      String encr = responseObj.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData").toString();
      String statementsStr = decrypt(encr.replaceAll("\"", ""));
      System.out.println("\nDecrypted Data: \n" + gson.toJson(JsonParser.parseString(decrypt(encr.replaceAll("\"", "")))));
    } else {
      System.err.println(responseObj);
    }
  }

  public void neftTransfer(String beneName,String beneAccNo,String beneIFSC,String amount,String transactionNote,String valueDate) throws Exception {
    String userRefNo = "PHED"+System.currentTimeMillis();
    String DATA_TO_ENCRYPT = "{\n"
        + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"txnPassword\": \"4a11a44a2a49a0a94a87a83a115\",\n"
        + "                \"srcAcctNumber\": \"120029248620\",\n"
        + "                \"destAcctNumber\": \""+beneAccNo+"\",\n"
        + "                \"customerID\": \"325273791\",\n"
        + "                \"txnAmount\": \""+amount+"\",\n"
        + "                \"benefName\": \""+beneName+"\",\n"
        + "                \"userRefNo\": \""+userRefNo+"\"\n"
        + "            }";
    String REQ_BODY = "{\n    \"Request\": {\n"
        + "        \"body\": {\n"
        + "            \"srcAccountDetails\": {\n"
        + "                \"identity\": \"B001\",\n"
        + "                \"currency\": \"INR\",\n"
        + "                \"branchCode\": \"5573\"\n"
        + "            },\n"
        + "            \"destAccountDetails\": {\n"
        + "                \"identity\": \"B001\",\n"
        + "                \"currency\": \"INR\"\n"
        + "            },\n"
        + "            \"txnCurrency\": \"INR\",\n"
        + "            \"ifscCode\": \""+beneIFSC+"\",\n"
        + "            \"narration\": \""+transactionNote+"\",\n"
        + "            \"paymentMode\": \"N\",\n"
        + "            \"valueDate\": \""+valueDate+"\",\n"
        + "            \"encryptData\": \"%s\" \n"
        + "            }\n"
        + "        }\n"
        + "    }";

    /** UNENCRYPTED PAYLOAD */
    String PAY_LOAD_PLAIN = "{\n    \"Request\": {\n"
        + "        \"body\": {\n"
        + "            \"srcAccountDetails\": {\n"
        + "                \"identity\": \"B001\",\n"
        + "                \"currency\": \"INR\",\n"
        + "                \"branchCode\": \"5573\"\n"
        + "            },\n"
        + "            \"destAccountDetails\": {\n"
        + "                \"identity\": \"B001\",\n"
        + "                \"currency\": \"INR\"\n"
        + "            },\n"
        + "            \"txnCurrency\": \"INR\",\n"
        + "            \"ifscCode\": \""+beneIFSC+"\",\n"
        + "            \"narration\": \""+transactionNote+"\",\n"
        + "            \"paymentMode\": \"N\",\n"
        + "            \"valueDate\":  \""+valueDate+"\",\n"
        + "            \"encryptData\": {\n"
        + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"txnPassword\": \"4a11a44a2a49a0a94a87a83a115\",\n"
        + "                \"srcAcctNumber\": \"120029248620\",\n"
        + "                \"destAcctNumber\": \""+beneAccNo+"\",\n"
        + "                \"customerID\": \"325273791\",\n"
        + "                \"txnAmount\": \""+amount+"\",\n"
        + "                \"benefName\": \""+beneName+"\",\n"
        + "                 \"userRefNo\": \""+userRefNo+"\"\n"
        + "            }\n"
        + "        }\n"
        + "    }\n"
        + "}";

    String encrypted = encrypt(DATA_TO_ENCRYPT);

    String payload = String.format(REQ_BODY, encrypted);
    
 // To print json object in poper format
 	Gson gson = new GsonBuilder().setPrettyPrinting().create();

    com.google.gson.JsonObject json = JsonParser.parseString(PAY_LOAD_PLAIN).getAsJsonObject();

    String sign = sign(json.toString());
    System.out.println("Signature : " + sign);
    System.out.println("plain payload:\n"+PAY_LOAD_PLAIN);
    System.out.println(DATA_TO_ENCRYPT);
    System.out.println("Payload With Encrypted Body");
    System.out.println(payload);
    String url = "https://apibanking.canarabank.in/apib/payment/neft";
    String response = invokeUniRequest(url, payload, sign);
    try {
      JsonObject responseObj = JsonParser.parseString(response).getAsJsonObject();
      if (responseObj.get("Response") != null) {
        String encr = responseObj.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData").toString();
        System.out.println("\nDecrypted Data: \n" + gson.toJson(JsonParser.parseString(decrypt(encr.replaceAll("\"", "")))));
      } else {
        System.err.println(responseObj);
      }
    } catch (Exception ex) {
      System.out.println(response);
      ex.printStackTrace();
      ;
    }

  }

  public void neftStatus(String userRefNo) throws Exception { 
    // String userRefNo = "PHED1724401223009";
    String UTR = "";
    String DATA_TO_ENCRYPT = "{\n"
        + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"UTR\": \""+UTR+"\",\n"
        + "                \"UserRefno\": \""+userRefNo+"\",\n"
        + "                \"TransactionType\": \"NEFT\",\n"
        + "                \"CustomerID\": \"325273791\"\n"
        + "            }";

    String REQ_BODY = "{\n    \"Request\": {\n"
        + "        \"body\": {\n"
        + "            \"encryptData\": \"%s\" \n"
        + "            }\n"
        + "        }\n"
        + "    }";

    /** UNENCRYPTED PAYLOAD */
    String PAY_LOAD_PLAIN = "{\n    \"Request\": {\n"
        + "        \"body\": {\n"
        + "            \"encryptData\": {\n"
        + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"UTR\": \""+UTR+"\",\n"
        + "                \"UserRefno\": \""+userRefNo+"\",\n"
        + "                \"TransactionType\": \"NEFT\",\n"
        + "                \"CustomerID\": \"325273791\"\n"
        + "            }\n"
        + "        }\n"
        + "    }\n"
        + "}";

    String encrypted = encrypt(DATA_TO_ENCRYPT);
    String payload = String.format(REQ_BODY, encrypted);
    com.google.gson.JsonObject json = JsonParser.parseString(PAY_LOAD_PLAIN).getAsJsonObject();

    String sign = sign(json.toString());
    System.out.println("Signature : " + sign);
    System.out.println(DATA_TO_ENCRYPT);
    System.out.println("Plaintext Payload : \n"+PAY_LOAD_PLAIN);
    System.out.println("Payload With Encrypted Body");
    System.out.println(payload);
    String url = "https://apibanking.canarabank.in/v1/paymentinquiry/neftfate";
    System.out.println("URL : "+url);
    String response = invokeUniRequest(url, payload, sign);
    try {
      JsonObject responseObj = JsonParser.parseString(response).getAsJsonObject();
     
      if (responseObj.get("Response") != null) {
        String encr = responseObj.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData").toString();
        System.out.println(decrypt(encr.replaceAll("\"", "")));
      } else {
        System.err.println(responseObj);
      }
    } catch (Exception ex) {
      System.out.println(response);
      ex.printStackTrace();
      ;
    }
  }

  public void rtgsTransfer(String beneName,String beneAccNo,String beneIFSC,String amount,String transactionNote,String valueDate) throws Exception {
      String userRefNo = "PHED"+System.currentTimeMillis();
      String DATA_TO_ENCRYPT = "{\n"
          + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
          + "                \"txnPassword\": \"4a11a44a2a49a0a94a87a83a115\",\n"
          + "                \"srcAcctNumber\": \"120029248620\",\n"
          + "                \"destAcctNumber\": \""+beneAccNo+"\",\n"
          + "                \"customerID\": \"325273791\",\n"
          + "                \"txnAmount\": \""+amount+"\",\n"
          + "                \"benefName\": \""+beneName+"\",\n"
          + "                \"userRefNo\": \""+userRefNo+"\"\n"
          + "            }";
      String REQ_BODY = "{\n    \"Request\": {\n"
          + "        \"body\": {\n"
          + "            \"srcAccountDetails\": {\n"
          // + "                \"identity\": \"B001\",\n"
          + "                \"currency\": \"INR\",\n"
          + "                \"branchCode\": \"5573\"\n"
          + "            },\n"
          + "            \"destAccountDetails\": {\n"
          + "                \"identity\": \"B001\",\n"
          + "                \"currency\": \"INR\"\n"
          + "            },\n"
          + "            \"txnCurrency\": \"INR\",\n"
          + "            \"benefBankName\": \"State Bank of India\",\n"
          + "            \"benefBranchName\": \"M G Road Bangalore\",\n"
          + "            \"ifscCode\": \""+beneIFSC+"\",\n"
          + "            \"narration\": \""+transactionNote+"\",\n"
          + "            \"encryptData\": \"%s\" \n"
          + "            }\n"
          + "        }\n"
          + "    }";
  
      /** UNENCRYPTED PAYLOAD */
      String PAY_LOAD_PLAIN = "{\n    \"Request\": {\n"
          + "        \"body\": {\n"
          + "            \"srcAccountDetails\": {\n"
          // + "                \"identity\": \"B001\",\n"
          + "                \"currency\": \"INR\",\n"
          + "                \"branchCode\": \"5573\"\n"
          + "            },\n"
          + "            \"destAccountDetails\": {\n"
          + "                \"identity\": \"B001\",\n"
          + "                \"currency\": \"INR\"\n"
          + "            },\n"
          + "            \"txnCurrency\": \"INR\",\n"
          + "            \"benefBankName\": \"State Bank of India\",\n"
          + "            \"benefBranchName\": \"M G Road Bangalore\",\n"
          + "            \"ifscCode\": \""+beneIFSC+"\",\n"
          + "            \"narration\": \""+transactionNote+"\",\n"
          + "            \"encryptData\": {\n"
          + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
          + "                \"txnPassword\": \"4a11a44a2a49a0a94a87a83a115\",\n"
          + "                \"srcAcctNumber\": \"120029248620\",\n"
          + "                \"destAcctNumber\": \""+beneAccNo+"\",\n"
          + "                \"customerID\": \"325273791\",\n"
          + "                \"txnAmount\": \""+amount+"\",\n"
          + "                \"benefName\": \""+beneName+"\",\n"
          + "                \"userRefNo\": \""+userRefNo+"\"\n"
          + "            }\n"
          + "        }\n"
          + "    }\n"
          + "}";
  
      String encrypted = encrypt(DATA_TO_ENCRYPT);
  
      String payload = String.format(REQ_BODY, encrypted);
  
      com.google.gson.JsonObject json = JsonParser.parseString(PAY_LOAD_PLAIN).getAsJsonObject();
  
      String sign = sign(json.toString());
      System.out.println("Signature : " + sign);
      System.out.println(DATA_TO_ENCRYPT);

      
      System.out.println("Plaintext Payload :\n"+PAY_LOAD_PLAIN);


      System.out.println("Payload With Encrypted Body");
      System.out.println(payload);
      String url = "https://apibanking.canarabank.in/apib/payment/rtgs";
      System.out.println("Url : "+url);
      String response = invokeUniRequest(url, payload, sign);
      try {
        JsonObject responseObj = JsonParser.parseString(response).getAsJsonObject();
        if (responseObj.get("Response") != null) {
          String encr = responseObj.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData").toString();
          System.out.println(decrypt(encr.replaceAll("\"", "")));
        } else {
          System.err.println(responseObj);
        }
      } catch (Exception ex) {
        System.out.println(response);
        ex.printStackTrace();
        ;
      }
  
   
  }
  
  public void rtgsStatus(String userRefNo) throws Exception {
      String UTR = "";
      String DATA_TO_ENCRYPT = "{\n"
      + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
      + "                \"UTR\": \""+UTR+"\",\n"
      + "                \"UserRefno\": \""+userRefNo+"\",\n"
      + "                \"TransactionType\": \"RTGS\",\n"
      + "                \"CustomerID\": \"325273791\"\n"
      + "            }";

  String REQ_BODY = "{\n    \"Request\": {\n"
      + "        \"body\": {\n"
      + "            \"encryptData\": \"%s\" \n"
      + "            }\n"
      + "        }\n"
      + "    }";

  /** UNENCRYPTED PAYLOAD */
  String PAY_LOAD_PLAIN = "{\n    \"Request\": {\n"
      + "        \"body\": {\n"
      + "            \"encryptData\": {\n"
      + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
      + "                \"UTR\": \""+UTR+"\",\n"
      + "                \"UserRefno\": \""+userRefNo+"\",\n"
      + "                \"TransactionType\": \"RTGS\",\n"
      + "                \"CustomerID\": \"325273791\"\n"
      + "            }\n"
      + "        }\n"
      + "    }\n"
      + "}";
  
      String encrypted = encrypt(DATA_TO_ENCRYPT);
  
      String payload = String.format(REQ_BODY, encrypted);
  
      com.google.gson.JsonObject json = JsonParser.parseString(PAY_LOAD_PLAIN).getAsJsonObject();
  
      String sign = sign(json.toString());
      System.out.println("Signature : " + sign);
      System.out.println(DATA_TO_ENCRYPT);

      
      System.out.println("Plaintext Payload :\n"+PAY_LOAD_PLAIN);


      System.out.println("Payload With Encrypted Body");
      System.out.println(payload);
      String url = "https://apibanking.canarabank.in/v1/paymentinquiry/rtgsfate";
      System.out.println(("URL : "+url));
      String response = invokeUniRequest(url, payload, sign);
      try {
        JsonObject responseObj = JsonParser.parseString(response).getAsJsonObject();
        if (responseObj.get("Response") != null) {
          String encr = responseObj.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData").toString();
          System.out.println(decrypt(encr.replaceAll("\"", "")));
        } else {
          System.err.println(responseObj);
        }
      } catch (Exception ex) {
        System.out.println(response);
        ex.printStackTrace();
        ;
      }
  
   
  }

  public void impsTransfer(String beneName,String beneAccNo,String beneIFSC,String amount,String transactionNote) throws Exception {
    String userRefNo = "PHED"+System.currentTimeMillis();
    String DATA_TO_ENCRYPT = "{\n"
        + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"txnPassword\": \"4a11a44a2a49a0a94a87a83a115\",\n"
        + "                \"srcAcctNumber\": \"120029248620\",\n"
        + "                \"destAcctNumber\": \""+beneAccNo+"\",\n"
        + "                \"customerID\": \"325273791\",\n"
        + "                \"txnAmount\": "+amount+",\n"
        + "                \"benefName\": \""+beneName+"\",\n"
        + "                \"userRefNo\": \""+userRefNo+"\"\n"
        + "            }";
    String REQ_BODY = "{\n"
        + "    \"Request\": {\n"
        + "        \"body\": {\n"
        + "            \"srcAccountDetails\": {\n"
        + "                \"currency\": \"INR\",\n"
        + "                \"branchCode\": \"5573\",\n"
        + "                \"branchName\": \"SMEBIDADI\"\n"
        + "            },\n"
        + "            \"destAccountDetails\": {\n"
        + "                \"ifscCode\": \""+beneIFSC+"\",\n"
        + "                \"benefBankName\": \""+beneIFSC+"\"\n"
        + "            },\n"
        + "            \"txnCurrency\": \"INR\",\n"
        + "            \"purpose\": \""+transactionNote+"\",\n"
        + "            \"encryptData\": \"%s\" \n"
        + "        }\n"
        + "    }\n"
        + "}";

    /** UNENCRYPTED PAYLOAD */
    String PAY_LOAD_PLAIN = "{\n"
        + "    \"Request\": {\n"
        + "        \"body\": {\n"
        + "            \"srcAccountDetails\": {\n"
        + "                \"currency\": \"INR\",\n"
        + "                \"branchCode\": \"5573\",\n"
        + "                \"branchName\": \"SMEBIDADI\"\n"
        + "            },\n"
        + "            \"destAccountDetails\": {\n"
        + "                \"ifscCode\": \""+beneIFSC+"\",\n"
        + "                \"benefBankName\": \""+beneIFSC+"\"\n"
        + "            },\n"
        + "            \"txnCurrency\": \"INR\",\n"
        + "            \"purpose\": \""+transactionNote+"\",\n"
        + "            \"encryptData\": {\n"
        + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"txnPassword\": \"4a11a44a2a49a0a94a87a83a115\",\n"
        + "                \"srcAcctNumber\": \"120029248620\",\n"
        + "                \"destAcctNumber\": \""+beneAccNo+"\",\n"
        + "                \"customerID\": \"325273791\",\n"
        + "                \"txnAmount\": "+amount+",\n"
        + "                \"benefName\": \""+beneName+"\",\n"
        + "                \"userRefNo\": \""+userRefNo+"\"\n"
        + "            }\n"
        + "        }\n"
        + "    }\n"
        + "}";

    String encrypted = encrypt(DATA_TO_ENCRYPT);

    String payload = String.format(REQ_BODY, encrypted);

    com.google.gson.JsonObject json = JsonParser.parseString(PAY_LOAD_PLAIN).getAsJsonObject();
    String url = "https://apibanking.canarabank.in/apib/payment/imps";

    String sign = sign(json.toString());
    System.out.println("Signature : " + sign);
    System.out.println("Plaintext Request Payload : "+PAY_LOAD_PLAIN);
    System.out.println(PAY_LOAD_PLAIN);
    System.out.println("Payload With Encrypted Body");
    System.out.println(payload);
    System.out.println("URL :"+url);
    String response = invokeUniRequest(url, payload, sign);
    try {
      JsonObject responseObj = JsonParser.parseString(response).getAsJsonObject();
      if (responseObj.get("Response") != null) {
        String encr = responseObj.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData").toString();
        System.out.println(decrypt(encr.replaceAll("\"", "")));
      } else {
        System.err.println(responseObj);
      }
    } catch (Exception ex) {
      System.out.println(response);
      ex.printStackTrace();
      ;
    }

  }

  public void impsStatus(String userRefNo) throws Exception {
    // String userRefNo = "PHED1724328579995";
     
      String DATA_TO_ENCRYPT = "{\n"
      + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
      + "                \"customerID\": \"325273791\"\n"
      + "            }";

  String REQ_BODY = "{\n    \"Request\": {\n"
      + "        \"body\": {\n"
      + "            \"userRefNumber\": \""+userRefNo+"\",\n"
      + "            \"encryptData\": \"%s\" \n"
      + "            }\n"
      + "        }\n"
      + "    }";

  /** UNENCRYPTED PAYLOAD */
  String PAY_LOAD_PLAIN = "{\n    \"Request\": {\n"
      + "        \"body\": {\n"
      + "            \"userRefNumber\": \""+userRefNo+"\",\n"
      + "            \"encryptData\": {\n"
      + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
      + "                \"customerID\": \"325273791\"\n"
      + "            }\n"
      + "        }\n"
      + "    }\n"
      + "}";
  
      String encrypted = encrypt(DATA_TO_ENCRYPT);
  
      String payload = String.format(REQ_BODY, encrypted);
  
      com.google.gson.JsonObject json = JsonParser.parseString(PAY_LOAD_PLAIN).getAsJsonObject();
  
      String sign = sign(json.toString());
      System.out.println("Signature : " + sign);
      System.out.println(DATA_TO_ENCRYPT);

      
      System.out.println("Plaintext Payload :\n"+PAY_LOAD_PLAIN);


      System.out.println("Payload With Encrypted Body");
      System.out.println(payload);
      String url = "https://apibanking.canarabank.in/v1/paymentinquiry/status/inquiry";
      System.out.println("URL : "+url);
      String response = invokeUniRequest(url, payload, sign);
      try {
        JsonObject responseObj = JsonParser.parseString(response).getAsJsonObject();
        if (responseObj.get("Response") != null) {
          String encr = responseObj.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData").toString();
          System.out.println("Response : "+responseObj.toString());
          System.out.println(decrypt(encr.replaceAll("\"", "")));
        } else {
          System.err.println(responseObj);
        }
      } catch (Exception ex) {
        System.out.println(response);
        ex.printStackTrace();
        ;
      }
  }

  public void internalTransfer(String beneName,String beneAccNo,String amount,String transactionNote,String valueDate) throws Exception {
    String userRefNo = "PHED"+System.currentTimeMillis();
    String DATA_TO_ENCRYPT = "{\n"
        + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"txnPassword\": \"4a11a44a2a49a0a94a87a83a115\",\n"
        + "                \"srcAcctNumber\": \"120029248620\",\n"
        + "                \"destAcctNumber\": \""+beneAccNo+"\",\n"
        + "                \"customerID\": \"325273791\",\n"
        + "                \"txnAmount\": "+amount+",\n"
        + "                \"benefName\": \""+beneName+"\",\n"
        + "                \"userRefNo\": \""+userRefNo+"\"\n"
        + "            }";
    String REQ_BODY = "{\n    \"Request\": {\n"
        + "        \"body\": {\n"
        + "            \"srcAccountDetails\": {\n"
        + "                \"identity\": \"B001\",\n"
        + "                \"currency\": \"INR\",\n"
        + "                \"branchCode\": \"5573\"\n"
        + "            },\n"
        + "            \"destAccountDetails\": {\n"
        + "                \"identity\": \"B001\",\n"
        + "                \"currency\": \"INR\"\n"
        + "            },\n"
        + "            \"txnCurrency\": \"INR\",\n"
        + "            \"narration\": \""+transactionNote+"\",\n"
        + "            \"paymentMode\": \"N\",\n"
        + "            \"valueDate\": \""+valueDate+"\",\n"
        + "            \"encryptData\": \"%s\" \n"
        + "            }\n"
        + "        }\n"
        + "    }";

    /** UNENCRYPTED PAYLOAD */
    String PAY_LOAD_PLAIN = "{\n    \"Request\": {\n"
        + "        \"body\": {\n"
        + "            \"srcAccountDetails\": {\n"
        + "                \"identity\": \"B001\",\n"
        + "                \"currency\": \"INR\",\n"
        + "                \"branchCode\": \"5573\"\n"
        + "            },\n"
        + "            \"destAccountDetails\": {\n"
        + "                \"identity\": \"B001\",\n"
        + "                \"currency\": \"INR\"\n"
        + "            },\n"
        + "            \"txnCurrency\": \"INR\",\n"
        + "            \"narration\": \""+transactionNote+"\",\n"
        + "            \"paymentMode\": \"N\",\n"
        + "            \"valueDate\": \""+valueDate+"\",\n"
        + "            \"encryptData\": {\n"
        + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
        + "                \"txnPassword\": \"4a11a44a2a49a0a94a87a83a115\",\n"
        + "                \"srcAcctNumber\": \"120029248620\",\n"
        + "                \"destAcctNumber\": \""+beneAccNo+"\",\n"
        + "                \"customerID\": \"325273791\",\n"
        + "                \"txnAmount\": "+amount+",\n"
        + "                \"benefName\": \""+beneName+"\",\n"
        + "                \"userRefNo\": \""+userRefNo+"\"\n"
        + "            }\n"
        + "        }\n"
        + "    }\n"
        + "}";

    String encrypted = encrypt(DATA_TO_ENCRYPT);

    String payload = String.format(REQ_BODY, encrypted);

    com.google.gson.JsonObject json = JsonParser.parseString(PAY_LOAD_PLAIN).getAsJsonObject();

    String sign = sign(json.toString());
    System.out.println("Signature : " + sign);

    System.out.println("Plaintext Payload : \n"+PAY_LOAD_PLAIN);

    System.out.println(DATA_TO_ENCRYPT);
    System.out.println("Payload With Encrypted Body");
    System.out.println(payload);
    String url = "https://apibanking.canarabank.in/apib/payment/internaltransfer";
    System.out.println("URL :"+url);

    String response = invokeUniRequest(url, payload, sign);
    try {
      JsonObject responseObj = JsonParser.parseString(response).getAsJsonObject();
      if (responseObj.get("Response") != null) {
        String encr = responseObj.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData").toString();
        System.out.println(decrypt(encr.replaceAll("\"", "")));
      } else {
        System.err.println(responseObj);
      }
    } catch (Exception ex) {
      System.out.println(response);
      ex.printStackTrace();
      ;
    }

  }

  public void internalTransferStatus(String userRefNo) throws Exception {
      // String userRefNo = "PHED1720501686125";
     
      String DATA_TO_ENCRYPT = "{\n"
      + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
      + "                \"customerID\": \"325273791\"\n"
      + "            }";

  String REQ_BODY = "{\n    \"Request\": {\n"
      + "        \"body\": {\n"
      + "            \"userRefNumber\": \""+userRefNo+"\",\n"
      + "            \"encryptData\": \"%s\" \n"
      + "            }\n"
      + "        }\n"
      + "    }";

  /** UNENCRYPTED PAYLOAD */
  String PAY_LOAD_PLAIN = "{\n    \"Request\": {\n"
      + "        \"body\": {\n"
      + "            \"userRefNumber\": \""+userRefNo+"\",\n"
      + "            \"encryptData\": {\n"
      + "                \"Authorization\": \"Basic MzI1MjczNzkxQVBJOjRhOGE0OGEyNGE0MWEwYTk0YTg3YTgzYTExMg==\",\n"
      + "                \"customerID\": \"325273791\"\n"
      + "            }\n"
      + "        }\n"
      + "    }\n"
      + "}";
  
      String encrypted = encrypt(DATA_TO_ENCRYPT);
  
      String payload = String.format(REQ_BODY, encrypted);
  
      com.google.gson.JsonObject json = JsonParser.parseString(PAY_LOAD_PLAIN).getAsJsonObject();
  
      String sign = sign(json.toString());
      System.out.println("Signature : " + sign);
      System.out.println(DATA_TO_ENCRYPT);

      
      System.out.println("Plaintext Payload :\n"+PAY_LOAD_PLAIN);


      System.out.println("Payload With Encrypted Body");
      System.out.println(payload);
      String url = "https://apibanking.canarabank.in/v1/paymentinquiry/status/inquiry";
      System.out.println("URL : "+url);
      String response = invokeUniRequest(url, payload, sign);
      try {
        JsonObject responseObj = JsonParser.parseString(response).getAsJsonObject();
        if (responseObj.get("Response") != null) {
          String encr = responseObj.getAsJsonObject("Response").getAsJsonObject("body").get("encryptData").toString();
          System.out.println("Response : "+responseObj.toString());
          System.out.println(decrypt(encr.replaceAll("\"", "")));
        } else {
          System.err.println(responseObj);
        }
      } catch (Exception ex) {
        System.out.println(response);
        ex.printStackTrace();
        ;
      }
  }
  public static void main(String[] args)
      throws NoSuchAlgorithmException, JoseException, InvalidKeySpecException, DecoderException, IOException {
        CanarabankLive client = new CanarabankLive();
    try {
//       client.getBalance();
//       client.impsTransfer("Sreemathi","015601564970","ICIC0000156","1","livetest");
//       client.impsStatus("PHED1724400235172");
//       client.getStatement("01-08-2024","23-08-2024");
//       client.neftTransfer("Sreemathi","015601564970","ICIC0000156","1","nefttest","23-08-2024");
//       client.neftStatus("PHED1724401223009");
//       client.internalTransfer("Sreemathi","110140395676","1","IFTTest","23-08-2024");
//       client.internalTransferStatus("PHED1724402899963");
//       client.rtgsTransfer("Phedora Technologies", "924020017800104", "UTIB0004575", "200000", "rtgsTest", "23-08-2024");
//       client.rtgsStatus("PHED1724405195621");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
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
    kong.unirest.HttpResponse<String> response = Unirest.post(url)
        .header("x-client-id", API_KEY)
        .header("x-client-secret", API_SECRET)
        .header("x-api-interaction-id", "fccfdade-2a4c-4616-b76e-4837f5ea4ae2")
        .header("x-timestamp", "1675780846")
        .header("x-client-certificate", CLIENT_PUBLIC_CERT)
        .header("x-signature", signature)
        // "Bo8bumksbX1oPK8ZuXQD98ilFN5Mr4okjDcipVADXntorqdPGvPRvHONbcIfjXZY3p6HLf+s8TuEPZmP4dLngM8zZ/FaAA7JP5Gzk7euTlte96gqSX/+UAGKA13DMxQJHDcARfA2zKMjSeZ2J4y1jGgjVyuYuOtj4I/56X2Ul/E0USe3tEAFfZfonGmWPrhk3BxGjKq8dR+6895yhSQsqUSARvsBmZm4ZYaMhXN81Ke/0g5Y5LepGrjitPb2CxUtESuRSz3NnrdXtw4ELKs7dOmi21BWdY90dVgh2CZWiOPZXHcI/PcSDftlv6Xndlxh3DG+qJuZu1AUvBTB3tM4fg==")
        .header("Content-Type", "application/json")
        .header("Cookie",
            "1122; TS01ea60e3=01aee67679c686b25f6fff23b7e82ee443fbcf7777927798d6167f014830f9fe80355ed560765eed2826bf9854c1f19b829f27dc2d; TS01ea60e3028=0192426cf8e858e9bfb109d227872f0e0517dd288d0a90c12f79c8fa30dbee07ff9189200d1d4ec8fcc47d9958f1016e268bb37d2f")
        .header("X-Forwarded-For", "weasdwd")
        .body(payload)
        .asString();
    return response.getBody();

  }

  public String sign(String input) {
    String realPK = CLIENT_PRIVATE_KEY.replaceAll("-----END PRIVATE KEY-----", "")
        .replaceAll("-----BEGIN PRIVATE KEY-----", "")
        .replaceAll("\n", "");
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

