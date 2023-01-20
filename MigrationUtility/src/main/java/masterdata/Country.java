package masterdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import utility.Utility;

public class Country extends RestExecution {

	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateCountry";

	public void createCountry(Map<String, String> map) {

		String apiURL = getAPIURL("country");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String apiBody = getCountryJson(map);
		Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

		JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		// Fetching the desired value of a parameter
		int status = JSONResponseBody.getInt("status");

		if (status == 200) {
			String message = "New Country is added successfully - " + map.get("Country");
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);

		} else if (status == 406) {
			String error = JSONResponseBody.getString("ERROR") + " - " + map.get("Country");
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createCountry(List<Map<String, String>> countryMapList) {

		for (int i = 0; i < countryMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = countryMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createCountry(map);
		}

	}

	public List<Map<String, String>> readCountryList() {

		String sheetName = "Country";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> countryMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String country = cellValue.get("Country");
			if ((!"".equals(country)) && (country != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("Country", cellValue.get("Country"));
				valuemap.put("Status", cellValue.get("Status"));
				countryMapList.add(valuemap);
			}
		}
		return countryMapList;
	}

	private String getCountryJson(Map<String, String> map) {

		String jsonString = null;

		try {
			JSONObject countryJsonObject = new JSONObject();

			countryJsonObject.put("name", map.get("Country"));
			countryJsonObject.put("status", map.get("Status"));
			countryJsonObject.put("delete", false);
			countryJsonObject.put("isDelete", false);
			
			jsonString = countryJsonObject.toString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}
		return jsonString;
	}
}