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

public class Branch extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateBranch";

	private void createBranch(String branchName, List<Integer> serviceAreaIdList) {

		String apiURL = getAPIURL("branchManagement/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getBranchJson(branchName, serviceAreaIdList);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");

		if (status == 200) {
			String message = "New Branch is added successfully - " + branchName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + branchName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
		
	}

	public void createBranch(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			String ans[] = value.split(":");

			String branchName = ans[0];
			String serviceAreaName = ans[1];

			List<Integer> serviceAreaIdList = new ArrayList<Integer>();
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			serviceAreaIdList.addAll(commonGetAPI.getServiceAreaIdList(serviceAreaName));
			createBranch(branchName, serviceAreaIdList);
		}
	}

	public Map<String, String> readUniqueBranchList() {
		
		String sheetName = "Geogaraphical Areas";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);
			if (!"".equals(cellValue.get("Branch"))) {
				
				String branch = cellValue.get("Branch");
				String serviceArea = cellValue.get("ServiceArea");
				String ans = branch + ":" + serviceArea;
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getBranchJson(String branchName, List<Integer> serviceAreaIdList) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject branchJsonObject = new org.json.simple.JSONObject();

			ReadData readData = new ReadData();
			branchJsonObject = readData.readJSONFile("CreateBranch.json");

			branchJsonObject.put("name", branchName);
			branchJsonObject.put("serviceAreaIdsList", serviceAreaIdList);
			
			branchJsonObject.put("status", "Active");
			branchJsonObject.put("branch_code", "");
			branchJsonObject.put("revenue_sharing", false);
			branchJsonObject.put("sharing_percentage", "");
			branchJsonObject.put("dunningDays", "");

			jsonString = branchJsonObject.toJSONString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

}
