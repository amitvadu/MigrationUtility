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

public class District extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateDistrict";

	private void createDistrict(Map<String, String> district) {

		String apiURL = getAPIURL("city");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getDistrictJson(district);
		Utility.printLog(logFileName,logModuleName , "Request Body", APIBody);
		
		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName,logModuleName , "Response", response);
		
		// Fetching the desired value of a parameter
		int status = JSONResponseBody.getInt("status");

		if (status == 200) {
			String message = "New District is added successfully - " + district.get("District");
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("ERROR") + " - " + district.get("District");
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);

		}
	}

	public void createDistrict(List<Map<String, String>> districtMapList) {
		
		for (int i = 0; i < districtMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = districtMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createDistrict(map);
		}
	}

	public List<Map<String, String>> readDistrictList() {
		
		String sheetName = "District";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> districtMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String district = cellValue.get("District");
			if ((!"".equals(district)) && (district != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("District", cellValue.get("District"));
				valuemap.put("Province", cellValue.get("Province"));
				valuemap.put("Country", cellValue.get("Country"));
				valuemap.put("Status", cellValue.get("Status"));
				districtMapList.add(valuemap);
			}
		}
		return districtMapList;
	}

	private String getDistrictJson(Map<String, String> district) {

		String jsonString = null;

		try {
			
			String provinceName = district.get("Province");
			int provinceId = getProvinceId(provinceName);
						
			Province province = new Province();
			String countryName = district.get("Country");
			int countryId = province.getCountryId(countryName);	
			
			JSONObject districtJsonObject = new JSONObject();
			JSONObject statePojoJsonObject = new JSONObject();
			JSONObject countryPojoJsonObject = new JSONObject();

			districtJsonObject.put("name", district.get("District"));
			districtJsonObject.put("countryName", countryName);
			districtJsonObject.put("countryId", countryId);
			districtJsonObject.put("status", district.get("Status"));

			statePojoJsonObject.put("name", provinceName);
			statePojoJsonObject.put("id", provinceId);
			statePojoJsonObject.put("status", "Active");

			countryPojoJsonObject.put("name", countryName);
			countryPojoJsonObject.put("id", countryId);
			countryPojoJsonObject.put("status", "Active");
			
			statePojoJsonObject.put("countryPojo", countryPojoJsonObject);
			districtJsonObject.put("statePojo", statePojoJsonObject);
			
			jsonString = districtJsonObject.toString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}
	
	public int getProvinceId(String provinceName) {

		String apiURL = getAPIURL("state/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("status");		
		int provinceId=0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("stateList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedProvinceName = jsonArray.getJSONObject(i).getString("name");
				if(provinceName.equalsIgnoreCase(receivedProvinceName)) {
					provinceId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}
			
		return provinceId;
	}
}
