package prepaidplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import utility.Utility;

public class PlanBundle extends RestExecution {

	private final String logFileName = "prepaidplan.log";
	private final String logModuleName = "CreatePlanBundle";
	
	private void createPlanBundle(Map<String, String> planBundle){
		
		String apiURL=getAPIURL("addPlanGroup");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		//Initializing payload or API body		  
		 String apiBody = getPlanBundleJson(planBundle);
		 Utility.printLog(logFileName,logModuleName , "Request Body", apiBody);
		 
		 JSONObject JSONResponseBody = httpPost(apiURL,apiBody);
		 String response = JSONResponseBody.toString(4);
		// Utility.printLog(logFileName,logModuleName , "Response", response);

		 int status = JSONResponseBody.getInt("status");

		 if(status==200) {
			String message = "New Plan-Bundle is added successfully - " + planBundle.get("PlanBundleName");
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);

		 } else if(status==406) {
			String error = JSONResponseBody.getString("ERROR") + " - " + planBundle.get("PlanBundleName");
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		 }
	}
	
	public void createPlanBundle(List<Map<String, String>> planBundleMapList) {
		
		for (int i = 0; i < planBundleMapList.size(); i++) {
			
			Map<String, String> map = new HashMap<String, String>();
			map = planBundleMapList.get(i);		
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createPlanBundle(map);
		}
	}

	public List<Map<String, String>> readPlanBundleList() {
		
		String sheetName = "PlanBundle";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getPlanDataSheet(sheetName);
		
		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> planBundleMapList = new ArrayList<Map<String, String>>();
		
		for (int i = 0; i < sheetMap.size(); i++) {
			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);
			
			String planBundleName = cellValue.get("PlanBundleName");
			if ((!"".equals(planBundleName)) && (planBundleName != null)) {

				valuemap.put("PlanBundleName", planBundleName);
				valuemap.put("PlanType", cellValue.get("PlanType"));
				valuemap.put("ServiceArea", cellValue.get("ServiceArea"));
				valuemap.put("PlanMode", cellValue.get("PlanMode"));
				valuemap.put("PlanGroup", cellValue.get("PlanGroup"));
				valuemap.put("PlanCategory", cellValue.get("PlanCategory"));
				valuemap.put("AllowDiscount", cellValue.get("AllowDiscount"));
				valuemap.put("ServicesAndPlans", cellValue.get("[Service:PlanName]"));
				planBundleMapList.add(valuemap);
			}
		}
		return planBundleMapList;
	}

	@SuppressWarnings("unchecked")
	private String getPlanBundleJson(Map<String, String> planBundle) {
		
		String jsonString = null;
		
		try {
			
			org.json.simple.JSONObject planBundleJsonObject = new org.json.simple.JSONObject();
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			
			planBundleJsonObject.put("planMode", planBundle.get("PlanMode").toUpperCase());
			planBundleJsonObject.put("mvnoId", 2);
			planBundleJsonObject.put("planGroupId", null);
			planBundleJsonObject.put("planGroupName", planBundle.get("PlanBundleName"));
			
			int serviceAreaId = commonGetAPI.getServiceAreaIdList(planBundle.get("ServiceArea")).get(0);
			if (serviceAreaId != 0) {
				planBundleJsonObject.put("serviceAreaId", serviceAreaId);
			}
			
			planBundleJsonObject.put("status", "Active");			
			planBundleJsonObject.put("planType", planBundle.get("PlanType"));
			planBundleJsonObject.put("planGroupType", planBundle.get("PlanGroup"));
			planBundleJsonObject.put("category", planBundle.get("PlanCategory"));
			planBundleJsonObject.put("allowdiscount", Boolean.valueOf(planBundle.get("AllowDiscount")));
			
			//--Plan Details
			
			List<org.json.simple.JSONObject> planDetailsList = new ArrayList<org.json.simple.JSONObject>();
			
			String servicesAndPlans = planBundle.get("ServicesAndPlans");
			
			//String servicesAndPlans = "[FTTH:Plan_4],[DTV:Plan_5],[FTTH:Plan_6],[DTV:Plan_7]";
			servicesAndPlans = servicesAndPlans.replaceAll("[\\[\\]]", "");			
			String ans[] = servicesAndPlans.split(",");
			
			for(int i=0;i<ans.length;i++) {
				
				String temp[] = ans[i].split(":");
				String service = temp[0];
				String planName = temp[1];
				
				int planId = commonGetAPI.getPlanId(planName);
				String planDetails[] = commonGetAPI.getPlanDetails(planId).split(":");

				String serviceName = planDetails[0];
				if(service.equalsIgnoreCase(serviceName)) {
					
					float offerPrice = Float.valueOf(planDetails[1]);
					int validity = Integer.parseInt(planDetails[2]);
					String unitsOfValidity = planDetails[3];
					float newOfferPrice = Float.valueOf(planDetails[4]);
					
					org.json.simple.JSONObject planDetailsJsonObject = new org.json.simple.JSONObject();
					
					planDetailsJsonObject.put("service", serviceName);
					planDetailsJsonObject.put("planId", planId);
					planDetailsJsonObject.put("validity", validity);
					planDetailsJsonObject.put("amount", offerPrice);
					planDetailsJsonObject.put("validityUnit", unitsOfValidity);
					planDetailsJsonObject.put("planGroupId", "");
					planDetailsJsonObject.put("planGroupMappingId", "");
					planDetailsJsonObject.put("newOfferPrice", newOfferPrice);
					
					planDetailsList.add(planDetailsJsonObject);
				}
			}
			
			planBundleJsonObject.put("planMappingList", planDetailsList);
						
			jsonString = planBundleJsonObject.toJSONString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonString;
	}
	

}
