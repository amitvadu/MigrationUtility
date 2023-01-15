package SalesCRM;

import java.util.HashMap;
import java.util.Map;

public class CreateSalesCRMData {

	
	private void createLeadSourceMaster() {

		LeadSourceMaster leadSourceMaster = new LeadSourceMaster();
		Map<String, String> map = new HashMap<String, String>();
		map = leadSourceMaster.readUniqueLeadSourceMasterList();
		leadSourceMaster.createLeadSourceMaster(map);
	}

	private void createRejectedReasonMaster() {

		RejectedReasonMaster rejectedReasonMaster = new RejectedReasonMaster();
		Map<String, String> map = new HashMap<String, String>();
		map = rejectedReasonMaster.readUniqueRejectedReasonList();
		rejectedReasonMaster.createRejectedReason(map);
	}
	
	public void generateSalesCRMData() {
		System.out.println("Started Generting SalesCRM Data...!");

		createLeadSourceMaster();
		createRejectedReasonMaster();
		System.out.println("Ended Generting SalesCRM Data...!");
	}

}
