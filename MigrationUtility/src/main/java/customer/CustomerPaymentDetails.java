package customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonList;
import utility.DBOperations;
import utility.Utility;

public class CustomerPaymentDetails extends RestExecution {

	private static String logFileName = "PaymentDetails.log";
	private static String logModuleName = "RecordPayment";

	private void recordCustomerPaymentDetails(Map<String, String> customerDetailsMap) {

		String apiURL = getAPIURL("record/payment");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getPrepaidCustomerJson(customerDetailsMap);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		if (!APIBody.equals(null)) {

			JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
			String response = JSONResponseBody.toString(4);
			Utility.printLog(logFileName, logModuleName, "Response", response);

			// Fetching the desired value of a parameter
			int status = JSONResponseBody.getInt("status");

			if (status == 200) {
				System.out
						.println("New Prepaid-Customer is added successfully - " + customerDetailsMap.get("Username"));

				String type = "PrepaidCustomer";
				JSONObject cityJSONObject = JSONResponseBody.getJSONObject("customer");
				String name = cityJSONObject.getString("username");
				int id = cityJSONObject.getInt("id");

				DBOperations dbo = new DBOperations();
				dbo.setAPIData(type, name, id, status);
			} else if (status == 406) {
				String error = JSONResponseBody.getString("ERROR");
				System.out.println(error + " - " + customerDetailsMap.get("Username"));
			}
		}
	}

	public void recordCustomerPaymentDetails(List<Map<String, String>> customerMapList) {

		for (int i = 0; i < customerMapList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map = customerMapList.get(i);

			String userName = map.get("CustomerUsername");
			if (checkcustomerUsernameIsAlreadyExists(userName)) {
				Utility.printLog(logFileName, logModuleName, "Sheet Raw Data", map.toString());
				recordCustomerPaymentDetails(map);
			} else {
				System.out.println("Customer UserName is not Exists! - " + userName);
			}
		}
	}

	public List<Map<String, String>> readUniqueCustomerPaymentDetailsList() {

		String sheetName = "PaymentDetails";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getPlanDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> customerMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			if (!"".equals(cellValue.get("CustomerUsername"))) {

				valuemap.put("CustomerUsername", cellValue.get("CustomerUsername"));
				valuemap.put("PaymentMode", cellValue.get("PaymentMode"));
				valuemap.put("Amount", cellValue.get("Amount"));
				valuemap.put("BarterAmount", cellValue.get("BarterAmount"));
				valuemap.put("PaymentReferenceNumber", cellValue.get("PaymentReferenceNumber"));
				
				valuemap.put("ChequeNumber", cellValue.get("ChequeNumber"));
				valuemap.put("ChequeDate", cellValue.get("ChequeDate"));
				valuemap.put("Bank", cellValue.get("Bank"));
				valuemap.put("Bank1", cellValue.get("Bank1"));
				valuemap.put("Branch", cellValue.get("Branch"));

				valuemap.put("ReferenceNumber", cellValue.get("ReferenceNumber"));
				valuemap.put("ReceiptNumber", cellValue.get("ReceiptNumber"));
				valuemap.put("Remark", cellValue.get("Remark"));

				customerMapList.add(valuemap);
			}
		}
		return customerMapList;
	}

	@SuppressWarnings("unchecked")
	private String getPrepaidCustomerJson(Map<String, String> customerDetails) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject customerJsonObject = new org.json.simple.JSONObject();

			//ReadData readData = new ReadData();
			//customerJsonObject = readData.readJSONFile("CreatePrepaidCustomer.json");
			
			String userName = customerDetails.get("CustomerUsername");
			if (!"".equals(userName)) {
				int customerId = getCustomerId(userName);
				if (customerId != 0) {
					customerJsonObject.put("customerid",customerId);
					
					String paymentMode = customerDetails.get("PaymentMode");
					
					CommonList commonList = new CommonList();
					String commonPaymentMode = commonList.getCommonPaymentMode(paymentMode);
					customerJsonObject.put("paymode", commonPaymentMode);
					
					int invoiceId = getCustomerInvoiceId(customerId);
					List<Integer> invoiceList = new ArrayList<Integer>();
					invoiceList.add(invoiceId);
					customerJsonObject.put("invoiceId", invoiceList);
				}
			}
			
			customerJsonObject.put("amount", Float.valueOf(customerDetails.get("Amount")));			
			customerJsonObject.put("referenceno", customerDetails.get("ReferenceNumber"));
			customerJsonObject.put("reciptNo", customerDetails.get("ReceiptNumber"));
			customerJsonObject.put("remark", customerDetails.get("Remark"));
			
			customerJsonObject.put("type", "Payment");
			customerJsonObject.put("paytype", "invoice");
			customerJsonObject.put("tdsAmount", 0);
			customerJsonObject.put("abbsAmount", 0);
			
			jsonString = customerJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}


	public int getCustomerInvoiceId(int customerId) {

		String apiURL = "invoiceList/byCustomer/" + customerId;
		apiURL = getAPIURL(apiURL);

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		// Fetching the desired value of a parameter
		int status = jsonResponse.getInt("status");
		int invoiceId = 0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("invoiceList");
					invoiceId = jsonArray.getJSONObject(0).getInt("id");
		}

		if (invoiceId == 0) {
			System.out.println("Customer Invoice details not found - " + customerId);
			Utility.printLog(logFileName, logModuleName, "Customer Invoice details not found - ", String.valueOf(customerId));
		}

		return invoiceId;
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
		org.json.simple.JSONObject searchCustomerJsonObject = null;

		ReadData readData = new ReadData();
		searchCustomerJsonObject = readData.readJSONFile("SearchPrepaidCustomer.json");

		List<org.json.simple.JSONObject> customerFilterJsonObjectList = new ArrayList<org.json.simple.JSONObject>();
		org.json.simple.JSONObject filterObject = new org.json.simple.JSONObject();

		filterObject.put("filterDataType", "");
		filterObject.put("filterValue", userName);
		filterObject.put("filterColumn", "username");
		filterObject.put("filterOperator", "equalto");
		filterObject.put("filterCondition", "and");

		customerFilterJsonObjectList.add(filterObject);
		searchCustomerJsonObject.put("filters", customerFilterJsonObjectList);

		jsonString = searchCustomerJsonObject.toJSONString();

		String apiURL = "customers/search/Prepaid";
		apiURL = getAPIURL(apiURL);
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
