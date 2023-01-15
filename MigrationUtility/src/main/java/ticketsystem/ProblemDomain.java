package ticketsystem;

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

public class ProblemDomain extends RestExecution {

	private void createProblemDomain(String problemDomainName,int serviceId) {

		String logFileName = "ticketdata.log";
		String logModuleName = "CreateProblemDomain";

		String apiURL = getAPIURL("ticketReasonCategory/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getProblemDomainJson(problemDomainName,serviceId);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		// Fetching the desired value of a parameter
		int status = JSONResponseBody.getInt("responseCode");

		if (status == 200) {
			System.out.println("New ProblemDomain is added successfully - " + problemDomainName);

			String type = "ProblemDomain";
			JSONObject cityJSONObject = JSONResponseBody.getJSONObject("data");
			String name = cityJSONObject.getString("categoryName");
			int id = cityJSONObject.getInt("id");

			DBOperations dbo = new DBOperations();
			dbo.setAPIData(type, name, id, status);
		}
	}

	public void createProblemDomain(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			
			String ans[] = value.split(":");

			String problemDomain = ans[0];
			String serviceType = ans[1];
			
			int serviceId = getPlanServiceId(serviceType);
			//int serviceId = getPlanServiceId("FTTH1");
			createProblemDomain(problemDomain,serviceId);
		}
	}

	public Map<String, String> readUniqueProblemDomainList() {

		String sheetName = "OLA_Matrix";
		int startRow = 1;
		int endRow = 228;

		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getTicketDataSheet(sheetName, startRow, endRow);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();

		for (int i = startRow; i < endRow; i++) {

			cellValue = sheetMap.get(i);
			String problemDomain = cellValue.get("problem domain");
			String serviceType = cellValue.get("Service Type");

			if (!"".equals(problemDomain)) {
				String ans = problemDomain + ":" + serviceType;
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getProblemDomainJson(String problemDomainName,int serviceId) {

		String jsonString = "";

		try {

			org.json.simple.JSONObject problemDomainJsonObject = null;
			org.json.simple.JSONObject jsonObject = null;
			
			ReadData readData = new ReadData();
			problemDomainJsonObject = readData.readJSONFile("CreateProblemDomain.json");

			problemDomainJsonObject.put("categoryName", problemDomainName);			
			jsonObject = (org.json.simple.JSONObject) problemDomainJsonObject.get("service");			
			jsonObject.put("id", serviceId);
			
			jsonString = problemDomainJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	private int getPlanServiceId(String serviceName) {

		String apiURL = getAPIURL("planservice/all");

		JSONObject jsonResponse = httpGet(apiURL);
		//String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("status");

		int serviceId=-1;
		
		if (status == 200) {
			
			org.json.JSONArray jsonArray = jsonResponse.getJSONArray("serviceList");
			
			for (int i = 0; i < jsonArray.length(); i++) {
				//list.add(jsonArray.getJSONObject(i).getInt("id"));
				 
				 JSONObject serviceListJSONObject = jsonArray.getJSONObject(i);
				 String tempServiceName  = serviceListJSONObject.getString("name");
				 if(serviceName.equals(tempServiceName)) {
					 serviceId  = serviceListJSONObject.getInt("id");
					 break;
				 }
			}
		}
		return serviceId;
	}


}


