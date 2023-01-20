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

public class Municipality extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateMunicipality";
	
	private void createMunicipality(Map<String, String> muncipality){
		
		String apiURL=getAPIURL("pincode/save");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		//Initializing payload or API body		  
		 String apiBody = getMunicipalityJson(muncipality);
		 Utility.printLog(logFileName,logModuleName , "Request Body", apiBody);
		 
		 JSONObject JSONResponseBody = httpPost(apiURL,apiBody);
		 String response = JSONResponseBody.toString(4);
		 Utility.printLog(logFileName,logModuleName , "Response", response);

		 int status = JSONResponseBody.getInt("responseCode");

		 if(status==200) {
			String message = "New Municipality is added successfully - " + muncipality.get("Municipalties");
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + muncipality.get("Municipalties");
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}
	
	public void createMunicipality(List<Map<String, String>> muncipalitiesMapList) {

		for (int i = 0; i < muncipalitiesMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = muncipalitiesMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createMunicipality(map);
		}
	}

	public List<Map<String, String>> readMunicipalityList() {
		
		String sheetName = "Municipalties";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> muncipalitiesMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String municipaltiy = cellValue.get("Municipalties");
			if ((!"".equals(municipaltiy)) && (municipaltiy != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("Municipalties", cellValue.get("Municipalties"));
				valuemap.put("District", cellValue.get("District"));
				valuemap.put("Province", cellValue.get("Province"));
				valuemap.put("Country", cellValue.get("Country"));
				valuemap.put("Status", cellValue.get("Status"));
				muncipalitiesMapList.add(valuemap);
			}
		}
		return muncipalitiesMapList;
	}

	private String getMunicipalityJson(Map<String, String> muncipality) {
		
		String jsonString = null; 
		
		try {
			
			JSONObject municipalityJsonObject = new JSONObject();
			
			int districtId = getDistrictId(muncipality.get("District"));
			
			District district = new District(); 
			int provinceId = district.getProvinceId(muncipality.get("Province"));
			
			Province province = new Province();
			int countryId = province.getCountryId(muncipality.get("Country"));			
			
			municipalityJsonObject.put("pincode", muncipality.get("Municipalties"));
			municipalityJsonObject.put("cityId", districtId);
			municipalityJsonObject.put("stateId", provinceId);
			municipalityJsonObject.put("countryId", countryId);
			municipalityJsonObject.put("status", "Active");
			
			jsonString = municipalityJsonObject.toString();
			
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
					break;
				}
			}
		}
			
		return districtId;
	}
}
