package masterdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import utility.Utility;

public class SubBusinessUnit extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateSubBusinessUnit";

	private void createSubBusinessUnit(Map<String, String> subBusinessUnit) {

		String apiURL = getAPIURL("subbusinessunit/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getSubBusinessUnitJson(subBusinessUnit);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");
		String subBUName = subBusinessUnit.get("SubBusinessUnitName");
				
		if (status == 200) {
			String message = "New Sub Business Unit is added successfully - " + subBUName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + subBUName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
		
	}

	public void createSubBusinessUnit(List<Map<String, String>> SubBUMapList) {

		for (int i = 0; i < SubBUMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = SubBUMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createSubBusinessUnit(map);
		}
	}

	public List<Map<String, String>> readSubBusinessUnitList() {
		
		String sheetName = "SubBusinessUnit";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> SubBUMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String subBusinessUnit = cellValue.get("SubBusinessUnitName");
			if ((!"".equals(subBusinessUnit)) && (subBusinessUnit != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("SubBusinessUnitName", cellValue.get("SubBusinessUnitName"));
				valuemap.put("SubBusinessUnitCode", cellValue.get("SubBusinessUnitCode"));
				valuemap.put("BusinessUnitName", cellValue.get("BusinessUnitName"));
				valuemap.put("Status", cellValue.get("Status"));
				SubBUMapList.add(valuemap);
			}
		}
		return SubBUMapList;
	}

	private String getSubBusinessUnitJson(Map<String, String> subBusinessUnit) {

		String jsonString = null;

		try {

			JSONObject businessVericalJsonObject = new JSONObject();

			businessVericalJsonObject.put("subbuname", subBusinessUnit.get("SubBusinessUnitName"));
			businessVericalJsonObject.put("subbucode", subBusinessUnit.get("SubBusinessUnitCode"));
			
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			
			int businessUnitId = commonGetAPI.getBusinessUnitIdList(subBusinessUnit.get("BusinessUnitName")).get(0);
			businessVericalJsonObject.put("businessunitid", businessUnitId);
			businessVericalJsonObject.put("status", subBusinessUnit.get("Status"));

			jsonString = businessVericalJsonObject.toString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

}
