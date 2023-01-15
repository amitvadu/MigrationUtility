package staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import utility.Utility;

public class Staff extends RestExecution {

	private static String logFileName = "Staff.log";
	private static String logModuleName = "CreateStaff";

	private void createStaff(Map<String, String> staffDetails) {

		String apiURL = getAPIURL("staffuser");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getStaffJson(staffDetails);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("status");

		if (status == 200) {
			
			String message = "New Staff is added successfully - " + staffDetails.get("UsrerName");
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + staffDetails.get("UsrerName");
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createStaff(List<Map<String, String>> staffMapList) {

		for (int i = 0; i < staffMapList.size(); i++) {
			
			Map<String, String> map = new HashMap<String, String>();
			map = staffMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createStaff(map);
		}
	}

	public List<Map<String, String>> readUniqueStaffList() {
		
		
		String sheetName = "Staff";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> staffMapList = new ArrayList<Map<String, String>>();
		
		
		for (int i = 0; i < sheetMap.size(); i++) {
			
			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);
			
			if (!"".equals(cellValue.get("Usrername"))) {
				
				valuemap.put("UsrerName", cellValue.get("Username"));
				valuemap.put("Password", cellValue.get("Password"));
				valuemap.put("Email", cellValue.get("Email"));
				valuemap.put("FirstName", cellValue.get("FirstName"));
				valuemap.put("LastName", cellValue.get("LastName"));
				valuemap.put("Status", cellValue.get("Status"));
				valuemap.put("CountryCode", cellValue.get("CountryCode"));
				valuemap.put("Mobile", cellValue.get("Mobile"));
				valuemap.put("Roles", cellValue.get("Roles"));			
				valuemap.put("Teams", cellValue.get("Teams"));
				valuemap.put("ServiceArea", cellValue.get("ServiceArea"));
				valuemap.put("BusinessUnit", cellValue.get("BusinessUnit"));			
				valuemap.put("Partner", cellValue.get("Partner"));
				valuemap.put("Branch", cellValue.get("Branch"));
				
				staffMapList.add(valuemap);

			}
		}
		return staffMapList;
	}

	@SuppressWarnings("unchecked")
	private String getStaffJson(Map<String, String> staffDetails) {

		String jsonString = null;

		try {
			
			CommonGetAPI commonGetAPI = new CommonGetAPI();			
			org.json.simple.JSONObject staffJsonObject = new org.json.simple.JSONObject();
			
			//ReadData readData = new ReadData();
			//staffJsonObject = readData.readJSONFile("CreateStaff.json");
			
			staffJsonObject.put("username", staffDetails.get("UsrerName"));
			staffJsonObject.put("password", staffDetails.get("Password"));
			staffJsonObject.put("email", staffDetails.get("Email"));
			
			staffJsonObject.put("firstname", staffDetails.get("FirstName"));
			staffJsonObject.put("lastname", staffDetails.get("LastName"));			
			staffJsonObject.put("status", staffDetails.get("Status").toUpperCase());
			
			staffJsonObject.put("countryCode", "+" + staffDetails.get("CountryCode"));
			staffJsonObject.put("phone", staffDetails.get("Mobile"));
			staffJsonObject.put("roleIds", getRoleId(staffDetails.get("Roles")));
			staffJsonObject.put("teamIds",getTeamId(staffDetails.get("Teams")));
			
			staffJsonObject.put("serviceAreaIdsList",commonGetAPI.getServiceAreaIdList(staffDetails.get("ServiceArea")));
			staffJsonObject.put("businessUnitIdsList",commonGetAPI.getBusinessUnitIdList(staffDetails.get("BusinessUnit")));
			staffJsonObject.put("partnerid", commonGetAPI.getPartnerId(staffDetails.get("Partner")));
			
			staffJsonObject.put("branchId", null);
			
			String branch = staffDetails.get("Branch");
			if(!"".equals(branch)) {
				staffJsonObject.put("branchId", commonGetAPI.getBranchId(staffDetails.get("Branch")));
			}
			
			staffJsonObject.put("parentStaffId", 2);
			staffJsonObject.put("mvnoid", null);			
			staffJsonObject.put("staffUserServiceMappingList", null);
			
			jsonString = staffJsonObject.toJSONString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

	
	private List<Integer> getTeamId(String teamName) {

		String apiURL = getAPIURL( "teams/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		
		List<Integer> list = new ArrayList<Integer>();

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedTeamName = jsonArray.getJSONObject(i).getString("name");
				if(teamName.equalsIgnoreCase(receivedTeamName)) {
					list.add(jsonArray.getJSONObject(i).getInt("id"));
				}
			}
		}
		
		return list;
	}
	
	private List<Integer> getRoleId(String teamName) {

		String apiURL = getAPIURL( "role/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		
		List<Integer> list = new ArrayList<Integer>();

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedTeamName = jsonArray.getJSONObject(i).getString("rolename");
				if(teamName.equalsIgnoreCase(receivedTeamName)) {
					list.add(jsonArray.getJSONObject(i).getInt("id"));
				}
			}
		}
			
		return list;
	}
	
}


