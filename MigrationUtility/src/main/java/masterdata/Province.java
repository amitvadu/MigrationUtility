package masterdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import utility.Utility;

public class Province extends RestExecution {

	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateProvince";

	public void createProvince(Map<String, String> provinceDetails) {

		String apiURL = getAPIURL("state");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String apiBody = getProvinceJson(provinceDetails);
		Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

		JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("status");

		if (status == 200) {
			String message = "New Province is added successfully - " + provinceDetails.get("Province");
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);

		} else if (status == 406) {
			String error = JSONResponseBody.getString("ERROR") + " - " + provinceDetails.get("Province");
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createProvince(List<Map<String, String>> provinceMapList) {

		for (int i = 0; i < provinceMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = provinceMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createProvince(map);
		}
	}

	public List<Map<String, String>> readProvinceList() {

		String sheetName = "Province";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> provinceMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String province = cellValue.get("Province");
			if ((!"".equals(province)) && (province != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("Province", cellValue.get("Province"));
				valuemap.put("Country", cellValue.get("Country"));
				valuemap.put("Status", cellValue.get("Status"));
				provinceMapList.add(valuemap);
			}
		}
		return provinceMapList;
	}

	private String getProvinceJson(Map<String, String> provinceDetails) {

		String jsonString = null;

		try {

			JSONObject statePojoJsonObject = new JSONObject();
			JSONObject countryPojoJsonObject = new JSONObject();

			String provinceName = provinceDetails.get("Province");
			String countryName = provinceDetails.get("Country");
			int countryId = getCountryId(countryName);

			statePojoJsonObject.put("name", provinceName);			
			statePojoJsonObject.put("status",  provinceDetails.get("Status"));

			countryPojoJsonObject.put("name", countryName);
			countryPojoJsonObject.put("id", countryId);
			countryPojoJsonObject.put("status", "Active");
			statePojoJsonObject.put("countryPojo", countryPojoJsonObject);
			
			jsonString = statePojoJsonObject.toString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}
		return jsonString;
	}

	public int getCountryId(String countryName) {

		String apiURL = getAPIURL("country/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("status");
		int countryId = 0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("countryList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedCountryName = jsonArray.getJSONObject(i).getString("name");
				if (countryName.equalsIgnoreCase(receivedCountryName)) {
					countryId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}

		if (countryId == 0) {
			System.out.println("Country details not found - " + countryId);
			Utility.printLog(logFileName, logModuleName, "Country details not found - ", "" + countryId);
		}
		return countryId;
	}
}