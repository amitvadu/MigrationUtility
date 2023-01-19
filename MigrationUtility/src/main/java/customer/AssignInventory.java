package customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import utility.Utility;

public class AssignInventory extends RestExecution {

	private static String logFileName = "prepaidplan.log";
	private static String logModuleName = "AssignInventoryToCustomer";

	private void AssignInventoryToCustomer(Map<String, String> customerDetailsMap) {

		String apiURL = getAPIURL("customers");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String apiBody = getAssignInventoryJson(customerDetailsMap);
		Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

		if (!apiBody.equals(null)) {

			JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
			String response = JSONResponseBody.toString(4);
			Utility.printLog(logFileName, logModuleName, "Response", response);

			// Fetching the desired value of a parameter
			int status = JSONResponseBody.getInt("status");

			if (status == 200) {
				String message = "An inventory is assigned to customer successfully - " + customerDetailsMap.get("CustomerUsername");
				System.out.println(message);
				Utility.printLog("execution.log", logModuleName, "Success", message);				
			} else if (status == 406) {
				String error = JSONResponseBody.getString("ERROR");
				System.out.println(error + " - " + customerDetailsMap.get("CustomerUsername"));
			}
		}
	}

	public void AssignInventoryToCustomer(List<Map<String, String>> customerMapList) {

		for (int i = 0; i < customerMapList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map = customerMapList.get(i);

			String userName = map.get("CustomerUsername");
			if (checkcustomerUsernameIsAlreadyExists(userName)) {
				Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
				AssignInventoryToCustomer(map);
			} else {
				System.out.println("Customer UserName is NOT found - " + userName);
			}
		}
	}

	public List<Map<String, String>> readAssignInventoryCustomerList() {

		String sheetName = "AssignCustomerInventory";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getPlanDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> customerMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String customerUsername = cellValue.get("CustomerUsername");
			if ((!"".equals(customerUsername)) && (customerUsername != null)) {

				valuemap.put("customerUsername", cellValue.get("customerUsername"));
				valuemap.put("Service", cellValue.get("Service"));
				valuemap.put("Product", cellValue.get("Product"));
				valuemap.put("InwardNumber", cellValue.get("InwardNumber"));
				valuemap.put("MAC", cellValue.get("MAC"));
				valuemap.put("SerialNumber", cellValue.get("SerialNumber"));
				valuemap.put("AssignDate", cellValue.get("AssignDate"));
				valuemap.put("Status", cellValue.get("Status"));
				
				customerMapList.add(valuemap);
			}
		}
		return customerMapList;
	}

	@SuppressWarnings("unchecked")
	private String getAssignInventoryJson(Map<String, String> customerDetails) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject customerJsonObject = new org.json.simple.JSONObject();

			CommonGetAPI commonGetAPI = new CommonGetAPI();
			int productId = commonGetAPI.getProductId(customerDetails.get("Product"));
			if (productId != 0) {
				customerJsonObject.put("productId", productId);
				customerJsonObject.put("qty", 1);
			}
			
			int serviceId = commonGetAPI.getServiceIdList(customerDetails.get("Service")).get(0);
			if (serviceId != 0) {
				customerJsonObject.put("serviceId", serviceId);
			}
			
			String customerUsername = customerDetails.get("CustomerUsername");
			int customerId = getCustomerId(customerUsername);
			if (customerId != 0) {
				customerJsonObject.put("customerId", customerId);
			}
			
			int staffId = commonGetAPI.getStaffId(customerDetails.get("admin"));
			
			customerJsonObject.put("staffId", staffId);
			customerJsonObject.put("status", customerDetails.get("Status"));
			
			customerJsonObject.put("inwardId", 0);
			
			String assignDate = customerDetails.get("AssignDate");
			if (!"".equals(assignDate)) {
				assignDate = Utility.getDateTimeInRequiredFormatFromProvidedDateTime(assignDate, "dd-MMM-yyyy",
						"yyyy-MM-dd'T'HH:mm:ss");
				customerJsonObject.put("assignedDateTime", assignDate);
			}
			
			customerJsonObject.put("id", null);
			customerJsonObject.put("custPackId", null);
			customerJsonObject.put("mvnoId", null);
			customerJsonObject.put("inOutWardMACMapping", null);
			customerJsonObject.put("externalItemId", null);
			
			int serviceAreaId = commonGetAPI.getServiceAreaIdList(customerDetails.get("ServiceArea")).get(0);
			customerJsonObject.put("serviceareaid", serviceAreaId);

			jsonString = customerJsonObject.toJSONString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}
	public boolean checkcustomerUsernameIsAlreadyExists(String customerName) {

		String apiURL = "customer/customerUsernameIsAlreadyExists/" + customerName;
		apiURL = getAPIURL(apiURL);

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("status");

		boolean checkCust = false;
		if (status == 200) {
			checkCust = jsonResponse.getBoolean("isAlreadyExists");
		}
		return checkCust;
	}

	public int getCustomerId(String userName) {

		String jsonString = null;
		org.json.simple.JSONObject searchCustomerJsonObject = new org.json.simple.JSONObject();

	//	ReadData readData = new ReadData();
	//	searchCustomerJsonObject = readData.readJSONFile("SearchPrepaidCustomer.json");

		List<org.json.simple.JSONObject> customerFilterJsonObjectList = new ArrayList<org.json.simple.JSONObject>();
		org.json.simple.JSONObject filterObject = new org.json.simple.JSONObject();

		filterObject.put("filterDataType", "");
		filterObject.put("filterValue", userName);
		filterObject.put("filterColumn", "username");
		filterObject.put("filterOperator", "equalto");
		filterObject.put("filterCondition", "and");

		customerFilterJsonObjectList.add(filterObject);
		searchCustomerJsonObject.put("filters", customerFilterJsonObjectList);
		searchCustomerJsonObject.put("page", 1);
		searchCustomerJsonObject.put("pageSize", 5);

		jsonString = searchCustomerJsonObject.toJSONString();

		String apiURL = getAPIURL("customers/search/Prepaid");
		String APIBody = jsonString;

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		// String response = JSONResponseBody.toString(4);
		// Utility.printLog(logFileName, logModuleName, "Response", response);

		// Fetching the desired value of a parameter
		int status = JSONResponseBody.getInt("status");
		int customerId = 0;

		if (status == 200) {
			JSONArray jsonArray = JSONResponseBody.getJSONArray("customerList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedUserName = jsonArray.getJSONObject(i).getString("username");
				if (receivedUserName.equalsIgnoreCase(userName)) {
					customerId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}

		if (customerId == 0) {
			System.out.println("Customer details not found - " + userName);
			Utility.printLog(logFileName, logModuleName, "Customer details not found - ", userName);
		}

		return customerId;
	}

	
	
}
