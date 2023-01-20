package prepaidplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import utility.Utility;

public class PlanTaxNew extends RestExecution {

	private String logFileName = "prepaidplan.log";
	private String logModuleName = "CreateTax";

	private void createPlanTax(Map<String, String> tax) {

		String apiURL = getAPIURL("taxes");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String apiBody = getPlanTaxJson(tax);
		Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

		JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("status");
		String taxName = tax.get("TaxName");

		if (status == 200) {
			String message = "New Plan-Tax is added successfully - " + taxName;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);

		} else if (status == 406) {
			String error = JSONResponseBody.getString("ERROR") + " - " + taxName;
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createPlanTax(List<Map<String, String>> planTaxMapList) {

		for (int i = 0; i < planTaxMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = planTaxMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createPlanTax(map);
		}
	}

	public List<Map<String, String>> readPlanTaxList() {

		String sheetName = "Tax";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getPlanDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> planTaxMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {
			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			String taxName = cellValue.get("TaxName");
			if ((!"".equals(taxName)) && (taxName != null)) {

				valuemap.put("RowIndex", cellValue.get("RowIndex"));
				valuemap.put("TaxName", cellValue.get("TaxName"));
				valuemap.put("TaxType", cellValue.get("TaxType"));
				valuemap.put("TaxStatus", cellValue.get("TaxStatus"));
				valuemap.put("LedgerID", cellValue.get("LedgerID"));
				valuemap.put("TaxDescription", cellValue.get("TaxDescription"));

				valuemap.put("[Name:Rate:Group:Status]", cellValue.get("[Name:Rate:Group:Status]"));
				planTaxMapList.add(valuemap);
			}
		}
		return planTaxMapList;
	}

	private String getPlanTaxJson(Map<String, String> tax) {

		String jsonString = null;

		try {

			JSONObject planTaxJson = new JSONObject();

			planTaxJson.put("name", tax.get("TaxName"));
			planTaxJson.put("taxtype", "TIER");

			String status = tax.get("TaxStatus");
			if (!"".equals(status)) {
				if (status.equalsIgnoreCase("active")) {
					status = "Y";
				} else if (status.equalsIgnoreCase("inactive")) {
					status = "N";
				}
			}
			planTaxJson.put("status", status);

			String ledgerId = tax.get("LedgerID");
			if ("".equals(ledgerId)) {
				ledgerId = null;
			}

			planTaxJson.put("ledgerId", ledgerId);
			planTaxJson.put("desc", tax.get("TaxDescription"));

			// --Tax Tier List Details
			List<JSONObject> taxTierDetailsList = new ArrayList<JSONObject>();

			String taxNameRateGroupStatus = tax.get("[Name:Rate:Group:Status]");

			taxNameRateGroupStatus = taxNameRateGroupStatus.replaceAll("[\\[\\]]", "");
			String ans[] = taxNameRateGroupStatus.split(",");

			for (int i = 0; i < ans.length; i++) {

				String taxTierDetails[] = ans[i].split(":");
				String taxTierName = taxTierDetails[0];
				float taxTierRate = Float.valueOf(taxTierDetails[1]);
				String taxTierTaxGroup = taxTierDetails[2];
				boolean taxTierTaxStatus = Boolean.valueOf(taxTierDetails[3]);

				JSONObject taxTierJson = new JSONObject();

				taxTierJson.put("name", taxTierName);
				taxTierJson.put("rate", taxTierRate);
				taxTierJson.put("taxGroup", taxTierTaxGroup);
				taxTierJson.put("id", "");
				taxTierJson.put("beforeDiscount", taxTierTaxStatus);

				taxTierDetailsList.add(taxTierJson);
			}

			planTaxJson.put("tieredList", taxTierDetailsList);
			jsonString = planTaxJson.toString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

}
