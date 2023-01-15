package commons;

import org.json.JSONArray;
import org.json.JSONObject;

import api.RestExecution;
import utility.Utility;

public class CommonList extends RestExecution {

	private String logFileName = "common.log";
	private String logModuleName = "CommonList";
	
	
	public String getCommonChargeType(String chargeType) {

		String apiURL = getAPIURL("commonList/generic/chargetype");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		String commonChargeType = null;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedChargeType = jsonArray.getJSONObject(i).getString("text");
				if (chargeType.equalsIgnoreCase(receivedChargeType)) {
					commonChargeType = jsonArray.getJSONObject(i).getString("value");
				}
			}
		}

		if (commonChargeType == null) {
			System.out.println("Common Chargetype details not found - " + chargeType);
			Utility.printLog(logFileName, logModuleName, "Common Chargetype details not found - ", chargeType);
		}

		return commonChargeType;
	}
	
	public String getCommonChargeCategory(String chargeCategory) {

		String apiURL = getAPIURL("commonList/chargeCategory");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("responseCode");
		String commonChargeCategory = null;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedChargeCategory = jsonArray.getJSONObject(i).getString("text");
				if (chargeCategory.equalsIgnoreCase(receivedChargeCategory)) {
					commonChargeCategory = jsonArray.getJSONObject(i).getString("value");
				}
			}
		}

		if (commonChargeCategory == null) {
			System.out.println("Common ChargeCategory details not found - " + chargeCategory);
			Utility.printLog(logFileName, logModuleName, "Common ChargeCategory details not found - ", chargeCategory);
		}

		return commonChargeCategory;
	}
	
	public String getCommonPlanGroup(String planCategory) {

		String apiURL = getAPIURL("commonList/planGroup");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("responseCode");
		String commonPlanCategory = null;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedPlanCategory = jsonArray.getJSONObject(i).getString("text");
				if (planCategory.equalsIgnoreCase(receivedPlanCategory)) {
					commonPlanCategory = jsonArray.getJSONObject(i).getString("value");
				}
			}
		}

		if (commonPlanCategory == null) {
			System.out.println("Plan-Category details not found - " + planCategory);
			Utility.printLog(logFileName, logModuleName, "Plan-Category details not found - ", planCategory);
		}

		return commonPlanCategory;
	}
	
	
	public String getCommonPaymentMode(String paymentMode) {

		String apiURL = getAPIURL("commonList/paymentMode");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("responseCode");
		String commonPaymentMode = null;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedChargeType = jsonArray.getJSONObject(i).getString("text");
				if (paymentMode.equalsIgnoreCase(receivedChargeType)) {
					commonPaymentMode = jsonArray.getJSONObject(i).getString("value");
				}
			}
		}

		if (commonPaymentMode == null) {
			System.out.println("Common PaymentMode details not found - " + paymentMode);
			Utility.printLog(logFileName, logModuleName, "Common PaymentMode details not found - ", paymentMode);
		}
		return commonPaymentMode;
	}
	
	
}
