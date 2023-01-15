package inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import utility.DBOperations;
import utility.Utility;

public class Inward extends RestExecution {

	private static String logFileName = "inventory.log";
	private static String logModuleName = "CreateInward";

	private void createInward(Map<String, String> inwardDetails) {

		String apiURL = getAPIURL("inwards/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getInwardJson(inwardDetails);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");

		if (status == 200) {

			String inwardNumber = JSONResponseBody.getJSONObject("data").getString("inwardNumber");

			String message = "New Inward is added successfully - " + inwardNumber;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);

			int inwardId = JSONResponseBody.getJSONObject("data").getInt("id");

			String product = inwardDetails.get("Product");
			String warehouse = inwardDetails.get("Warehouse");
			String name = product + "_" + warehouse;
			String type = "Inward";

			DBOperations dbo = new DBOperations();
			dbo.setAPIData(type, name, inwardId, status);

			inwardMacMappingTemp(inwardId, inwardDetails);
			inwardApproval(inwardId);

		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + inwardDetails.get("Product");
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createInward(List<Map<String, String>> warehouseMapList) {

		for (int i = 0; i < warehouseMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = warehouseMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createInward(map);
		}
	}

	public List<Map<String, String>> readUniqueInwardList() {

		String sheetName = "Inward";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getInventoryDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> inwardMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			if (!"".equals(cellValue.get("Product"))) {

				valuemap.put("Product", cellValue.get("Product"));
				valuemap.put("Warehouse", cellValue.get("Warehouse"));
				valuemap.put("InwardDate", cellValue.get("InwardDate"));
				valuemap.put("QuantityInward", cellValue.get("QuantityInward"));
				valuemap.put("InwardMAC", cellValue.get("InwardMAC"));
				valuemap.put("InwardSerialNumber", cellValue.get("InwardSerialNumber"));
				valuemap.put("Type", cellValue.get("Type"));
				valuemap.put("Status", cellValue.get("Status"));

				inwardMapList.add(valuemap);

			}
		}
		return inwardMapList;
	}

	@SuppressWarnings("unchecked")
	private String getInwardJson(Map<String, String> inwardDetails) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject inwardJsonObject = new org.json.simple.JSONObject();

			CommonGetAPI commonGetAPI = new CommonGetAPI();
			int productId = commonGetAPI.getProductId(inwardDetails.get("Product"));
			if (productId != 0) {
				inwardJsonObject.put("productId", productId);
			}

			int warehouseId = commonGetAPI.getWarehouseId(inwardDetails.get("Warehouse"));
			inwardJsonObject.put("destinationId", warehouseId);
			inwardJsonObject.put("destinationType", "Warehouse");

			int qtyInward = Integer.parseInt(inwardDetails.get("QuantityInward"));
			if (qtyInward > 0) {
				inwardJsonObject.put("inTransitQty", qtyInward);
			}

			String inwardDate = inwardDetails.get("InwardDate");
			if (!"".equals(inwardDate)) {
				inwardDate = Utility.getDateTimeInRequiredFormatFromProvidedDateTime(inwardDate, "dd-MMM-yyyy",
						"yyyy-MM-dd'T'HH:mm:ss");
				inwardJsonObject.put("inwardDateTime", inwardDate);
			}

			inwardJsonObject.put("type", inwardDetails.get("Type"));
			inwardJsonObject.put("status", inwardDetails.get("Status").toUpperCase());

			inwardJsonObject.put("id", null);
			inwardJsonObject.put("qty", null);
			inwardJsonObject.put("usedQty", null);
			inwardJsonObject.put("unusedQty", null);
			inwardJsonObject.put("outTransitQty", null);
			inwardJsonObject.put("rejectedQty", null);
			inwardJsonObject.put("totalMacSerial", null);

			inwardJsonObject.put("mvnoId", null);

			jsonString = inwardJsonObject.toJSONString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

	private String inwardMacMapping(int inwardNumber, Map<String, String> macDetails) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject macMappingJsonObject = new org.json.simple.JSONObject();

			String tempMAC = macDetails.get("InwardMAC");
			String tempSerialNumber = macDetails.get("InwardSerialNumber");

			if ((!"".equals(tempMAC)) && (!"".equals(tempSerialNumber))) {

				String macList[] = tempMAC.split(",");
				String snList[] = tempSerialNumber.split(",");

				for (int i = 0; i < macList.length; i++) {
					String mac = macList[i];
					String serial = snList[i];

					macMappingJsonObject.put("macAddress", mac);
					macMappingJsonObject.put("serialNumber", serial);
					macMappingJsonObject.put("id", null);
					macMappingJsonObject.put("inwardId", inwardNumber);
					macMappingJsonObject.put("status", macDetails.get("Status").toUpperCase());

					jsonString = macMappingJsonObject.toJSONString();

					String apiURL = getAPIURL("inoutWardMacMapping/save");
					Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

					// Initializing payload or API body
					String apiBody = jsonString;
					Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

					JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
					String response = JSONResponseBody.toString(4);
					Utility.printLog(logFileName, logModuleName, "Response", response);

					int status = JSONResponseBody.getInt("responseCode");

					if (status == 200) {

						String message = "New MAC/Serial is added successfully - " + mac + "/" + serial;
						System.out.println(message);
						Utility.printLog("execution.log", logModuleName, "Success", message);

					} else if (status == 406) {
						String error = JSONResponseBody.getString("responseMessage") + " - " + mac + "/" + serial;
						System.out.println(error);
						Utility.printLog("execution.log", logModuleName, "Already Exist", error);
					}
				}
			}

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

	private String inwardApproval(int inwardNumber) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject macMappingJsonObject = new org.json.simple.JSONObject();

			if (inwardNumber != 0) {

				macMappingJsonObject.put("id", inwardNumber);
				macMappingJsonObject.put("approvalStatus", "Approve");
				macMappingJsonObject.put("approvalRemark", "Approved by migration");

				jsonString = macMappingJsonObject.toJSONString();

				String apiURL = getAPIURL("inwards/inwardApproval");
				Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

				// Initializing payload or API body
				String apiBody = jsonString;
				Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

				JSONObject JSONResponseBody = httpPut(apiURL, apiBody);
				String response = JSONResponseBody.toString(4);
				Utility.printLog(logFileName, logModuleName, "Response", response);

				int status = JSONResponseBody.getInt("responseCode");

				if (status == 200) {

					String inwardNumber1 = JSONResponseBody.getJSONObject("data").getString("inwardNumber");
					String message = "New Inward is approved successfully - " + inwardNumber1;

					System.out.println(message);
					Utility.printLog("execution.log", logModuleName, "Success", message);

				} else {
					String error = JSONResponseBody.getString("responseMessage") + " - IN-" + inwardNumber;
					System.out.println(error);
					Utility.printLog("execution.log", logModuleName, "Inward Approval", error);
				}
			}
		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

	
	private String inwardMacMappingTemp(int inwardNumber, Map<String, String> macDetails) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject macMappingJsonObject = new org.json.simple.JSONObject();

			String tempMAC = macDetails.get("InwardMAC");
			String tempSerialNumber = macDetails.get("InwardSerialNumber");

			if ((!"".equals(tempMAC)) && (!"".equals(tempSerialNumber))) {

				String macList[] = tempMAC.split(",");
				String snList[] = tempSerialNumber.split(",");

				for (int i = 0; i < macList.length; i++) {
					//String mac = macList[i];
					//String serial = snList[i];
					
					String mac = Utility.getRandomMacAddress();
					String serial =  Utility.getRandomSerialNumber("A", 4);

					
					macMappingJsonObject.put("macAddress", mac);
					macMappingJsonObject.put("serialNumber", serial);
					macMappingJsonObject.put("id", null);
					macMappingJsonObject.put("inwardId", inwardNumber);
					macMappingJsonObject.put("status", macDetails.get("Status").toUpperCase());

					jsonString = macMappingJsonObject.toJSONString();

					String apiURL = getAPIURL("inoutWardMacMapping/save");
					Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

					// Initializing payload or API body
					String apiBody = jsonString;
					Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

					JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
					String response = JSONResponseBody.toString(4);
					Utility.printLog(logFileName, logModuleName, "Response", response);

					int status = JSONResponseBody.getInt("responseCode");

					if (status == 200) {

						String message = "New MAC/Serial is added successfully - " + mac + "/" + serial;
						System.out.println(message);
						Utility.printLog("execution.log", logModuleName, "Success", message);

					} else if (status == 406) {
						String error = JSONResponseBody.getString("responseMessage") + " - " + mac + "/" + serial;
						System.out.println(error);
						Utility.printLog("execution.log", logModuleName, "Already Exist", error);
					}
				}
			}

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}


	
}
