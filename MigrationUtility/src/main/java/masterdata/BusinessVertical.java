package masterdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import utility.Utility;

public class BusinessVertical extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateBusinessVertical";

	private void createBusinessVertical(Map<String, String> businessVertical) {

		String apiURL = getAPIURL("businessverticals/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getBusinessVerticalJson(businessVertical);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");
		String businessVerticalName = businessVertical.get("BusinessVerticalName");
		
		if (status == 200) {
			String message = "New Business Verical is added successfully - " + businessVerticalName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + businessVerticalName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
		
	}

	public void createBusinessVertical(List<Map<String, String>> businessVerticalMapList) {

		for (int i = 0; i < businessVerticalMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = businessVerticalMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createBusinessVertical(map);
		}
	}

	public List<Map<String, String>> readBusinessVerticalList() {
		
		String sheetName = "BusinessVertical";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> businessVerticalMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String regionName = cellValue.get("BusinessVerticalName");
			if ((!"".equals(regionName)) && (regionName != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("BusinessVerticalName", cellValue.get("BusinessVerticalName"));
				valuemap.put("Region", cellValue.get("Region"));
				valuemap.put("Status", cellValue.get("Status"));
				businessVerticalMapList.add(valuemap);
			}
		}
		return businessVerticalMapList;
	}

	private String getBusinessVerticalJson(Map<String, String> businessVertical) {

		String jsonString = null;

		try {

			JSONObject businessVericalJsonObject = new JSONObject();
			
			String regionName = businessVertical.get("Region");
			String id = null;
			
			businessVericalJsonObject.put("vname", businessVertical.get("BusinessVerticalName"));
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			businessVericalJsonObject.put("region_id", commonGetAPI.getRegionIdList(regionName));
			businessVericalJsonObject.put("status", businessVertical.get("Status"));
			businessVericalJsonObject.put("id", id);

			jsonString = businessVericalJsonObject.toString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

}
