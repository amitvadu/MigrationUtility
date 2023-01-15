package SalesCRM;

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

public class RejectedReasonMaster extends RestExecution {

	private void createRejectedReason(String rejectedReason) {

		String logFileName = "SalesCRM.log";
		String logModuleName = "CreateRejectedReasonMaster";

		String apiURL = getAPIURL("AdoptSalesCrmsBss/rejectReason/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getRejectedReasonJson(rejectedReason);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		// Fetching the desired value of a parameter
		int status = JSONResponseBody.getInt("status");
		// System.out.println("status = " + result1);

		if (status == 200) {
			System.out.println("New RejectedReasonMaster is added successfully - " + rejectedReason);

			String type = "RejectedReasonMaster";
			JSONObject cityJSONObject = JSONResponseBody.getJSONObject("rejectReason");
			String name = cityJSONObject.getString("name");
			int id = cityJSONObject.getInt("id");

			DBOperations dbo = new DBOperations();
			dbo.setAPIData(type, name, id, status);
		}
	}

	public void createRejectedReason(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			
			String rejectedReason = value;
						
			createRejectedReason(rejectedReason);
		}
	}

	public Map<String, String> readUniqueRejectedReasonList() {

		String sheetName = "Rejection Master";
		int startRow = 1;
		int endRow = 13;

		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getSalesCRMDataSheet(sheetName, startRow, endRow);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		String ans = "";
		
		for (int i = startRow; i < endRow; i++) {
			
			cellValue = sheetMap.get(i);
			String rejectedReason = cellValue.get("Rejection on Lead");
			ans=rejectedReason;
			valuemap.putIfAbsent(ans, ans);	
		}
		return valuemap;
	}

	@SuppressWarnings({ "unchecked" })
	private String getRejectedReasonJson(String rejectedReason) {

		String jsonString = "";

		try {

			org.json.simple.JSONObject rejectedReasonJsonObject = null;
			ReadData readData = new ReadData();
			rejectedReasonJsonObject = readData.readJSONFile("CreateRejectedReasonMaster.json");
			
			List<org.json.simple.JSONObject> jsonObjectList = new ArrayList<org.json.simple.JSONObject>();
			
			rejectedReasonJsonObject.put("name", rejectedReason);
			rejectedReasonJsonObject.put("rejectSubReasonDtoList", jsonObjectList);
			jsonString = rejectedReasonJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}


}


