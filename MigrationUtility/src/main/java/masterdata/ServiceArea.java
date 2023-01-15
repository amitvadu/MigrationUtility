package masterdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import utility.Utility;

public class ServiceArea extends RestExecution {
	
	private static String logFileName = "masterdata.log";
	private static String logModuleName = "CreateServiceArea";

	private void createServiceArea(String serviceAreaName, int cityId, List<Integer> pincodes) {

		String apiURL = getAPIURL("serviceArea/save");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		// Initializing payload or API body
		String apiBody = getServiceAreaJson(serviceAreaName, cityId, pincodes);
		Utility.printLog(logFileName,logModuleName , "Request Body", apiBody);
		
		JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName,logModuleName , "Response", response);
		
		int status = JSONResponseBody.getInt("responseCode");

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

	public void createServiceArea(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			String ans[] = value.split(":");

			String tempSA[] = ans[0].split(",");
			
			for(int i=0;i<tempSA.length;i++) {
				
				String serviceAreaName = tempSA[i];
				String districtName = ans[1];

				Municipality municipality = new Municipality();
				int districtId = municipality.getDistrictId(districtName);
				List<Integer> pincodes = getPincodefromCity(districtId);
				Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
				createServiceArea(serviceAreaName, districtId, pincodes);
			}
		}
	}

	public Map<String, String> readUniqueServiceAreaList() {

		String sheetName = "Geogaraphical Areas";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getDemographicDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		for (int i = 0; i < sheetMap.size(); i++) {

			cellValue = sheetMap.get(i);
			if (!"".equals(cellValue.get("ServiceArea"))) {
				
				String serviceArea = cellValue.get("ServiceArea");
				String district = cellValue.get("District");

				String ans = serviceArea + ":" + district;
				valuemap.putIfAbsent(ans, ans);
			}
		}
		return valuemap;
	}

	@SuppressWarnings("unchecked")
	private String getServiceAreaJson(String serviceAreaName, int cityId, List<Integer> pincodes) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject serviceAreaJsonObject = new org.json.simple.JSONObject();

			ReadData readData = new ReadData();
			serviceAreaJsonObject = readData.readJSONFile("CreateServiceArea.json");

			serviceAreaJsonObject.put("name", serviceAreaName);
			serviceAreaJsonObject.put("cityid", cityId);
			serviceAreaJsonObject.put("pincodes", pincodes);
			
			serviceAreaJsonObject.put("id", "");
			serviceAreaJsonObject.put("lastModifiedById", "");
			serviceAreaJsonObject.put("status", "Active");
			serviceAreaJsonObject.put("isDeleted", false);
			serviceAreaJsonObject.put("latitude", "");
			serviceAreaJsonObject.put("longitude", "");
			serviceAreaJsonObject.put("areaid", "");
			serviceAreaJsonObject.put("mvnoId", 2);

			jsonString = serviceAreaJsonObject.toJSONString();

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
