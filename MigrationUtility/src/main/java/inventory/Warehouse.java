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

public class Warehouse extends RestExecution {

	private static String logFileName = "inventory.log";
	private static String logModuleName = "CreateWarehouse";

	private void createWarehouse(Map<String, String> warehouseDetails) {

		String apiURL = getAPIURL("warehouseManagement/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getWarehouseJson(warehouseDetails);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");

		if (status == 200) {
			String message = "New Warehouse is added successfully - " + warehouseDetails.get("Name");
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);

		}  else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + warehouseDetails.get("Name");
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createWarehouse(List<Map<String, String>> warehouseMapList) {

		for (int i = 0; i < warehouseMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = warehouseMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createWarehouse(map);
		}
	}

	public List<Map<String, String>> readUniqueWarehouseList() {

		String sheetName = "Warehouse";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getInventoryDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> warehouseMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			if (!"".equals(cellValue.get("Name"))) {

				valuemap.put("Name", cellValue.get("Name"));
				valuemap.put("Type", cellValue.get("Type"));
				valuemap.put("ParentServiceArea", cellValue.get("ParentServiceArea"));
				valuemap.put("Description", cellValue.get("Description"));
				valuemap.put("Status", cellValue.get("Status"));

				valuemap.put("Latitude", cellValue.get("Latitude"));
				valuemap.put("Longitude", cellValue.get("Longitude"));

				valuemap.put("ServiceArea", cellValue.get("ServiceArea"));
				valuemap.put("Branch", cellValue.get("Branch"));
				valuemap.put("Address1", cellValue.get("Address1"));
				valuemap.put("Address2", cellValue.get("Address2"));
				valuemap.put("Municipality", cellValue.get("Municipality"));

				warehouseMapList.add(valuemap);

			}
		}
		return warehouseMapList;
	}

	@SuppressWarnings("unchecked")
	private String getWarehouseJson(Map<String, String> warehouseDetails) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject warehouseJsonObject = new org.json.simple.JSONObject();

			warehouseJsonObject.put("name", warehouseDetails.get("Name"));
			warehouseJsonObject.put("warehouseType", warehouseDetails.get("Type"));

			CommonGetAPI commonGetAPI = new CommonGetAPI();
			String parentServiceArea = warehouseDetails.get("ParentServiceArea");
			if(!"".equals(parentServiceArea)) {
				warehouseJsonObject.put("parentServiceAreaIdsList",commonGetAPI.getServiceAreaIdList(parentServiceArea));				
			} else {
				warehouseJsonObject.put("parentServiceAreaIdsList", null);
			}
			
			warehouseJsonObject.put("description", warehouseDetails.get("Description"));
			warehouseJsonObject.put("status", warehouseDetails.get("Status").toUpperCase());

			warehouseJsonObject.put("latitude", warehouseDetails.get("Latitude"));
			warehouseJsonObject.put("longitude", warehouseDetails.get("Longitude"));

			warehouseJsonObject.put("serviceAreaIdsList",
					commonGetAPI.getServiceAreaIdList(warehouseDetails.get("ServiceArea")));
			
			
			String branch = warehouseDetails.get("Branch");
			if(!"".equals(branch)) {				
				int branchId = commonGetAPI.getBranchId(branch);
				warehouseJsonObject.put("branchId", branchId);
			} else {
				warehouseJsonObject.put("branchId", null);
			}
			
			warehouseJsonObject.put("address1", warehouseDetails.get("Address1"));
			warehouseJsonObject.put("address2", warehouseDetails.get("Address2"));

			String municipality = warehouseDetails.get("Municipality");
			int municipalityId = commonGetAPI.getMunicipalityId(municipality);

			if (municipalityId != 0) {

				String temp = commonGetAPI.getMasterDetailsFromMunicipalityId(municipalityId);

				String data[] = temp.split(":");
				int countryId = Integer.parseInt(data[0]);
				int stateId = Integer.parseInt(data[1]);
				int cityId = Integer.parseInt(data[2]);

				warehouseJsonObject.put("pincode", municipalityId);
				warehouseJsonObject.put("city", cityId);
				warehouseJsonObject.put("state", stateId);
				warehouseJsonObject.put("country", countryId);
			}

			warehouseJsonObject.put("id", "");
			warehouseJsonObject.put("mvnoId", 2);

			jsonString = warehouseJsonObject.toJSONString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

}
