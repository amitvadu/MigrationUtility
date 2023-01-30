package productdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import commons.CommonList;
import utility.Utility;

public class PrepaidPlan extends RestExecution {

	String logFileName = "prepaidplan.log";
	String logModuleName = "CreatePrepaidPlan";

	private void createPrepaidPlan(Map<String, String> planDetailMap) {

		String apiURL = getAPIURL("postpaidplan");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getPrepaidPlanJson(planDetailMap);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		if (!APIBody.equals(null)) {

			JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
			String response = JSONResponseBody.toString(4);
			Utility.printLog(logFileName, logModuleName, "Response", response);

			int status = JSONResponseBody.getInt("status");
			String planName = planDetailMap.get("PlanName");

			if (status == 200) {
				String message = "New PrepaidPlan is added successfully - " + planName;
				System.out.println(message);
				Utility.printLog("execution.log", logModuleName, "Success", message);

			} else if (status == 406) {
				String error = JSONResponseBody.getString("ERROR") + " - " + planName;
				System.out.println(error);
				Utility.printLog("execution.log", logModuleName, "Already Exist", error);
				
			} else {
				String error = JSONResponseBody.get("ERROR") + " - " + planName;
				System.out.println(error);
				Utility.printLog("execution.log", logModuleName, "ERROR", error);				
			}
		}
	}

	public void createPrepaidPlan(List<Map<String, String>> planMapList) {

		for (int i = 0; i < planMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = planMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createPrepaidPlan(map);
		}
	}

	public List<Map<String, String>> readUniquePrepaidPlanList() {

		String sheetName = "Plan";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getPlanDataSheet(sheetName);
		
		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> planMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String planName = cellValue.get("PlanName");
			if ((!"".equals(planName)) && (planName != null)) {

				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("PlanName", cellValue.get("PlanName"));
				valuemap.put("DisplayName", cellValue.get("DisplayName"));
				valuemap.put("Code", cellValue.get("Code"));
				valuemap.put("Type", cellValue.get("Type"));
				valuemap.put("Category", cellValue.get("Category"));
				valuemap.put("Mode", cellValue.get("Mode"));
				valuemap.put("Group", cellValue.get("Group"));

				valuemap.put("Service", cellValue.get("Service"));
				valuemap.put("ServiceArea", cellValue.get("ServiceArea"));
				valuemap.put("Accessibility", cellValue.get("Accessibility"));
				valuemap.put("StartDate", cellValue.get("StartDate"));
				valuemap.put("EndDate", cellValue.get("EndDate"));

				valuemap.put("Validity", cellValue.get("Validity"));
				valuemap.put("ValidityUnit", cellValue.get("ValidityUnit"));
				valuemap.put("Status", cellValue.get("Status"));

				valuemap.put("AllowOverUsage", cellValue.get("AllowOverUsage"));
				valuemap.put("MaxCurrentSession", cellValue.get("MaxCurrentSession"));
				valuemap.put("AllowDiscount", cellValue.get("AllowDiscount"));
				valuemap.put("Description", cellValue.get("Description"));

				valuemap.put("QuotaType", cellValue.get("QuotaType"));
				valuemap.put("QuotaTime", cellValue.get("QuotaTime"));
				valuemap.put("QuotaUnitTime", cellValue.get("QuotaUnitTime"));
				valuemap.put("QuotaData", cellValue.get("QuotaData"));
				valuemap.put("QuotaUnitData", cellValue.get("QuotaUnitData"));
				valuemap.put("QuotaResetInterval", cellValue.get("QuotaResetInterval"));

				valuemap.put("QosPolicy", cellValue.get("QosPolicy"));
				valuemap.put("TimeBasePolicy", cellValue.get("TimeBasePolicy"));
				valuemap.put("ChargeName", cellValue.get("ChargeName"));
				valuemap.put("NewOfferPrice", cellValue.get("NewOfferPrice"));
				valuemap.put("PostpaidBilingCycle", cellValue.get("PostpaidBilingCycle"));

				planMapList.add(valuemap);
			}
		}
		return planMapList;
	}

	private String getPrepaidPlanJson(Map<String, String> planDetails) {

		String jsonString = null;

		try {

			CommonGetAPI commonGetAPI = new CommonGetAPI();
			CommonList commonList = new CommonList();
			JSONObject planJson = new JSONObject();

			// -- Prepaid Plan Information
			
			planJson.put("name", planDetails.get("PlanName"));
			planJson.put("displayName", planDetails.get("DisplayName"));
			planJson.put("code", planDetails.get("Code"));
			planJson.put("plantype", planDetails.get("Type"));

			planJson.put("category", planDetails.get("Category"));
			planJson.put("mode", planDetails.get("Mode").toUpperCase());
			planJson.put("planGroup", commonList.getCommonPlanGroup(planDetails.get("Group")));
			
			planJson.put("serviceId", commonGetAPI.getServiceIdList(planDetails.get("Service")).get(0));
			planJson.put("serviceAreaIds", commonGetAPI.getServiceAreaIdList(planDetails.get("ServiceArea")));

			String accessibility = planDetails.get("Accessibility");
			if(("".equals(accessibility)) || (accessibility == null)) {
				accessibility = null;
			} else {
				accessibility = commonList.getCommonPlanAccessibility(accessibility);
			}			
			planJson.put("accessibility", accessibility);
			
			String startDate = planDetails.get("StartDate");
			String endDate = planDetails.get("EndDate");
			startDate = Utility.getDateTimeInRequiredFormatFromProvidedDateTime(startDate, "dd-MMM-yyyy", "yyyy-MM-dd");
			endDate = Utility.getDateTimeInRequiredFormatFromProvidedDateTime(endDate, "dd-MMM-yyyy", "yyyy-MM-dd");
			planJson.put("startDate", startDate);
			planJson.put("endDate", endDate);

			planJson.put("validity", Integer.parseInt(planDetails.get("Validity")));
			planJson.put("unitsOfValidity", planDetails.get("ValidityUnit"));
			planJson.put("status", planDetails.get("Status").toUpperCase());

			planJson.put("allowOverUsage", Boolean.valueOf(planDetails.get("AllowOverUsage")));
			planJson.put("maxconcurrentsession", Integer.parseInt(planDetails.get("MaxCurrentSession")));
			
			String allowDiscount = planDetails.get("AllowDiscount");
			boolean allowDiscount1 = false;
			if((!"".equals(allowDiscount)) && (allowDiscount != null)) {
				if(allowDiscount.equalsIgnoreCase("Yes")) {
					allowDiscount1 = true;
				}
			}
			planJson.put("allowdiscount", allowDiscount1);
			planJson.put("desc", planDetails.get("Description"));

			// -- PrepaidPlan Quota Details
			
			String quotaType = planDetails.get("QuotaType");
			planJson.put("quotatype", quotaType);

			if ((quotaType.equalsIgnoreCase("Time")) || (quotaType.equalsIgnoreCase("Both"))) {
				planJson.put("quotatime", Integer.parseInt(planDetails.get("QuotaTime")));
				planJson.put("quotaunittime", planDetails.get("QuotaUnitTime").toUpperCase());
			}

			if ((quotaType.equalsIgnoreCase("Data")) || (quotaType.equalsIgnoreCase("Both"))) {
				planJson.put("quota", Integer.parseInt(planDetails.get("QuotaData")));
				planJson.put("quotaUnit", planDetails.get("QuotaUnitData"));
			}
			planJson.put("quotaResetInterval", planDetails.get("QuotaResetInterval"));

			// -- PrepaidPlan Additional Information
			String sacCode = null;
			planJson.put("saccode", sacCode);
			planJson.put("qospolicyid", commonGetAPI.getQosPolicyId(planDetails.get("QosPolicy")));

			String timeBasePolicy = planDetails.get("TimeBasePolicy");
			if (!"".equals(timeBasePolicy)) {
				planJson.put("timebasepolicyId",
						commonGetAPI.getTimeBasePolicyId(planDetails.get("TimeBasePolicy")));
			} else {
				planJson.put("timebasepolicyId", "");
			}

			String param1 = null;
			String param2 = null;
			String param3 = null;
			
			planJson.put("param1", param1);
			planJson.put("param2", param2);
			planJson.put("param3", param3);
			
			// -- PrepaidPlan Charge Details
			
			String planType = planDetails.get("Type");
			String billingCycle = "1";
			if (planType.equalsIgnoreCase("Postpaid")) {
				billingCycle = planDetails.get("PostpaidBilingCycle");
			}
			
			List<JSONObject> chargeJsonObjectList = new ArrayList<JSONObject>();

			float offerprice = 0.0f;
			String chargeNames[] = planDetails.get("ChargeName").split(",");

			for (int i = 0; i < chargeNames.length; i++) {

				String tempChargeName = chargeNames[i];

				int chargeId = commonGetAPI.getChargeId(tempChargeName);
				String taxAmountAndActualPrice = getChargeByIdAndTaxAmountAndActualPrice(chargeId);
				String ans[] = taxAmountAndActualPrice.split(":");
				float taxamount = Float.parseFloat(ans[0]);
				float actualprice = Float.parseFloat(ans[1]);

				offerprice = offerprice + taxamount + actualprice;

				JSONObject chargeListObject = new JSONObject();
				JSONObject chargeObject = new JSONObject();

				chargeObject.put("id", chargeId);
				chargeObject.put("actualprice", actualprice);
				chargeObject.put("taxamount", taxamount);

				chargeListObject.put("billingCycle", billingCycle);
				chargeListObject.put("charge", chargeObject);
				chargeJsonObjectList.add(chargeListObject);

			}

			String tempOfferPrice = Utility.formattedDecimalNumber(offerprice);
			planJson.put("offerprice", tempOfferPrice);
			planJson.put("chargeList", chargeJsonObjectList);

			String planCategory = planDetails.get("Category");
			if (planCategory.equalsIgnoreCase("Business Promotion")) {
				String newOfferPrice = planDetails.get("NewOfferPrice");
				if (!"".equals(newOfferPrice)) {
					float tempNewOfferPrice = Float.valueOf(newOfferPrice);
					String strNewOfferPrice = Utility.formattedDecimalNumber(tempNewOfferPrice);
					planJson.put("newOfferPrice", strNewOfferPrice);
				} else {
					planJson.put("newOfferPrice", tempOfferPrice);
				}

			}
			
			// -- PrepaidPlan Product Details
			
			List<org.json.simple.JSONObject> productPlanMappingList = new ArrayList<org.json.simple.JSONObject>();
			
			String productCategory = null;
			String productType = null;
			String productsType = null;
			
			planJson.put("product_category", productCategory);
			planJson.put("product_type", productType);
			planJson.put("ProductsType", productsType);
			planJson.put("productplanmappingList", productPlanMappingList);

			jsonString = planJson.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	public String getChargeByIdAndTaxAmountAndActualPrice(int chargeId) {

		String tempURL = "charge/" + chargeId;
		String apiURL = getAPIURL(tempURL);

		JSONObject jsonResponse = httpGet(apiURL);

		int status = jsonResponse.getInt("status");
		String taxAmountAndActualPrice = "";

		if (status == 200) {
			JSONObject jsonObject = jsonResponse.getJSONObject("chargebyid");

			int taxAmount = jsonObject.getInt("taxamount");
			int actualprice = jsonObject.getInt("actualprice");
			taxAmountAndActualPrice = taxAmount + ":" + actualprice;
		}

		if ("".equals(taxAmountAndActualPrice)) {
			System.out.println("Taxamount and Actualprice details not found from chargeId - " + chargeId);
			Utility.printLog(logFileName, logModuleName, "Taxamount and Actualprice details not found from chargeId - ",
					String.valueOf(chargeId));
		}

		return taxAmountAndActualPrice;
	}

}
