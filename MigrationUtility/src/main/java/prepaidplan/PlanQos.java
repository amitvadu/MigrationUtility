package prepaidplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import utility.DBOperations;
import utility.Utility;

public class PlanQos extends RestExecution {

	private void createPlanQos(Map<String, String> qosDetails){
		
		String logFileName = "prepaidplan.log";
		String logModuleName = "CreateQos";
		
		String apiURL=getAPIURL("qosPolicy/save");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		//Initializing payload or API body		  
		 String apiBody = getPlanQosJson(qosDetails);
		 Utility.printLog(logFileName,logModuleName, "Request Body", apiBody);
		 
		 JSONObject JSONResponseBody = httpPost(apiURL,apiBody);
		 String response = JSONResponseBody.toString(4);
		 Utility.printLog(logFileName,logModuleName , "Response", response);
		 		 

		 int status = JSONResponseBody.getInt("responseCode");

		 if(status==200 || status==0) {
			System.out.println("New Plan-Qos is added successfully - " +  qosDetails.get("QosName"));
			
			String type = "Plan-Qos";
			JSONObject cityJSONObject = JSONResponseBody.getJSONObject("data");
			String name = cityJSONObject.getString("name");
			int id = cityJSONObject.getInt("id");

			DBOperations dbo = new DBOperations();
			dbo.setAPIData(type,name,id,status);
			
		 } else if (status == 406) {
				String error = JSONResponseBody.getString("responseMessage");
				System.out.println(error + " - " + qosDetails.get("QosName"));
		}
	}
	
	public void createPlanQos(List<Map<String, String>> qosMapList) {

		for (int i = 0; i < qosMapList.size(); i++) {
			
			Map<String, String> map = new HashMap<String, String>();
			map = qosMapList.get(i);			
			createPlanQos(map);
		}
	}

	public List<Map<String, String>> readUniquePlanQosList() {
		
		String sheetName = "Qos";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getPlanDataSheet(sheetName);
		
		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> qosMapList = new ArrayList<Map<String, String>>();
		
		for (int i = 0; i < sheetMap.size(); i++) {
			
			Map<String, String> valuemap = new HashMap<String, String>();			
			cellValue = sheetMap.get(i);
			
			if(!"".equals(cellValue.get("QosPolicyName"))) {
				
				valuemap.put("QosName", cellValue.get("QosPolicyName"));
				valuemap.put("PolicyName", cellValue.get("PolicyName"));
				valuemap.put("BasePolicyName", cellValue.get("BasePolicyName"));
				valuemap.put("Param1", cellValue.get("Param1"));
				valuemap.put("Param2", cellValue.get("Param2"));
				valuemap.put("Param3", cellValue.get("Param3"));
				valuemap.put("BaseParam1", cellValue.get("BaseParam1"));
				valuemap.put("BaseParam2", cellValue.get("BaseParam2"));
				valuemap.put("BaseParam3", cellValue.get("BaseParam3"));
				qosMapList.add(valuemap);
			}
		}
		return qosMapList;
	}

	@SuppressWarnings("unchecked")
	private String getPlanQosJson(Map<String, String> qosDetails) {
		
		String jsonString = "";
		
		try {
			
			org.json.simple.JSONObject qosJsonObject = null;
			
			ReadData readData = new ReadData();			
			qosJsonObject = readData.readJSONFile("CreatePlanQos.json");
			qosJsonObject.put("name", qosDetails.get("QosName"));
			qosJsonObject.put("thpolicyname", qosDetails.get("PolicyName"));
			qosJsonObject.put("basepolicyname", qosDetails.get("BasePolicyName"));
			qosJsonObject.put("thparam1", qosDetails.get("Param1"));
			qosJsonObject.put("thparam2", qosDetails.get("Param2"));
			qosJsonObject.put("thparam3", qosDetails.get("Param3"));
			qosJsonObject.put("baseparam1", qosDetails.get("BaseParam1"));
			qosJsonObject.put("baseparam2", qosDetails.get("BaseParam2"));
			qosJsonObject.put("baseparam3", qosDetails.get("BaseParam3"));
			qosJsonObject.put("description", "Migration");
			
			jsonString = qosJsonObject.toJSONString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonString;
	}
	

}
