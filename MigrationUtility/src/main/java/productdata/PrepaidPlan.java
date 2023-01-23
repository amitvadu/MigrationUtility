package productdata;

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

public class PrepaidPlan extends RestExecution {
	
	String logFileName = "prepaidplan.log";
	String logModuleName = "CreatePrepaidPlan";

	private void createPrepaidPlan(Map<String, String> planDetailMap){
		
		String apiURL=getAPIURL("postpaidplan");
		Utility.printLog(logFileName,logModuleName , "Request URL", apiURL);
		
		//Initializing payload or API body		  
		 String APIBody = getPrepaidPlanJson(planDetailMap);
		 Utility.printLog(logFileName,logModuleName , "Request Body", APIBody);
		 
		 if(!APIBody.equals(null)) {
			 
			 JSONObject JSONResponseBody = httpPost(apiURL,APIBody);
			 String response = JSONResponseBody.toString(4);
			 Utility.printLog(logFileName,logModuleName , "Response", response);
			 		 
	
			 int status = JSONResponseBody.getInt("status");

			 if(status==200) {
				System.out.println("New PrepaidPlan is added successfully - " + planDetailMap.get("PlanName"));
				
				String type = "PrepaidPlan";
				JSONObject cityJSONObject = JSONResponseBody.getJSONObject("postpaidplan");
				String name = cityJSONObject.getString("name");
				int id = cityJSONObject.getInt("id");

				DBOperations dbo = new DBOperations();
				dbo.setAPIData(type,name,id,status);
				
			 } else if (status == 406) {
					String error = JSONResponseBody.getString("ERROR");
					System.out.println(error + " - " + planDetailMap.get("PlanName"));
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

	public  List<Map<String, String>> readUniquePrepaidPlanList() {
		
		String sheetName = "Plan";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getPlanDataSheet(sheetName);
	
		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> planMapList = new ArrayList<Map<String, String>>();
		
		for (int i = 0; i < sheetMap.size(); i++) {
			
			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			if(!"".equals(cellValue.get("PlanName"))) {
			
				valuemap.put("PlanName", cellValue.get("PlanName"));
				valuemap.put("DisplayName", cellValue.get("DisplayName"));
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

	@SuppressWarnings("unchecked")
	private String getPrepaidPlanJson(Map<String, String> planDetails) {
		
		String jsonString = null;
		
		try {
			
			CommonGetAPI commonGetAPI = new CommonGetAPI();
			org.json.simple.JSONObject planJsonObject = null;
			
			ReadData readData = new ReadData();			
			planJsonObject = readData.readJSONFile("CreatePrepaidPlan.json");
			
			planJsonObject.put("name", planDetails.get("PlanName"));
			planJsonObject.put("displayName", planDetails.get("DisplayName"));			
			planJsonObject.put("plantype", planDetails.get("Type"));
			
			planJsonObject.put("category", planDetails.get("Category"));
			planJsonObject.put("mode", planDetails.get("Mode").toUpperCase());
			planJsonObject.put("planGroup", planDetails.get("Group"));
			
			planJsonObject.put("serviceId", commonGetAPI.getServiceIdList(planDetails.get("Service")).get(0));
			planJsonObject.put("serviceAreaIds", commonGetAPI.getServiceAreaIdList(planDetails.get("ServiceArea")));
			
			String accessibility = planDetails.get("Accessibility");
			if("".equals(accessibility)) {
				accessibility=null;
			}
			planJsonObject.put("accessibility", accessibility);
			
			String startDate = planDetails.get("StartDate");
			String endDate =  planDetails.get("EndDate");
			startDate = Utility.getDateTimeInRequiredFormatFromProvidedDateTime(startDate, "dd-MMM-yyyy", "yyyy-MM-dd");
			endDate = Utility.getDateTimeInRequiredFormatFromProvidedDateTime(endDate, "dd-MMM-yyyy", "yyyy-MM-dd");			
			planJsonObject.put("startDate", startDate);			
			planJsonObject.put("endDate", endDate);
			
			planJsonObject.put("validity",Float.valueOf(planDetails.get("Validity")).intValue());
			planJsonObject.put("unitsOfValidity", planDetails.get("ValidityUnit"));
			planJsonObject.put("status", planDetails.get("Status").toUpperCase());
			
			planJsonObject.put("allowOverUsage", Boolean.valueOf(planDetails.get("AllowOverUsage")));
			planJsonObject.put("maxconcurrentsession", Float.valueOf(planDetails.get("MaxCurrentSession")).intValue());			
			planJsonObject.put("desc", planDetails.get("Description"));
			
			String quotaType = planDetails.get("QuotaType");			
			planJsonObject.put("quotatype", quotaType);	
			
			if((quotaType.equalsIgnoreCase("Time")) || (quotaType.equalsIgnoreCase("Both"))) {
				planJsonObject.put("quotatime",Float.valueOf(planDetails.get("QuotaTime")).intValue());
				planJsonObject.put("quotaunittime", planDetails.get("QuotaUnitTime"));
			}
			
			if((quotaType.equalsIgnoreCase("Data")) || (quotaType.equalsIgnoreCase("Both"))) {
				planJsonObject.put("quota",Float.valueOf(planDetails.get("QuotaData")).intValue());
				planJsonObject.put("quotaUnit", planDetails.get("QuotaUnitData"));
			}
			planJsonObject.put("quotaResetInterval", planDetails.get("QuotaResetInterval"));
			
			planJsonObject.put("qospolicyid", commonGetAPI.getQosPolicyId(planDetails.get("QosPolicy")));
			
			String timeBasePolicy = planDetails.get("TimeBasePolicy");
			if(!"".equals(timeBasePolicy)) {
				planJsonObject.put("timebasepolicyId", commonGetAPI.getTimeBasePolicyId(planDetails.get("TimeBasePolicy")));
			} else {
				planJsonObject.put("timebasepolicyId", "");
			}
			
			String planType = planDetails.get("Type");
			String billingCycle = "1";
			if(planType.equalsIgnoreCase("Postpaid")) {
				billingCycle =  planDetails.get("PostpaidBilingCycle");
			}
			
			List<org.json.simple.JSONObject> chargeJsonObjectList = new ArrayList<org.json.simple.JSONObject>();
			
			float offerprice = 0.0f;
			String chargeNames[] = planDetails.get("ChargeName").split(",");
			
			for(int i=0;i<chargeNames.length;i++) {
				
				String tempChargeName = chargeNames[i];
				
				int chargeId = commonGetAPI.getChargeId(tempChargeName);
				String taxAmountAndActualPrice = getChargeByIdAndTaxAmountAndActualPrice(chargeId);
				String ans[] = taxAmountAndActualPrice.split(":");
				float taxamount = Float.parseFloat(ans[0]);
				float actualprice = Float.parseFloat(ans[1]);
				
				offerprice = offerprice + taxamount + actualprice;
										
				org.json.simple.JSONObject chargeListObject = new org.json.simple.JSONObject();
				org.json.simple.JSONObject chargeObject = new org.json.simple.JSONObject();	
				
				chargeObject.put("id", chargeId);
				chargeObject.put("actualprice", actualprice);
				chargeObject.put("taxamount", taxamount);	
				
				chargeListObject.put("billingCycle", billingCycle);
				chargeListObject.put("charge", chargeObject);
				chargeJsonObjectList.add(chargeListObject);
				
			}
			
			
			String tempOfferPrice = Utility.formattedDecimalNumber(offerprice);
			planJsonObject.put("offerprice", tempOfferPrice);
			planJsonObject.put("chargeList", chargeJsonObjectList);
			
			String planCategory =  planDetails.get("Category");
			if(planCategory.equalsIgnoreCase("Business Promotion")) {
				String newOfferPrice =  planDetails.get("NewOfferPrice");
				if(!"".equals(newOfferPrice)) {
					float tempNewOfferPrice = Float.valueOf(newOfferPrice);
					String strNewOfferPrice = Utility.formattedDecimalNumber(tempNewOfferPrice);
					planJsonObject.put("newOfferPrice", strNewOfferPrice);					
				} else {
					planJsonObject.put("newOfferPrice", tempOfferPrice);		
				}
			
			}
			
			jsonString = planJsonObject.toJSONString();
			
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
			taxAmountAndActualPrice = taxAmount +":"+ actualprice;
		}
		
		if("".equals(taxAmountAndActualPrice)) {
			System.out.println("Taxamount and Actualprice details not found from chargeId - " + chargeId);
			Utility.printLog(logFileName, logModuleName, "Taxamount and Actualprice details not found from chargeId - ", String.valueOf(chargeId));
		}
		
		return taxAmountAndActualPrice;
	}
	
}
