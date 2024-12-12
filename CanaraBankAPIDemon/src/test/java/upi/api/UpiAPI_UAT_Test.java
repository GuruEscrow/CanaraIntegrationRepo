package upi.api;

import org.testng.annotations.Test;

public class UpiAPI_UAT_Test {

//	This method is for VPA creation
	@Test
	public void vpaCreation() throws Exception {
		CanarabankUPI_UAT client = new CanarabankUPI_UAT();
		String accountNum = "04762020001837";
		String terminalID = "";
		String mobileNum = "8970486528";
		String sid = "test12";
		client.vpaCreation(accountNum,terminalID,mobileNum,sid);
	}
	
//	This method is for VPA Inquiry
	@Test
	public void vpaInquiry() throws Exception {
		CanarabankUPI_UAT client = new CanarabankUPI_UAT();
		String terminalID = "";
		String sid = "test11";
		String batchID = "API227027782963623";
		client.vpaInquiry(terminalID,sid,batchID);
	}
	
//	This method is for VPA Deactivation
	@Test
	public void vpaDeactivation() throws Exception {
		CanarabankUPI_UAT client = new CanarabankUPI_UAT();
		String upiID = "youtube.youtube001.test11@cnrf";
		String mid = "YOUTUBE001";
		String terminalID = "";
		String sid = "test11";
		client.vpaDeactivation(upiID, mid, terminalID, sid);
	}
	
//	This method is for VPA Deactivation Inquiry
	@Test
	public void vpaDeactivationInquiry() throws Exception {
		CanarabankUPI_UAT client = new CanarabankUPI_UAT();
		String mid = "YOUTUBE001";
		String terminalID = "";
		String sid = "test11";
		String batchID = "API227027782963623";
		client.vpaDeactivationEnquiry(mid, terminalID, sid, batchID);
	}
	
//	This method is for VPA Verification
	@Test
	public void vpaVerification() throws Exception {
		CanarabankUPI_UAT client = new CanarabankUPI_UAT();
		String mid = "YOUTUBE001";
		String extTxnID = "VPAVERIFICATION001";
		String upiID = "youtube.youtube001.test11@cnrf";
		String terminalID = "";
		String sid = "test11";
		client.verifyVPA(mid, extTxnID, upiID, terminalID, sid);
	}
	
//	This method is for raise Collection through UPI
	@Test
	public void raiseCollectThroughUPI() throws Exception {
		CanarabankUPI_UAT client = new CanarabankUPI_UAT();
		String mid = "YOUTUBE001";
		String terminalID = "";
		String amt = "1";
		String extTxnID = "UPICOLLECT002";
		String sid = "test11";
		String beneUPIID = "8970486528@ybl";
		String payeeUpi = "youtube.youtube001.test11@cnrf";
		client.raiseCollect_Thr_UPIID(mid, terminalID, amt, extTxnID, sid, beneUPIID, payeeUpi);
	}
	
//	This method is for UPI collection status check using TXN ID
	@Test
	public void upiCollectStatusCheckUsingTxnID() throws Exception {
		CanarabankUPI_UAT client = new CanarabankUPI_UAT();
		String mid = "YOUTUBE001";
		String sid = "test11";
		String terminalID = "";
		String txnID = "UPICOLLECT002";
		client.raiseCollect_TxnStatusUsingTxnID(mid, sid, terminalID, txnID);
	}
	
//	This method is for UPI collection status check using RRN
	@Test
	public void upiCollectStatusCheckUsingRRN() throws Exception {
		CanarabankUPI_UAT client = new CanarabankUPI_UAT();
		String mid = "YOUTUBE001";
		String sid = "test11";
		String terminalID = "";
		String rrn = "Txn001";
		client.raiseCollect_TxnStatusUsingRRN(mid, sid, terminalID, rrn);
	}
	
//	This method is for To raise the Collection through QR code
	@Test
	public void raiseCollectThroughQR() throws Exception {
		CanarabankUPI_UAT client = new CanarabankUPI_UAT();
		String amt = "50";
		String extTxnID = "QRCOLLECT002";
		String remark = "QR COLLECT UAT";
		String mid = "YOUTUBE001";
		String terminalID = "";
		String sid = "test11";
		String sourceUPI = "youtube.youtube001.test11@cnrf";
		client.qr_Generation_forCollect(amt, extTxnID, remark, mid, terminalID, sid,sourceUPI);
	}
	
//	This method is for QR code collection status check using TXN ID
	@Test
	public void qrCollectStatusCheckUsingTxnId() throws Exception {
		CanarabankUPI_UAT client = new CanarabankUPI_UAT();
		String mid = "YOUTUBE001";
		String sid = "test11";
		String terminalID = "";
		String txnID = "QRCOLLECT003";
		client.qr_GenerationCollect_status_ExtTXNID(mid, sid, terminalID, txnID);
	}
	
//	This method is for QR code Collection status check using RRN
	@Test
	public void qrCollectStatusCheckUsingRRN() throws Exception {
		CanarabankUPI_UAT client = new CanarabankUPI_UAT();
		String mid = "YOUTUBE001";
		String sid = "test001";
		String terminalID = "";
		String rrn = "Txn001";
		client.qr_GenerationCollect_status_RRN(mid, sid, terminalID, rrn);
	}
}
