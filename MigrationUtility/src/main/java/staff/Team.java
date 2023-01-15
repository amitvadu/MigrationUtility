package staff;

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

public class  Team extends RestExecution {
	
	private static String logFileName = "Staff.log";
	private static String logModuleName = "CreateTeam";

	private void createTeam(String teamName) {

		String apiURL = getAPIURL("teams/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getTeamJson(teamName);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");

		if (status == 200) {
			String message = "New Team is added successfully - " + teamName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + teamName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createTeam(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			
			String team = value;
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createTeam(team);
		}
	}

	public Map<String, String> readUniqueTeamList() {
		
		String sheetName = "Teams";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);
			if (!"".equals(cellValue.get("Name"))) {
				
				String ans = cellValue.get("Name");
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
		
	}

	@SuppressWarnings("unchecked")
	private String getTeamJson(String teamName) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject teamJsonObject = new org.json.simple.JSONObject();
			
			//ReadData readData = new ReadData();
			//teamJsonObject = readData.readJSONFile("CreateTeam.json");
			
			teamJsonObject.put("name", teamName);
			teamJsonObject.put("status", "active");
			
			jsonString = teamJsonObject.toJSONString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}


}


