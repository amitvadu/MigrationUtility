package masterdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import utility.Utility;

public class SubBusinessUnit extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateSubBusinessUnit";

	private void createSubBusinessUnit(String subBusinessUnit,String businessUnit) {

		String apiURL = getAPIURL("subbusinessunit/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getSubBusinessUnitJson(subBusinessUnit,businessUnit);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");

		if (status == 200) {
			String message = "New Sub Business Unit is added successfully - " + subBusinessUnit;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + subBusinessUnit;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
		
	}

	public void createSubBusinessUnit(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			String ans[] = value.split(":");

			String subBusinessUnit = ans[0];
			String businessUnit = ans[1];

			createSubBusinessUnit(subBusinessUnit,businessUnit);
		}
	}

	public Map<String, String> readUniqueSubBusinessUnitList() {
		
		String sheetName = "Geogaraphical Areas";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);
			if (!"".equals(cellValue.get("SubBusinessUnit"))) {
				
				String subBusinessUnit = cellValue.get("SubBusinessUnit");
				String businessUnit = cellValue.get("BusinessUnit");
				String ans = subBusinessUnit + ":" + businessUnit;
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getSubBusinessUnitJson(String subBusinessUnit,String businessUnit) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject businessVericalJsonObject = new org.json.simple.JSONObject();

			businessVericalJsonObject.put("subbuname", subBusinessUnit);
			businessVericalJsonObject.put("subbucode", subBusinessUnit);
			
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			businessVericalJsonObject.put("businessunitid", commonGetAPI.getBusinessUnitIdList(businessUnit).get(0));
			businessVericalJsonObject.put("status", "Active");

			jsonString = businessVericalJsonObject.toJSONString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

}
