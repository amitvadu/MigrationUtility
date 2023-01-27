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
import productdata.PrepaidPlan;
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
				int no = Utility.elapsedTime(-1, logModuleName, logFileName);
				Utility.printLog(logFileName, logModuleName, "Sheet Raw Data", map.toString());
				
				createPrepaidCustomer(map);
				Utility.elapsedTime(no, logModuleName, logFileName);
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
			
			String userName = cellValue.get("Username");
			if ((!"".equals(userName)) && (userName != null)) {
				
				valuemap.put("RowIndex", cellValue.get("RowIndex"));
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
				valuemap.put("PlanGroup", cellValue.get("PlanGroup"));
				valuemap.put("[PlanName:NewOfferPrice]", cellValue.get("[PlanName:NewOfferPrice]"));				
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

			org.json.simple.JSONObject customerJsonObject = new org.json.simple.JSONObject();

			//ReadData readData = new ReadData();
			//customerJsonObject = readData.readJSONFile("CreatePrepaidCustomer.json");
			
			customerJsonObject.put("custtype", "Prepaid");
			
			customerJsonObject.put("title", customerDetails.get("Title"));
			customerJsonObject.put("firstname", customerDetails.get("FirstName"));
			customerJsonObject.put("lastname", customerDetails.get("LastName"));
			customerJsonObject.put("contactperson", customerDetails.get("ContactPerson"));
			
			customerJsonObject.put("cafno", null);
			customerJsonObject.put("username", customerDetails.get("Username"));
			customerJsonObject.put("password", customerDetails.get("Password"));
			customerJsonObject.put("calendarType", customerDetails.get("CalendarType"));
			
			
			customerJsonObject.put("branch", null);
			customerJsonObject.put("status", customerDetails.get("Status"));

			customerJsonObject.put("failcount", 0);
			customerJsonObject.put("isCustCaf", null);
			customerJsonObject.put("customerArea", null);
			customerJsonObject.put("servicetype", "");
			customerJsonObject.put("parentCustomerId", null);
			
			String parentCustomer = customerDetails.get("ParentCustomer");
			if (!"".equals(parentCustomer)) {
				int parentCustomerId = getCustomerId(parentCustomer);
				if (parentCustomerId != 0) {
					customerJsonObject.put("parentCustomerId", parentCustomerId);
					customerJsonObject.put("invoiceType", customerDetails.get("InvoiceType"));
				}
			}
			
			customerJsonObject.put("popid", null);
			customerJsonObject.put("valleyType", null);
			
			//-- Customer KYC Details --
			
			customerJsonObject.put("gst", "");
			customerJsonObject.put("pan", "");
			customerJsonObject.put("aadhar", "");
			customerJsonObject.put("passportNo", "");
			customerJsonObject.put("tinNo", null);
			
			//-- Customer Contact Details --
			
			customerJsonObject.put("countryCode", "+" + customerDetails.get("CountryCode"));
			customerJsonObject.put("mobile", customerDetails.get("Mobile"));
			customerJsonObject.put("phone", "");
			customerJsonObject.put("email", customerDetails.get("Email"));
			customerJsonObject.put("dunningCategory", customerDetails.get("CustomerCategory"));
			customerJsonObject.put("dunningType", null);
			customerJsonObject.put("dunningSector", null);
			
			
			// -- Customer Subscriber connection & Network Details
			
			customerJsonObject.put("latitude", null);
			customerJsonObject.put("longitude", null);
			
			// -- Customer Business Partner Details
			
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
			customerJsonObject.put("salesremark", "");
			
			// -- Customer Payment Details --
			org.json.simple.JSONObject paymentJson = new org.json.simple.JSONObject();
			paymentJson.put("amount", 0);
			paymentJson.put("paymode", null);
			paymentJson.put("referenceno", null);
			paymentJson.put("paymentdate", null);
			
			customerJsonObject.put("paymentDetails", paymentJson);
			
			// -- Customer Present Address Details --
			
			int serviceAreaId = commonGetAPI.getServiceAreaIdList(customerDetails.get("ServiceArea")).get(0);
			customerJsonObject.put("serviceareaid", serviceAreaId);

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
			customerJsonObject.put("istrialplan", false);
			String planCategory = customerDetails.get("PlanCategory");
			
			// --Individual Plan
			if(planCategory.equalsIgnoreCase("Individual")) {
				
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
				//String unitsOfValidity = planDetails[3];

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
				customerJsonObject.put("isInvoiceToOrg", invoiceToOrg);
			}
			
						
			// --Plan Group
			customerJsonObject.put("plangroupid", null);
			
			if(planCategory.equalsIgnoreCase("Plan Group")) {
				
				
				String billTo = customerDetails.get("BillTo").toUpperCase();
				String invoiceToOrganization = customerDetails.get("InvoiceToOrganization").toUpperCase();
				boolean invoiceToOrg = false;
				boolean istrialplan = false;

				customerJsonObject.put("billTo", billTo);
				customerJsonObject.put("discount", 0);

				String planGroup = customerDetails.get("PlanGroup");
				String planGroupDetails[] = getPlanBundleDetails(planGroup).split(":");

				int planGroupId = Integer.parseInt(planGroupDetails[0]);
				float offerPrice = Float.valueOf(planGroupDetails[1]);

				float flatAmount = 0;
				float discountPercentage = 0;
				String tempDiscountPercentage = customerDetails.get("DiscountPercentage");

				customerJsonObject.put("plangroupid", planGroupId);
				
				if ((billTo.equalsIgnoreCase("CUSTOMER")) && (!"".equals(tempDiscountPercentage))) {
					flatAmount = offerPrice;
					discountPercentage = Float.valueOf(customerDetails.get("DiscountPercentage"));
					flatAmount = offerPrice - (offerPrice * discountPercentage / 100);
					flatAmount = Float.valueOf(Utility.formattedDecimalNumber(flatAmount));
					customerJsonObject.put("discount", discountPercentage);
					customerJsonObject.put("flatAmount", flatAmount);
				}
				
				
				if (billTo.equalsIgnoreCase("SUBISU")) {
					
					if (invoiceToOrganization.equalsIgnoreCase("YES")) {
						invoiceToOrg = true;
					}
					
					customerJsonObject.put("discount", discountPercentage);
					customerJsonObject.put("flatAmount", flatAmount);
					
					List<org.json.simple.JSONObject> planJsonObjectList = new ArrayList<org.json.simple.JSONObject>();
					
					String planNameNewOfferPrice = customerDetails.get("[PlanName:NewOfferPrice]");
					planNameNewOfferPrice = planNameNewOfferPrice.replaceAll("[\\[\\]]", "");
					
					String ans[] = planNameNewOfferPrice.split(",");

					for (int i = 0; i < ans.length; i++) {

						String planNameNewOfferDetails[] = ans[i].split(":");
						String planName = planNameNewOfferDetails[0];
						float newOffer = Float.valueOf(planNameNewOfferDetails[1]);

						org.json.simple.JSONObject planDetailJsonObject = new org.json.simple.JSONObject();

						int planId = commonGetAPI.getPlanId(planName);
						String planDetails[] = commonGetAPI.getPlanDetails(planId).split(":");

						String serviceName = planDetails[0];
						offerPrice = Float.valueOf(planDetails[1]);
						int validity = Integer.parseInt(planDetails[2]);
						//String unitsOfValidity = planDetails[3];
						
						planDetailJsonObject.put("planId", planId);
						planDetailJsonObject.put("name", planName);
						planDetailJsonObject.put("service", serviceName);
						planDetailJsonObject.put("validity", validity);
						planDetailJsonObject.put("billTo", billTo);
						planDetailJsonObject.put("discount", discountPercentage);
						planDetailJsonObject.put("newAmount", newOffer);
						planDetailJsonObject.put("offerPrice", offerPrice);
							
						planDetailJsonObject.put("chargeName", "");
						planDetailJsonObject.put("isInvoiceToOrg", invoiceToOrg);

						
						planJsonObjectList.add(planDetailJsonObject);
					}
					customerJsonObject.put("planMappingList", planJsonObjectList);
				}
				
				customerJsonObject.put("istrialplan", istrialplan);
				customerJsonObject.put("isInvoiceToOrg", invoiceToOrg);
			}
			
			
			// -- Customer Additional Service Details
			
			customerJsonObject.put("voicesrvtype", "");
			customerJsonObject.put("didno", "");

			// -- Radius Service Details
			
			customerJsonObject.put("nasPort", null);
			customerJsonObject.put("framedIp", null);
			
			// -- Over Direct Charge Mapping

			List<org.json.simple.JSONObject> chargeJsonObjectList = new ArrayList<org.json.simple.JSONObject>();
			String directChargeName = customerDetails.get("DirectChargeName");

			if (!"".equals(directChargeName)) {
				
				int planId = commonGetAPI.getPlanId(customerDetails.get("Plan"));
				String planDetails[] = commonGetAPI.getPlanDetails(planId).split(":");

				//String serviceName = planDetails[0];
				//float offerPrice = Float.valueOf(planDetails[1]);
				int validity = Integer.parseInt(planDetails[2]);
				String unitsOfValidity = planDetails[3];

				

				float directChargeNewPrice = Float.valueOf(customerDetails.get("DirectChargeNewPrice"));
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

	@SuppressWarnings("unchecked")
	public int getCustomerId(String userName) {

		String jsonString = null;
		org.json.simple.JSONObject searchCustomerJson = new org.json.simple.JSONObject();

		//ReadData readData = new ReadData();
		//searchCustomerJsonObject = readData.readJSONFile("SearchPrepaidCustomer.json");

		List<org.json.simple.JSONObject> customerFilterJsonObjectList = new ArrayList<org.json.simple.JSONObject>();
		org.json.simple.JSONObject filterObject = new org.json.simple.JSONObject();

		filterObject.put("filterDataType", "");
		filterObject.put("filterValue", userName);
		filterObject.put("filterColumn", "username");
		filterObject.put("filterOperator", "equalto");
		filterObject.put("filterCondition", "and");

		customerFilterJsonObjectList.add(filterObject);
		searchCustomerJson.put("filters", customerFilterJsonObjectList);
		
		searchCustomerJson.put("page", 1);
		searchCustomerJson.put("pageSize", 5);

		jsonString = searchCustomerJson.toJSONString();

		String apiURL = getAPIURL("customers/search/Prepaid");
		String APIBody = jsonString;

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
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

	public String getPlanBundleDetails(String planGroupName) {

		String apiURL = getAPIURL("findPlanGroupByName?name=" + planGroupName);

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("status");
		String detail = "";

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("planGroupList");
			for (int i = 0; i < jsonArray.length(); i++) {

				String receivedPlanGroupName = jsonArray.getJSONObject(i).getString("planGroupName").trim();
				if (receivedPlanGroupName.equalsIgnoreCase(planGroupName)) {
					int planGroupId = jsonArray.getJSONObject(i).getInt("planGroupId");
					JSONArray planMappingListArray = jsonArray.getJSONObject(i).getJSONArray("planMappingList");
					float planGroupOfferprice = 0.0f;
					
					for (int j = 0; j < planMappingListArray.length(); j++) {
						float offerprice = planMappingListArray.getJSONObject(j).getJSONObject("plan").getFloat("offerprice");
						planGroupOfferprice=planGroupOfferprice+offerprice;
					}

					detail = planGroupId + ":" + planGroupOfferprice;
					break;
				}
			}
		}

		if ("".equals(detail)) {
			System.out.println("Plan-Group details not found - " + planGroupName);
			Utility.printLog(logFileName, logModuleName, "Plan-Group details not found - ", planGroupName);
		}

		return detail;
	}


	
}
