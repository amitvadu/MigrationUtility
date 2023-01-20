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

public class Ward extends RestExecution {

	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateWard";
	
	private void createWard(Map<String, String> ward) {
		
		String apiURL = getAPIURL("area/save");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		// Initializing payload or API body
		String APIBody = getWardJson(ward);
		Utility.printLog(logFileName,logModuleName , "Request Body", APIBody);
		
		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName,logModuleName , "Response", response);
		
		int status = JSONResponseBody.getInt("responseCode");
		String wardName = ward.get("WardName");
		String municipalityName = ward.get("Municipalties");
				
		if (status == 200) {
			String message = "New Ward is added successfully - " + wardName + " (" + municipalityName + ")";
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + wardName + " (" + municipalityName + ")";
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createWard(List<Map<String, String>> wardMapList) {
		
		for (int i = 0; i < wardMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = wardMapList.get(i);
			
			int totalWard = Integer.parseInt(map.get("TotalWard"));			
			for(int j=1;j<=totalWard;j++) {
				map.put("WardName", String.valueOf(j));
				Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
				createWard(map);
			}
		}
	}

	public List<Map<String, String>> readWardList() {
		
		String sheetName = "Ward";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> wardMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String ward = cellValue.get("TotalWard");
			if ((!"".equals(ward)) && (ward != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("TotalWard", cellValue.get("TotalWard"));
				valuemap.put("Municipalties", cellValue.get("Municipalties"));
				valuemap.put("Status", cellValue.get("Status"));
				wardMapList.add(valuemap);
			}
		}
		return wardMapList;
	}

	private String getWardJson(Map<String, String> ward) {

		String jsonString = null;

		try {

			JSONObject wardJsonObject = new JSONObject();
			JSONObject pincodeJsonObject = new JSONObject();
			
			String wardName = ward.get("WardName");
			String status = ward.get("Status");
			
			String municipalityName = ward.get("Municipalties");
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			int pincodeId = commonGetAPI.getMunicipalityId(municipalityName);			
			String temp = commonGetAPI.getMasterDetailsFromMunicipalityId(pincodeId);
			
			String data[] = temp.split(":");
			int countryId = Integer.parseInt(data[0]);
			int stateId = Integer.parseInt(data[1]);
			int cityId = Integer.parseInt(data[2]);
			
			wardJsonObject.put("name", wardName);
			wardJsonObject.put("pincodeId", pincodeId);
			wardJsonObject.put("cityId", cityId);
			wardJsonObject.put("stateId", stateId);
			wardJsonObject.put("countryId", countryId);
			wardJsonObject.put("status", "Active");
			
			pincodeJsonObject.put("pincodeid", pincodeId);
			pincodeJsonObject.put("pincode", municipalityName);
			pincodeJsonObject.put("status", status);
			pincodeJsonObject.put("isDeleted", false);
			wardJsonObject.put("pincode", pincodeJsonObject);
			
			jsonString = wardJsonObject.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	
	
	

}
