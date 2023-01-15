package masterdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import utility.Utility;

public class District extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateDistrict";

	private void createDistrict(String districtName, String provinceName, int provinceId, String countryName,
			int countryId) {

		String apiURL = getAPIURL("city");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getDistrictJson(districtName, provinceName, provinceId, countryName, countryId);
		Utility.printLog(logFileName,logModuleName , "Request Body", APIBody);
		
		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName,logModuleName , "Response", response);
		
		// Fetching the desired value of a parameter
		int status = JSONResponseBody.getInt("status");

		if (status == 200) {
			String message = "New District is added successfully - " + districtName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("ERROR") + " - " + districtName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);

		}
	}

	public void createDistrict(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			String ans[] = value.split(":");

			String countryName = ans[0];
			String provinceName = ans[1];
			String districtName = ans[2];
			
			Province province = new Province();
			int countryId = province.getCountryId(countryName);			
			int provinceId = getProvinceId(provinceName);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createDistrict(districtName, provinceName, provinceId, countryName, countryId);
		}
	}

	public Map<String, String> readUniqueDistrictList() {
		
		String sheetName = "Geogaraphical Areas";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);
			if (!"".equals(cellValue.get("Province"))) {
				
				String country = cellValue.get("Country");
				String province = cellValue.get("Province");
				String district = cellValue.get("District");
				String ans = country + ":" + province + ":" + district;
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getDistrictJson(String districtName, String provinceName, int provinceId, String countryName,
			int countryId) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject districtJsonObject = new org.json.simple.JSONObject();
			org.json.simple.JSONObject statePojoJsonObject = new org.json.simple.JSONObject();
			org.json.simple.JSONObject countryPojoJsonObject = new org.json.simple.JSONObject();

			districtJsonObject.put("name", districtName);
			districtJsonObject.put("countryName", countryName);
			districtJsonObject.put("countryId", countryId);
			districtJsonObject.put("status", "Active");

			statePojoJsonObject.put("name", provinceName);
			statePojoJsonObject.put("id", provinceId);
			statePojoJsonObject.put("status", "Active");

			countryPojoJsonObject.put("name", countryName);
			countryPojoJsonObject.put("id", countryId);
			countryPojoJsonObject.put("status", "Active");
			
			statePojoJsonObject.put("countryPojo", countryPojoJsonObject);
			districtJsonObject.put("statePojo", statePojoJsonObject);
			
			jsonString = districtJsonObject.toJSONString();

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
				}
			}
		}
			
		return provinceId;
	}
}
