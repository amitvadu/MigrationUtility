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
import prepaidplan.PrepaidPlan;
import utility.DBOperations;
import utility.Utility;

public class PrepaidCustomer extends RestExecution {

	private static String logFileName = "prepaidplan.log";
	private static String logModuleName = "CreatePrepaidCustomer";

	private void createPrepaidCustomer(Map<String, String> customerDetailsMap) {

		String apiURL = getAPIURL("customers");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String apiBody = getPrepaidCustomerJson(customerDetailsMap);
		Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

		if (!apiBody.equals(null)) {

			JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
			String response = JSONResponseBody.toString(4);
			Utility.printLog(logFileName, logModuleName, "Response", response);

			// Fetching the desired value of a parameter
			int status = JSONResponseBody.getInt("status");

			if (status == 200) {
				String message = "New Prepaid-Customer is added successfully - " + customerDetailsMap.get("Username");
				System.out.println(message);

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

	public void createPrepaidCustomer(List<Map<String, String>> customerMapList) {

		for (int i = 0; i < customerMapList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map = customerMapList.get(i);

			String userName = map.get("Username");
			if (!checkcustomerUsernameIsAlreadyExists(userName)) {
				Utility.printLog(logFileName, logModuleName, "Sheet Raw Data", map.toString());
				createPrepaidCustomer(map);
			} else {
				System.out.println("Customer UserName is Already Exists! - " + userName);
			}
		}
	}

	public List<Map<String, String>> readUniquePrepaidCustomerList() {

		String sheetName = "PrepaidCustomer";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getPlanDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> customerMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			if (!"".equals(cellValue.get("Username"))) {

				valuemap.put("Title", cellValue.get("Title"));
				valuemap.put("FirstName", cellValue.get("FirstName"));
				valuemap.put("LastName", cellValue.get("LastName"));
				valuemap.put("ContactPerson", cellValue.get("ContactPerson"));
				valuemap.put("Username", cellValue.get("Username"));
				valuemap.put("Password", cellValue.get("Password"));

				valuemap.put("CalendarType", cellValue.get("CalendarType"));
				valuemap.put("Status", cellValue.get("Status"));
				valuemap.put("ParentCustomer", cellValue.get("ParentCustomer"));
				valuemap.put("InvoiceType", cellValue.get("InvoiceType"));

				valuemap.put("CountryCode", cellValue.get("CountryCode"));
				valuemap.put("Mobile", cellValue.get("Mobile"));
				valuemap.put("Email", cellValue.get("Email"));
				valuemap.put("CustomerCategory", cellValue.get("CustomerCategory"));
				valuemap.put("Partner", cellValue.get("Partner"));

				valuemap.put("ServiceArea", cellValue.get("ServiceArea"));
				valuemap.put("Address", cellValue.get("Address"));
				valuemap.put("Municipality", cellValue.get("Municipality"));
				valuemap.put("Ward", cellValue.get("Ward"));

				valuemap.put("PlanCategory", cellValue.get("PlanCategory"));
				valuemap.put("BillTo", cellValue.get("BillTo"));
				valuemap.put("InvoiceToOrganization", cellValue.get("InvoiceToOrganization"));
				valuemap.put("NewOfferPrice", cellValue.get("NewOfferPrice"));
				valuemap.put("Service", cellValue.get("Service"));
				valuemap.put("Plan", cellValue.get("Plan"));
				valuemap.put("DiscountPercentage", cellValue.get("DiscountPercentage"));

				valuemap.put("DirectChargeName", cellValue.get("DirectChargeName"));
				valuemap.put("DirectChargeType", cellValue.get("DirectChargeType"));
				valuemap.put("DirectChargeNewPrice", cellValue.get("DirectChargeNewPrice"));

				valuemap.put("MAC", cellValue.get("MAC"));

				customerMapList.add(valuemap);
			}
		}
		return customerMapList;
	}

	@SuppressWarnings("unchecked")
	private String getPrepaidCustomerJson(Map<String, String> customerDetails) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject customerJsonObject = null;

			ReadData readData = new ReadData();
			customerJsonObject = readData.readJSONFile("CreatePrepaidCustomer.json");

			customerJsonObject.put("title", customerDetails.get("Title"));
			customerJsonObject.put("firstname", customerDetails.get("FirstName"));
			customerJsonObject.put("lastname", customerDetails.get("LastName"));
			customerJsonObject.put("contactperson", customerDetails.get("ContactPerson"));
			customerJsonObject.put("username", customerDetails.get("Username"));
			customerJsonObject.put("password", customerDetails.get("Password"));
			customerJsonObject.put("calendarType", customerDetails.get("CalendarType"));
			customerJsonObject.put("status", customerDetails.get("Status"));

			String parentCustomer = customerDetails.get("ParentCustomer");
			if (!"".equals(parentCustomer)) {
				int parentCustomerId = getCustomerId(parentCustomer);
				if (parentCustomerId != 0) {
					customerJsonObject.put("parentCustomerId", parentCustomerId);
					customerJsonObject.put("invoiceType", customerDetails.get("InvoiceType"));
				}
			}

			customerJsonObject.put("countryCode", "+" + customerDetails.get("CountryCode"));
			customerJsonObject.put("mobile", customerDetails.get("Mobile"));
			customerJsonObject.put("email", customerDetails.get("Email"));
			customerJsonObject.put("dunningCategory", customerDetails.get("CustomerCategory"));
			
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			String partner = customerDetails.get("Partner");
			int partnerId = commonGetAPI.getPartnerId(partner);
			if (partnerId == 0) {
				System.out.println("Customer creation is cancelled because partner details not found" + partner);
				Utility.printLog(logFileName, logModuleName,
						"Customer creation is cancelled because partner details not found", partner);
				return jsonString = null;
			}
			customerJsonObject.put("partnerid", partnerId);

			int serviceAreaId = commonGetAPI.getServiceAreaIdList(customerDetails.get("ServiceArea")).get(0);
			customerJsonObject.put("serviceareaid", serviceAreaId);

			// --PresentAddressDetails

			List<org.json.simple.JSONObject> addressJsonObjectList = new ArrayList<org.json.simple.JSONObject>();
			org.json.simple.JSONObject addressDetailJsonObject = new org.json.simple.JSONObject();

			addressDetailJsonObject.put("addressType", "Present");
			addressDetailJsonObject.put("landmark", customerDetails.get("Address"));

			String wardHeirarchyDetail = getWardHierarchyIdDetails(customerDetails.get("Ward"),
					customerDetails.get("Municipality"));
			String detail[] = wardHeirarchyDetail.split(":");

			int wardId = Integer.parseInt(detail[0]);
			int pincodeId = Integer.parseInt(detail[1]);
			int cityId = Integer.parseInt(detail[2]);
			int stateId = Integer.parseInt(detail[3]);
			int countryId = Integer.parseInt(detail[4]);

			addressDetailJsonObject.put("areaId", wardId);
			addressDetailJsonObject.put("pincodeId", pincodeId);
			addressDetailJsonObject.put("cityId", cityId);
			addressDetailJsonObject.put("stateId", stateId);
			addressDetailJsonObject.put("countryId", countryId);
			addressDetailJsonObject.put("version", "NEW");

			addressJsonObjectList.add(addressDetailJsonObject);

			customerJsonObject.put("addressList", addressJsonObjectList);

			// --PlanMappingDetails
			String billTo = customerDetails.get("BillTo").toUpperCase();
			String invoiceToOrganization = customerDetails.get("InvoiceToOrganization").toUpperCase();
			boolean invoiceToOrg = false;

			customerJsonObject.put("billTo", billTo);
			customerJsonObject.put("discount", 0);

			int planId = commonGetAPI.getPlanId(customerDetails.get("Plan"));
			String planDetails[] = commonGetAPI.getPlanDetails(planId).split(":");

			String serviceName = planDetails[0];
			float offerPrice = Float.valueOf(planDetails[1]);
			int validity = Integer.parseInt(planDetails[2]);
			String unitsOfValidity = planDetails[3];

			float flatAmount = offerPrice;
			float discountPercentage = 0;
			String tempDiscountPercentage = customerDetails.get("DiscountPercentage");

			if ((billTo.equalsIgnoreCase("CUSTOMER")) && (!"".equals(tempDiscountPercentage))) {
				discountPercentage = Float.valueOf(customerDetails.get("DiscountPercentage"));
				flatAmount = offerPrice - (offerPrice * discountPercentage / 100);
				flatAmount = Float.valueOf(Utility.formattedDecimalNumber(flatAmount));
			}

			customerJsonObject.put("flatAmount", flatAmount);

			List<org.json.simple.JSONObject> planJsonObjectList = new ArrayList<org.json.simple.JSONObject>();
			org.json.simple.JSONObject planDetailJsonObject = new org.json.simple.JSONObject();

			planDetailJsonObject.put("newAmount", null);
			float newAmount = 0;

			if (billTo.equalsIgnoreCase("SUBISU")) {
				if (invoiceToOrganization.equalsIgnoreCase("YES")) {
					invoiceToOrg = true;
				}

				String tempNewOfferPrice = customerDetails.get("NewOfferPrice");
				if (!"".equals(tempNewOfferPrice)) {
					newAmount = Float.valueOf(tempNewOfferPrice);
					newAmount = Float.valueOf(Utility.formattedDecimalNumber(newAmount));
					planDetailJsonObject.put("newAmount", newAmount);
				}

			}

			customerJsonObject.put("isInvoiceToOrg", invoiceToOrg);

			planDetailJsonObject.put("planId", planId);
			planDetailJsonObject.put("billTo", billTo);
			planDetailJsonObject.put("service", serviceName);
			planDetailJsonObject.put("validity", validity);
			planDetailJsonObject.put("discount", discountPercentage);
			// planDetailJsonObject.put("newAmount", newAmount);
			planDetailJsonObject.put("offerPrice", offerPrice);
			planDetailJsonObject.put("isInvoiceToOrg", invoiceToOrg);
			planDetailJsonObject.put("istrialplan", null);

			planJsonObjectList.add(planDetailJsonObject);
			customerJsonObject.put("planMappingList", planJsonObjectList);

			// -- Over Direct Charge Mapping

			List<org.json.simple.JSONObject> chargeJsonObjectList = new ArrayList<org.json.simple.JSONObject>();
			String directChargeName = customerDetails.get("DirectChargeName");

			if (!"".equals(directChargeName)) {

				float directChargeNewPrice = Float.valueOf(customerDetails.get("DirectChargeNewPrice"));
				//CommonGetAPI commonGetAPI = new CommonGetAPI();
				int chargeId = commonGetAPI.getChargeId(directChargeName);

				PrepaidPlan prepaidPlan = new PrepaidPlan();
				String taxAmountAndActualPrice = prepaidPlan.getChargeByIdAndTaxAmountAndActualPrice(chargeId);
				String ans[] = taxAmountAndActualPrice.split(":");
				// float taxamount = Float.parseFloat(ans[0]);
				float actualprice = Float.parseFloat(ans[1]);

				org.json.simple.JSONObject chargeJsonObject = new org.json.simple.JSONObject();

				String currentDate = Utility.getCurrentDateTimeByProvidedFormat("yyyy-MM-dd");

				chargeJsonObject.put("actualprice", actualprice);
				chargeJsonObject.put("billingCycle", null);
				chargeJsonObject.put("charge_date", currentDate);
				chargeJsonObject.put("chargeid", chargeId);
				chargeJsonObject.put("id", null);

				chargeJsonObject.put("planid", planId);
				chargeJsonObject.put("price", directChargeNewPrice);
				chargeJsonObject.put("type", "One-time");
				chargeJsonObject.put("unitsOfValidity", unitsOfValidity);
				chargeJsonObject.put("validity", validity);

				chargeJsonObjectList.add(chargeJsonObject);
			}
			customerJsonObject.put("overChargeList", chargeJsonObjectList);

			// --Customer MAC Addresses Mapping

			List<org.json.simple.JSONObject> macJsonObjectList = new ArrayList<org.json.simple.JSONObject>();
			String macList = customerDetails.get("MAC");

			if (!"".equals(macList)) {

				String macArray[] = customerDetails.get("MAC").split(",");

				for (int i = 0; i < macArray.length; i++) {
					org.json.simple.JSONObject macObject = new org.json.simple.JSONObject();
					macObject.put("macAddress", macArray[i]);
					macJsonObjectList.add(macObject);
				}
			}
			customerJsonObject.put("custMacMapppingList", macJsonObjectList);

			jsonString = customerJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	public String getWardHierarchyIdDetails(String wardName, String municipalityName) {

		String apiURL = getAPIURL("area/all");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		// Fetching the desired value of a parameter
		int status = jsonResponse.getInt("responseCode");
		String detail = "";

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {

				String receivedMunicipalityName = jsonArray.getJSONObject(i).getString("code").trim();
				String receivedWardName = jsonArray.getJSONObject(i).getString("name").trim();

				if (receivedMunicipalityName.equalsIgnoreCase(municipalityName)) {
					if (receivedWardName.equalsIgnoreCase(wardName)) {
						int wardId = jsonArray.getJSONObject(i).getInt("id");
						int pincodeId = jsonArray.getJSONObject(i).getInt("pincodeId");
						int cityId = jsonArray.getJSONObject(i).getInt("cityId");
						int stateId = jsonArray.getJSONObject(i).getInt("stateId");
						int countryId = jsonArray.getJSONObject(i).getInt("countryId");
						detail = wardId + ":" + pincodeId + ":" + cityId + ":" + stateId + ":" + countryId;
						break;
					}
				}
			}
		}

		if ("".equals(detail)) {
			System.out.println("Ward details not found - " + wardName);
			Utility.printLog(logFileName, logModuleName, "Ward details not found - ", wardName);
		}

		return detail;
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
