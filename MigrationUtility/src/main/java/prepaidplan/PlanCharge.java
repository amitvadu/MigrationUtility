package prepaidplan;

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

	private void createPlanCharge(Map<String, String> chargeDetails) {

		String apiURL = getAPIURL("charge");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String apiBody = getPlanChargeJson(chargeDetails);
		Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

		JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		// Fetching the desired value of a parameter
		int status = JSONResponseBody.getInt("status");
		// System.out.println("status = " + result1);

		if (status == 200) {
			System.out.println("New Plan-Charge is added successfully - " + chargeDetails.get("Name"));

			String type = "Plan-Charge";
			JSONObject cityJSONObject = JSONResponseBody.getJSONObject("charge");
			String name = cityJSONObject.getString("name");
			int id = cityJSONObject.getInt("id");

			DBOperations dbo = new DBOperations();
			dbo.setAPIData(type, name, id, status);

		} else if (status == 406) {
			String error = JSONResponseBody.getString("ERROR");
			System.out.println(error + " - " + chargeDetails.get("Name"));
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
			valuemap.put("Description", cellValue.get("Description"));
			valuemap.put("ActualPrice", cellValue.get("ActualPrice"));
			valuemap.put("SACCode", cellValue.get("SACCode"));
			valuemap.put("Tax", cellValue.get("Tax"));
			chargeMapList.add(valuemap);
		}
		return chargeMapList;
	}

	@SuppressWarnings("unchecked")
	private String getPlanChargeJson(Map<String, String> chargeDetails) {

		String jsonString = "";

		try {
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			CommonList commonList = new CommonList();
			
			org.json.simple.JSONObject chargeJsonObject = null;

			ReadData readData = new ReadData();
			chargeJsonObject = readData.readJSONFile("CreatePlanCharge.json");

			chargeJsonObject.put("name", chargeDetails.get("Name"));

			String commonChargeCategory = commonList.getCommonChargeCategory(chargeDetails.get("Category"));
			chargeJsonObject.put("chargecategory", commonChargeCategory);

			String commonChargeType = commonList.getCommonChargeType(chargeDetails.get("Type"));
			chargeJsonObject.put("chargetype", commonChargeType);
			
			chargeJsonObject.put("desc", chargeDetails.get("Description"));

			float actualPrice = Float.valueOf(chargeDetails.get("ActualPrice"));

			chargeJsonObject.put("actualprice", actualPrice);
			chargeJsonObject.put("price", actualPrice);
			chargeJsonObject.put("saccode", chargeDetails.get("SACCode"));
			
			
			chargeJsonObject.put("serviceid", commonGetAPI.getServiceIdList(chargeDetails.get("Service")));
			chargeJsonObject.put("taxid", commonGetAPI.getTaxId(chargeDetails.get("Tax")));

			jsonString = chargeJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

}
