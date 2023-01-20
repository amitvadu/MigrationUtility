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

public class Branch extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateBranch";

	private void createBranch(Map<String, String> branchDetails) {

		String apiURL = getAPIURL("branchManagement/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getBranchJson(branchDetails);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");
		String branchName = branchDetails.get("BranchName");
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

	public void createBranch(List<Map<String, String>> branchMapList) {
		
		for (int i = 0; i < branchMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = branchMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createBranch(map);
		}
	}

	public List<Map<String, String>> readBranchList() {
		
		String sheetName = "Branch";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> branchMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String branchName = cellValue.get("BranchName");
			if ((!"".equals(branchName)) && (branchName != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("BranchName", cellValue.get("BranchName"));
				valuemap.put("BranchCode", cellValue.get("BranchCode"));
				valuemap.put("ServiceArea", cellValue.get("ServiceArea"));
				valuemap.put("RevenueSharing", cellValue.get("RevenueSharing"));
				valuemap.put("SharingPercentage", cellValue.get("SharingPercentage"));
				valuemap.put("DunningDay", cellValue.get("DunningDay"));
				valuemap.put("Status", cellValue.get("Status"));
				branchMapList.add(valuemap);
			}
		}
		return branchMapList;
	}

	private String getBranchJson(Map<String, String> branchDetails) {

		String jsonString = null;

		try {
			
			JSONObject branchJsonObject = new JSONObject();
			
			String branchName = branchDetails.get("BranchName");
			String serviceAreaName = branchDetails.get("ServiceArea");
			
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			List<Integer> serviceAreaIdList = commonGetAPI.getServiceAreaIdList(serviceAreaName);

			branchJsonObject.put("name", branchName);
			branchJsonObject.put("branch_code", branchDetails.get("BranchCode"));
			branchJsonObject.put("serviceAreaIdsList", serviceAreaIdList);
			
			boolean revenueSharing = Boolean.valueOf(branchDetails.get("RevenueSharing"));
			branchJsonObject.put("revenue_sharing",revenueSharing );
			
			if(revenueSharing) {
				String tempSharingPercentage = branchDetails.get("SharingPercentage");
				int sharingPercentage = Integer.valueOf(tempSharingPercentage);
				branchJsonObject.put("sharing_percentage", sharingPercentage);
			}else {
				branchJsonObject.put("sharing_percentage", "");
			}
			
			branchJsonObject.put("dunningDays", branchDetails.get("DunningDay"));
			branchJsonObject.put("status", branchDetails.get("Status"));
			
			jsonString = branchJsonObject.toString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

}
