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

public class BusinessUnit extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateBusinessUnit";
	
	private void createBusinessUnit(String businessUnitName){
		
		String apiURL=getAPIURL("businessUnit/save");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		//Initializing payload or API body		  
		 String APIBody = getBusinessUnitJson(businessUnitName);
		 Utility.printLog(logFileName,logModuleName , "Request Body", APIBody);
		
		 JSONObject JSONResponseBody = httpPost(apiURL,APIBody);
		 String response = JSONResponseBody.toString(4);
		 Utility.printLog(logFileName,logModuleName , "Response", response);
		 		 
		 int status = JSONResponseBody.getInt("responseCode");

		 if(status==200) {
			String message = "New BusinessUnit is added successfully - " + businessUnitName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + businessUnitName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}
	
	public void createBusinessUnit(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			String ans[] = value.split(":");

			String businessUnit = ans[0];						
			createBusinessUnit(businessUnit);
		}
	}

	public Map<String, String> readUniqueBusinessUnitList() {

		String sheetName = "Geogaraphical Areas";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);
			if (!"".equals(cellValue.get("BusinessUnit"))) {
				
				String businessUnit = cellValue.get("BusinessUnit");
				String ans = businessUnit;
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getBusinessUnitJson(String businessUnitName) {
		
		String jsonString = null;
		
		try {
			
			org.json.simple.JSONObject districtJsonObject = new org.json.simple.JSONObject();
			
			ReadData readData = new ReadData();			
			districtJsonObject = readData.readJSONFile("CreateBusinessUnit.json");
			
			districtJsonObject.put("buname", businessUnitName);
			districtJsonObject.put("bucode", businessUnitName);
			districtJsonObject.put("status", "Active");
			
			jsonString = districtJsonObject.toJSONString();
			
		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}
		
		return jsonString;
	}
	

}
