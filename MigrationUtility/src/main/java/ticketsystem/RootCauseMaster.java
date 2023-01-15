package ticketsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.json.simple.JSONArray;

import api.ReadData;
import api.RestExecution;
import utility.DBOperations;
import utility.Utility;

public class RootCauseMaster extends RestExecution {

	private void createRootCause(String rootCause,String resolutionCode) {

		String logFileName = "ticketdata.log";
		String logModuleName = "CreateRootCause";

		String apiURL = getAPIURL("resolutionReasons/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getRootCauseMasterJson(rootCause,resolutionCode);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		// Fetching the desired value of a parameter
		int status = JSONResponseBody.getInt("responseCode");

		if (status == 200) {
			System.out.println("New RootCause is added successfully - " + rootCause);

			String type = "RootCause";
			JSONObject cityJSONObject = JSONResponseBody.getJSONObject("data");
			String name = cityJSONObject.getString("name");
			int id = cityJSONObject.getInt("id");

			DBOperations dbo = new DBOperations();
			dbo.setAPIData(type, name, id, status);
		}
		
	}

	public void createRootCause(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			
			String ans[] = value.split(":");

			String rootCause = ans[0];
			String resolutionCode = ans[1];
			
			createRootCause(rootCause,resolutionCode);
		}
	}

	public Map<String, String> readUniqueRootCauseList() {

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
			String rootCause = cellValue.get("Root Cause");
			String resolutionCode = cellValue.get("Resolution Code");

			if (!"".equals(rootCause) && !"".equals(resolutionCode)) {
				String ans = rootCause + ":" + resolutionCode;
				valuemap.putIfAbsent(ans, ans);
			}

		}

		return valuemap;

	}

	@SuppressWarnings("unchecked")
	private String getRootCauseMasterJson(String rootCause,String resolutionCode) {

		String jsonString = "";

		try {

			org.json.simple.JSONObject rootCauseJsonObject = null;
			org.json.simple.JSONArray jsonArray= null;
			org.json.simple.JSONObject jsonObject = null;
			
			ReadData readData = new ReadData();
			rootCauseJsonObject = readData.readJSONFile("CreateRootCause.json");

			rootCauseJsonObject.put("name", rootCause);
			
			jsonArray = (JSONArray) rootCauseJsonObject.get("rootCauseResolutionMappingList");
			jsonObject = (org.json.simple.JSONObject) jsonArray.get(0);
			
			jsonObject.put("rootCauseReason", resolutionCode);
			
						
		/*	
		  	org.json.simple.JSONObject jsonObject1 = (org.json.simple.JSONObject) jsonArray.get(0);
		    jsonObject1.put("rootCauseReason", "Test_AD1");
			jsonObject1.put("resolutionId", "");			
			jsonArray.add(0, jsonObject1); 
			
			jsonObject1.put("rootCauseReason", "Test_AD2");
			jsonObject1.put("resolutionId", "");			
			jsonArray.add(1, jsonObject1);
			
			jsonObject1.put("rootCauseReason", "Test_AD3");
			jsonObject1.put("resolutionId", "");			
			jsonArray.add(2, jsonObject1); 
		*/	
			rootCauseJsonObject.put("rootCauseResolutionMappingList", jsonArray);
			
			jsonString = rootCauseJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

}
