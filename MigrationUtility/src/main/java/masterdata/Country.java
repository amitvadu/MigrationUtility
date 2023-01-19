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
import utility.Utility;

public class Country extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateCountry";
	
	public void createCountry(String countryName) {
		
		String apiURL = getAPIURL("country");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		// Initializing payload or API body
		String apiBody =  getCountryJson(countryName);
		Utility.printLog(logFileName,logModuleName , "Request Body", apiBody);
		
		JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName,logModuleName , "Response", response);
		
		// Fetching the desired value of a parameter
		int status = JSONResponseBody.getInt("status");

		if (status == 200) {
			String message = "New Country is added successfully - " + countryName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("ERROR") + " - " + countryName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createCountry(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String countryName = map.get(key);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createCountry(countryName);
		}
	}

	public Map<String, String> readUniqueCountryList() {
		
		String sheetName = "Country";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);
			if (!"".equals(cellValue.get("Country"))) {
				
				String ans = cellValue.get("Country");
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}
	
	
	@SuppressWarnings("unchecked")
	private String getCountryJson(String countryName) {
		
		String jsonString = null; 
		
		try {
			
			org.json.simple.JSONObject countryJsonObject = new org.json.simple.JSONObject();;
			
			countryJsonObject.put("name", countryName);
			countryJsonObject.put("status", "Active");
			countryJsonObject.put("delete", false);
			countryJsonObject.put("isDelete", false);
			
			jsonString = countryJsonObject.toJSONString();
			
		} catch (Exception e) {
			jsonString=null;
			e.printStackTrace();
		}
		return jsonString;
	}
	
}