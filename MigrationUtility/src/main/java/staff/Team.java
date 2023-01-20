package staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import utility.Utility;

public class  Team extends RestExecution {
	
	private static String logFileName = "Staff.log";
	private static String logModuleName = "CreateTeam";

	private void createTeam(Map<String, String> teamDetails) {

		String apiURL = getAPIURL("teams/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getTeamJson(teamDetails);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");
		String teamName = teamDetails.get("TeamName");
				
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

	public void createTeam(List<Map<String, String>> teamsMapList) {
		
		for (int i = 0; i < teamsMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = teamsMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createTeam(map);
		}
	}

	public List<Map<String, String>> readTeamList() {
		
		String sheetName = "Teams";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> teamsMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String teamName = cellValue.get("TeamName");
			if ((!"".equals(teamName)) && (teamName != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("TeamName", cellValue.get("TeamName"));
				valuemap.put("Status", cellValue.get("Status"));
				teamsMapList.add(valuemap);
			}
		}
		return teamsMapList;
	}

	private String getTeamJson(Map<String, String> teamDetails) {

		String jsonString = null;

		try {

			JSONObject teamJsonObject = new JSONObject();
			
			teamJsonObject.put("name", teamDetails.get("TeamName"));
			teamJsonObject.put("status", teamDetails.get("TeamName").toLowerCase());
			
			jsonString = teamJsonObject.toString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}


}


