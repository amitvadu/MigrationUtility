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

public class SubProblemDomain extends RestExecution {

	private void createSubProblemDomain(String SubProblemDomainName,int problemDomainId) {

		String logFileName = "ticketdata.log";
		String logModuleName = "CreateSubProblemDomain";

		String apiURL = getAPIURL("ticketReasonSubCategory/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getSubProblemDomainJson(SubProblemDomainName,problemDomainId);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		// Fetching the desired value of a parameter
		int status = JSONResponseBody.getInt("responseCode");

		if (status == 200) {
			System.out.println("New SubProblemDomain is added successfully - " + SubProblemDomainName);

			String type = "SubProblemDomain";
			JSONObject cityJSONObject = JSONResponseBody.getJSONObject("data");
			String name = cityJSONObject.getString("subCategoryName");
			int id = cityJSONObject.getInt("id");

			DBOperations dbo = new DBOperations();
			dbo.setAPIData(type, name, id, status);
		}
	}

	public void createSubProblemDomain(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			
			String ans[] = value.split(":");

			String problemDomain = ans[0];
			String subProblemDomain = ans[1];
			
			DBOperations dbo = new DBOperations();			
			String query = "select id from status where entitytype='ProblemDomain' and name='" + problemDomain + "'";
			int problemDomainId = Integer.parseInt(dbo.getSingleData(query));
			
			createSubProblemDomain(subProblemDomain,problemDomainId);
		}
	}

	public Map<String, String> readUniqueSubProblemDomainList() {

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
			String subProblemDomain = cellValue.get("sub-problem domain");

			if (!"".equals(problemDomain)) {
				if ("".equals(subProblemDomain)) {
					subProblemDomain=problemDomain;					
				}				
				String ans = problemDomain + ":" + subProblemDomain;
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getSubProblemDomainJson(String subProblemDomainName,int problemDomainId) {

		String jsonString = "";

		try {

			org.json.simple.JSONObject subProblemDomainJsonObject = null;
			org.json.simple.JSONArray jsonArray= null;
			org.json.simple.JSONObject jsonObject = null;
			
			ReadData readData = new ReadData();
			subProblemDomainJsonObject = readData.readJSONFile("CreateSubProblemDomain.json");

			subProblemDomainJsonObject.put("subCategoryName", subProblemDomainName);			
			jsonObject = (org.json.simple.JSONObject) subProblemDomainJsonObject.get("parentCategory");			
			jsonObject.put("id", problemDomainId);
			
			jsonArray = (JSONArray) subProblemDomainJsonObject.get("ticketSubCategoryGroupReasonMappingList");
			jsonObject = (org.json.simple.JSONObject) jsonArray.get(0);
			
			jsonObject.put("reason", subProblemDomainName);
			
			jsonString = subProblemDomainJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}


}


