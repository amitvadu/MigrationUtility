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

public class Region extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateRegion";

	private void createRegion(String regionName,String branchName) {

		String apiURL = getAPIURL("region/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getRegionJson(regionName,branchName);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");

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

	public void createRegion(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			String ans[] = value.split(":");

			String regionName = ans[0];
			String branchName = ans[1];
			Utility.printLog(logFileName, logModuleName, "Sheet Data", value);
			createRegion(regionName,branchName);
		}
	}

	public Map<String, String> readUniqueRegionList() {
		
		String sheetName = "Geogaraphical Areas";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);
			if (!"".equals(cellValue.get("Region"))) {
				
				String region = cellValue.get("Region");
				String branch = cellValue.get("Branch");
				String ans = region + ":" + branch;
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getRegionJson(String regionName,String branchName) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject regionJsonObject = new org.json.simple.JSONObject();

			regionJsonObject.put("rname", regionName);
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			regionJsonObject.put("branchid", commonGetAPI.getBranchIdList(branchName));
			regionJsonObject.put("status", "Active");
			regionJsonObject.put("id", null);

			jsonString = regionJsonObject.toJSONString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

}
