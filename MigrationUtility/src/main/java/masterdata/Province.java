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

public class Province extends RestExecution{
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateProvince";
	
	public void createProvince(String provinceName,String countryName,int countryId){
		
		String apiURL=getAPIURL("state");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		 
		//Initializing payload or API body		  
		String apiBody = getProvinceJson(provinceName,countryName,countryId);
		Utility.printLog(logFileName,logModuleName , "Request Body", apiBody);
		 
		JSONObject JSONResponseBody = httpPost(apiURL,apiBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName,logModuleName , "Response", response); 

		 int status = JSONResponseBody.getInt("status");

		 if(status==200) {
			String message = "New Province is added successfully - " + provinceName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("ERROR") + " - " + provinceName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		 }
	}

	
	public void createProvince(Map <String, String> map) {
		
		Set<String> keys = map.keySet(); 
	    Iterator<String> keyIter = keys.iterator();

	    while (keyIter.hasNext()) {
	        String key = keyIter.next();
	        String value = map.get(key);
	        String ans[] = value.split(":");	 
	        
	        String countryName = ans[0];
	        String provinceName = ans[1];
	        
	        int id = getCountryId(countryName);
	        Utility.printLog(logFileName, logModuleName, "Sheet Data", value);
	        createProvince(provinceName,countryName,id);
	    }		
	}
	
	
	public Map <String, String> readUniqueProvisionList() {
		
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
				String ans = country + ":" + province;			
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}
	
	@SuppressWarnings("unchecked")
	private String getProvinceJson(String provinceName,String countryName,int countryId) {
		
		String jsonString = null; 
		
		try {
			
			org.json.simple.JSONObject statePojoJsonObject = new org.json.simple.JSONObject();
			org.json.simple.JSONObject countryPojoJsonObject = new org.json.simple.JSONObject();
			
			statePojoJsonObject.put("name", provinceName);
			statePojoJsonObject.put("status", "Active");
			
			countryPojoJsonObject.put("name", countryName);
			countryPojoJsonObject.put("id", countryId);
			countryPojoJsonObject.put("status", "Active");
			statePojoJsonObject.put("countryPojo", countryPojoJsonObject);
			
			jsonString = statePojoJsonObject.toJSONString();
			
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
		int countryId=0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("countryList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedCountryName = jsonArray.getJSONObject(i).getString("name");
				if(countryName.equalsIgnoreCase(receivedCountryName)) {
					countryId = jsonArray.getJSONObject(i).getInt("id");
				}
			}
		}
			
		return countryId;
	}
}