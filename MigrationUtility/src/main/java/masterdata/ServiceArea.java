package masterdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import utility.Utility;

public class ServiceArea extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateServiceArea";

	private void createServiceArea(Map<String, String> serviceArea) {

		String apiURL = getAPIURL("serviceArea/save");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		// Initializing payload or API body
		String apiBody = getServiceAreaJson(serviceArea);
		Utility.printLog(logFileName,logModuleName , "Request Body", apiBody);
		
		JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName,logModuleName , "Response", response);
		
		int status = JSONResponseBody.getInt("responseCode");
		String serviceAreaName = serviceArea.get("ServiceArea");
		
		if (status == 200) {
			String message = "New serviceArea is added successfully - " + serviceAreaName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + serviceAreaName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createServiceArea(List<Map<String, String>> serviceAreaMapList) {
		
		for (int i = 0; i < serviceAreaMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = serviceAreaMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createServiceArea(map);
		}
	}

	public List<Map<String, String>> readServiceAreaList() {

		String sheetName = "ServiceArea";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> serviceAreaMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String serviceArea = cellValue.get("ServiceArea");
			if ((!"".equals(serviceArea)) && (serviceArea != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("ServiceArea", cellValue.get("ServiceArea"));
				valuemap.put("District", cellValue.get("District"));
				valuemap.put("Latitude", cellValue.get("Latitude"));
				valuemap.put("Longitude", cellValue.get("Longitude"));
				valuemap.put("Status", cellValue.get("Status"));
				serviceAreaMapList.add(valuemap);
			}
		}
		return serviceAreaMapList;
	}

	private String getServiceAreaJson(Map<String, String> serviceArea) {

		String jsonString = null;

		try {

			JSONObject serviceAreaJsonObject = new JSONObject();
			
			Municipality municipality = new Municipality();
			int districtId = municipality.getDistrictId(serviceArea.get("District"));
			
			List<Integer> pincodes = getPincodefromCity(districtId);
			
			serviceAreaJsonObject.put("name", serviceArea.get("ServiceArea"));
			serviceAreaJsonObject.put("cityid", districtId);
			serviceAreaJsonObject.put("pincodes", pincodes);
			serviceAreaJsonObject.put("latitude", serviceArea.get("Latitude"));
			serviceAreaJsonObject.put("longitude", serviceArea.get("Longitude"));
			serviceAreaJsonObject.put("status", serviceArea.get("Status"));
			
			serviceAreaJsonObject.put("id", "");
			serviceAreaJsonObject.put("lastModifiedById", "");
			serviceAreaJsonObject.put("isDeleted", false);
			serviceAreaJsonObject.put("areaid", "");
			serviceAreaJsonObject.put("mvnoId", 2);

			jsonString = serviceAreaJsonObject.toString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

	private List<Integer> getPincodefromCity(int cityId) {

		String apiURL = "serviceArea/getPincodefromCity?id=" + cityId;
		apiURL = getAPIURL(apiURL);

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		List<Integer> list = new ArrayList<Integer>();

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(jsonArray.getJSONObject(i).getInt("id"));
			}
		}
		return list;
	}

}
