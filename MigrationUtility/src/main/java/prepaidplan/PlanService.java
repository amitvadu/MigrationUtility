package prepaidplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import utility.DBOperations;
import utility.Utility;

public class PlanService extends RestExecution {

	private void createPlanService(String planServiceName){
		
		String logFileName = "prepaidplan.log";
		String logModuleName = "CreateService";
		
		String apiURL=getAPIURL("planservice");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		//Initializing payload or API body		  
		 String apiBody = getPlanServiceJson(planServiceName);
		 Utility.printLog(logFileName,logModuleName , "Request Body", apiBody);
		 
		 JSONObject JSONResponseBody = httpPost(apiURL,apiBody);
		 String response = JSONResponseBody.toString(4);
		 Utility.printLog(logFileName,logModuleName , "Response", response);
		 		 

		 int status = JSONResponseBody.getInt("status");

		 if(status==200) {
			System.out.println("New Plan-Service is added successfully - " + planServiceName);
			
			String type = "Plan-Service";
			JSONObject jSONObject = JSONResponseBody.getJSONObject("plan");
			String name = jSONObject.getString("name");
			int id = jSONObject.getInt("id");

			DBOperations dbo = new DBOperations();
			dbo.setAPIData(type,name,id,status);
			
		 } else if(status==406) {			 
			String error = JSONResponseBody.getString("ERROR");
			System.out.println(error +" - " + planServiceName);
		 }
	}
	
	public void createPlanService(Map<String, String> map) {
		
		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			
			String service = value;
			createPlanService(service);
		}
	}

	public Map<String, String> readUniquePlanServiceList() {
		
		String sheetName = "Service";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getPlanDataSheet(sheetName);
		
		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();

		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);

			String service = cellValue.get("ServiceName");
			String ans = service;
			if(!"".equals(ans)) {
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getPlanServiceJson(String planServiceName) {
		
		String jsonString = "";
		
		try {
			
			org.json.simple.JSONObject planServiceJsonObject = null;
			
			ReadData readData = new ReadData();			
			planServiceJsonObject = readData.readJSONFile("CreatePlanService.json");
			
			planServiceJsonObject.put("name", planServiceName);
			
			jsonString = planServiceJsonObject.toJSONString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonString;
	}
	

}
