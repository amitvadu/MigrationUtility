package productdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import commons.CommonList;
import utility.DBOperations;
import utility.Utility;

public class PlanCharge extends RestExecution {

	private String logFileName = "prepaidplan.log";
	private String logModuleName = "CreateCharge";

	private void createPlanCharge(Map<String, String> charge) {

		String apiURL = getAPIURL("charge");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String apiBody = getPlanChargeJson(charge);
		Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

		JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("status");
		String chargeName = charge.get("Name");
		
		if (status == 200) {
			String message = "New Plan-Charge is added successfully - " + chargeName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("ERROR") + " - " + chargeName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		} else {
			String error = JSONResponseBody.get("ERROR") + " - " + chargeName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "ERROR", error);
		}
	}

	public void createPlanCharge(List<Map<String, String>> chargeMapList) {

		for (int i = 0; i < chargeMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = chargeMapList.get(i);
			createPlanCharge(map);
		}
	}

	public List<Map<String, String>> readUniquePlanChargeList() {

		String sheetName = "Charge";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getPlanDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> chargeMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			valuemap.put("Name", cellValue.get("Name"));
			valuemap.put("Category", cellValue.get("Category"));
			valuemap.put("Type", cellValue.get("Type"));
			valuemap.put("Service", cellValue.get("Service"));
			valuemap.put("Status", cellValue.get("Status"));
			valuemap.put("LedgerID", cellValue.get("LedgerID"));
			valuemap.put("Description", cellValue.get("Description"));
			valuemap.put("RoyaltyPayable", cellValue.get("RoyaltyPayable"));			
			valuemap.put("ActualPrice", cellValue.get("ActualPrice"));
			valuemap.put("SACCode", cellValue.get("SACCode"));
			valuemap.put("Tax", cellValue.get("Tax"));
			chargeMapList.add(valuemap);
		}
		return chargeMapList;
	}

	@SuppressWarnings("unchecked")
	private String getPlanChargeJson(Map<String, String> charge) {

		String jsonString = null;

		try {
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			CommonList commonList = new CommonList();
			
			JSONObject chargeJson = new JSONObject();

			chargeJson.put("name", charge.get("Name"));
			String commonChargeCategory = commonList.getCommonChargeCategory(charge.get("Category"));
			chargeJson.put("chargecategory", commonChargeCategory);

			String commonChargeType = commonList.getCommonChargeType(charge.get("Type"));
			chargeJson.put("chargetype", commonChargeType);
			
			chargeJson.put("serviceid", commonGetAPI.getServiceIdList(charge.get("Service")));
			chargeJson.put("status", charge.get("Status"));
			
			String ledgerId = charge.get("LedgerID");
			if ("".equals(ledgerId)) {
				ledgerId = null;
			}
			chargeJson.put("ledgerId", ledgerId);
			
			chargeJson.put("royalty_payable", Boolean.valueOf(charge.get("RoyaltyPayable")));
			chargeJson.put("desc", charge.get("Description"));
			
			float actualPrice = Float.valueOf(charge.get("ActualPrice"));

			chargeJson.put("actualprice", actualPrice);
			chargeJson.put("price", actualPrice);
			chargeJson.put("saccode", charge.get("SACCode"));
			chargeJson.put("taxid", commonGetAPI.getTaxId(charge.get("Tax")));
			
			String serviceNameList = null;
			chargeJson.put("serviceNameList", serviceNameList);
			
			jsonString = chargeJson.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

}
