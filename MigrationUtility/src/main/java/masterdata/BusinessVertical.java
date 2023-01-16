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

public class BusinessVertical extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateBusinessVertical";

	private void createBusinessVertical(String businessVertical,String region) {

		String apiURL = getAPIURL("businessverticals/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getBusinessVerticalJson(businessVertical,region);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");

		if (status == 200) {
			String message = "New Business Verical is added successfully - " + businessVertical;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + businessVertical;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
		
	}

	public void createBusinessVertical(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			String ans[] = value.split(":");

			String businessVertical = ans[0];
			String region = ans[1];
			Utility.printLog(logFileName, logModuleName, "Sheet Data", value);
			createBusinessVertical(businessVertical,region);
		}
	}

	public Map<String, String> readUniqueBusinessVerticalList() {
		
		String sheetName = "Geogaraphical Areas";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);
			if (!"".equals(cellValue.get("BusinessVertical"))) {
				
				String businessVertical = cellValue.get("BusinessVertical");
				String region = cellValue.get("Region");
				String ans = businessVertical + ":" + region;
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getBusinessVerticalJson(String businessVertical,String regionName) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject businessVericalJsonObject = new org.json.simple.JSONObject();

			businessVericalJsonObject.put("vname", businessVertical);
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			businessVericalJsonObject.put("region_id", commonGetAPI.getRegionIdList(regionName));
			businessVericalJsonObject.put("status", "Active");
			businessVericalJsonObject.put("id", null);

			jsonString = businessVericalJsonObject.toJSONString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

}
