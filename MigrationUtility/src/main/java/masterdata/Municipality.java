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

public class Municipality extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateMunicipality";
	
	private void createMunicipality(String muncipilityName,int cityId,int stateId,int countryId){
		
		String apiURL=getAPIURL("pincode/save");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		//Initializing payload or API body		  
		 String apiBody = getMunicipalityJson(muncipilityName,cityId,stateId,countryId);
		 Utility.printLog(logFileName,logModuleName , "Request Body", apiBody);
		 
		 JSONObject JSONResponseBody = httpPost(apiURL,apiBody);
		 String response = JSONResponseBody.toString(4);
		 Utility.printLog(logFileName,logModuleName , "Response", response);

		 int status = JSONResponseBody.getInt("responseCode");

		 if(status==200) {
			String message = "New Municipality is added successfully - " + muncipilityName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + muncipilityName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}
	
	public void createMunicipality(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			String ans[] = value.split(":");

			String countryName = ans[0];
			String provinceName = ans[1];
			String districtName = ans[2];
			String municipalityName = ans[3];
			
			Province province = new Province();
			int countryId = province.getCountryId(countryName);			
			District district = new District(); 
			int provinceId = district.getProvinceId(provinceName);
			int districtId = getDistrictId(districtName);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", value);		
			createMunicipality(municipalityName,districtId,provinceId,countryId);
		}
	}

	public Map<String, String> readUniqueMunicipalityList() {
		
		String sheetName = "Geogaraphical Areas";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);
			if (!"".equals(cellValue.get("Municipalties"))) {
				
				String country = cellValue.get("Country");
				String province = cellValue.get("Province");
				String district = cellValue.get("District");
				String municipality = cellValue.get("Municipalties");
				
				String ans = country + ":" + province + ":" + district + ":" +  municipality;  
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getMunicipalityJson(String municipalityName,int cityId,int stateId,int countryId) {
		
		String jsonString = null; 
		
		try {
			
			org.json.simple.JSONObject municipalityJsonObject = new org.json.simple.JSONObject();
			
			//ReadData readData = new ReadData();			
			//municipalityJsonObject = readData.readJSONFile("CreateMunicipality.json");
			
			municipalityJsonObject.put("pincode", municipalityName);
			municipalityJsonObject.put("cityId", cityId);
			municipalityJsonObject.put("stateId", stateId);
			municipalityJsonObject.put("countryId", countryId);
			municipalityJsonObject.put("status", "Active");
			
			jsonString = municipalityJsonObject.toJSONString();
			
		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}
		
		return jsonString;
	}
	
	public int getDistrictId(String districtName) {

		String apiURL = getAPIURL("city/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("status");		
		int districtId=0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("cityList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedDistrictName = jsonArray.getJSONObject(i).getString("name");
				if(districtName.equalsIgnoreCase(receivedDistrictName)) {
					districtId = jsonArray.getJSONObject(i).getInt("id");
				}
			}
		}
			
		return districtId;
	}
}
