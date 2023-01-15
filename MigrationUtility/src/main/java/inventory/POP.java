package inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import utility.Utility;

public class POP extends RestExecution {

	private String logFileName = "inventory.log";
	private String logModuleName = "CreatePop";

	private void createPop(Map<String, String> popDetails) {

		String apiURL = getAPIURL("popmanagement/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String apiBody = getPopJson(popDetails);
		Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

		if (!apiBody.equals(null)) {
			JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
			String response = JSONResponseBody.toString(4);
			Utility.printLog(logFileName, logModuleName, "Response", response);

			int status = JSONResponseBody.getInt("responseCode");

			if (status == 200) {
				String message = "New POP is added successfully - " + popDetails.get("Name");
				System.out.println(message);
				Utility.printLog("execution.log", logModuleName, "Success", message);

			} else if (status == 406) {
				String error = JSONResponseBody.getString("responseMessage") + " - " + popDetails.get("Name");
				System.out.println(error);
				Utility.printLog("execution.log", logModuleName, "Already Exist", error);
			}
		}
	}

	public void createPop(List<Map<String, String>> popMapList) {

		for (int i = 0; i < popMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = popMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createPop(map);
		}
	}

	public List<Map<String, String>> readUniquePopList() {

		String sheetName = "POP";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getInventoryDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> popMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			if (!"".equals(cellValue.get("Name"))) {
				
				valuemap.put("Name", cellValue.get("Name"));
				valuemap.put("Latitude", cellValue.get("Latitude"));
				valuemap.put("Longitude", cellValue.get("Longitude"));
				valuemap.put("ServiceArea", cellValue.get("ServiceArea"));
				valuemap.put("Status", cellValue.get("Status"));
				popMapList.add(valuemap);
			}
		}
		return popMapList;
	}

	@SuppressWarnings("unchecked")
	private String getPopJson(Map<String, String> popDetails) {

		String jsonString = "";

		try {

			org.json.simple.JSONObject popJsonObject = new org.json.simple.JSONObject();

			// ReadData readData = new ReadData();
			// productJsonObject = readData.readJSONFile("CreateProduct.json");

			
			popJsonObject.put("name", popDetails.get("Name"));
			popJsonObject.put("latitude", popDetails.get("Latitude"));
			popJsonObject.put("longitude", popDetails.get("Longitude"));
			
			CommonGetAPI commonGetAPI = new CommonGetAPI();			
			popJsonObject.put("serviceAreaIdsList", commonGetAPI.getServiceAreaIdList(popDetails.get("ServiceArea")));
			popJsonObject.put("status", popDetails.get("Status"));
			
			popJsonObject.put("id", "");
			popJsonObject.put("isDeleted", false);
			popJsonObject.put("createdById", "");
			popJsonObject.put("lastModifiedById", "");
			popJsonObject.put("mvnoId", 2);
			
			jsonString = popJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

}
