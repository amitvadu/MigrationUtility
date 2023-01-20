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

public class Region extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateRegion";

	private void createRegion(Map<String, String> region) {

		String apiURL = getAPIURL("region/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getRegionJson(region);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");
		String regionName = region.get("RegionName");
		if (status == 200) {
			String message = "New Region is added successfully - " + regionName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + regionName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createRegion(List<Map<String, String>> regionMapList) {
		
		for (int i = 0; i < regionMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = regionMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createRegion(map);
		}
	}

	public List<Map<String, String>> readRegionList() {
		
		String sheetName = "Region";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> regionMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String regionName = cellValue.get("RegionName");
			if ((!"".equals(regionName)) && (regionName != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("RegionName", cellValue.get("RegionName"));
				valuemap.put("Branch", cellValue.get("Branch"));
				valuemap.put("Status", cellValue.get("Status"));
				regionMapList.add(valuemap);
			}
		}
		return regionMapList;
	}

	private String getRegionJson(Map<String, String> region) {

		String jsonString = null;

		try {

			
			String id = null;
			JSONObject regionJsonObject = new JSONObject();

			regionJsonObject.put("rname", region.get("RegionName"));
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			regionJsonObject.put("branchid", commonGetAPI.getBranchIdList(region.get("Branch")));
			regionJsonObject.put("status", region.get("Status"));
			regionJsonObject.put("id", id);

			jsonString = regionJsonObject.toString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

}
