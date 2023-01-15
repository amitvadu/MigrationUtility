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

public class Ward extends RestExecution {

	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateWard";
	
	private void createWard(String wardName, String municipalityName, int pincodeId,int cityId,int stateId,int countryId) {
		
		String apiURL = getAPIURL("area/save");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		// Initializing payload or API body
		String APIBody = getWardJson(wardName,municipalityName,pincodeId,cityId,stateId,countryId);
		Utility.printLog(logFileName,logModuleName , "Request Body", APIBody);
		
		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName,logModuleName , "Response", response);
		
		int status = JSONResponseBody.getInt("responseCode");

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

	public void createWard(Map<String, String> map) {
		
		CommonGetAPI commonGetAPI = new CommonGetAPI();
		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			String ans[] = value.split(":");

			int totalWard = Integer.parseInt(ans[0]);
			String municipalityName = ans[1];
			
			int municipalityId = commonGetAPI.getMunicipalityId(municipalityName);			
			String temp = commonGetAPI.getMasterDetailsFromMunicipalityId(municipalityId);
			
			String data[] = temp.split(":");
			int countryId = Integer.parseInt(data[0]);
			int stateId = Integer.parseInt(data[1]);
			int cityId = Integer.parseInt(data[2]);
			
			for(int i=1;i<=totalWard;i++) {
				String wardName=String.valueOf(i);
				Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());	
				createWard(wardName,municipalityName,municipalityId,cityId,stateId,countryId);
			}
		}
	}

	public Map<String, String> readUniqueWardList() {
		
		String sheetName = "Geogaraphical Areas";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);
			if (!"".equals(cellValue.get("Total Ward"))) {
				
				String ward = cellValue.get("Total Ward");
				String municipality = cellValue.get("Municipalties");
				
				String ans = ward + ":" + municipality;
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getWardJson(String wardName, String municipalityName, int pincodeId,int cityId,int stateId,int countryId) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject wardJsonObject = new org.json.simple.JSONObject();
			org.json.simple.JSONObject pincodeJsonObject = new org.json.simple.JSONObject();

			wardJsonObject.put("name", wardName);
			wardJsonObject.put("pincodeId", pincodeId);
			wardJsonObject.put("cityId", cityId);
			wardJsonObject.put("stateId", stateId);
			wardJsonObject.put("countryId", countryId);
			wardJsonObject.put("status", "Active");
			
			pincodeJsonObject.put("pincodeid", pincodeId);
			pincodeJsonObject.put("pincode", municipalityName);
			pincodeJsonObject.put("status", "Active");
			pincodeJsonObject.put("isDeleted", false);
			wardJsonObject.put("pincode", pincodeJsonObject);
			
			jsonString = wardJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	
	
	

}
