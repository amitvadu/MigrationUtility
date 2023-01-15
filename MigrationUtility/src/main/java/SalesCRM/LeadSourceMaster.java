package SalesCRM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import utility.DBOperations;
import utility.Utility;

public class LeadSourceMaster extends RestExecution {

	private void createLeadSourceMaster(String leadSourceName,String subLeadSourceName) {

		String logFileName = "SalesCRM.log";
		String logModuleName = "CreateLeadSourceMaster";

		String apiURL = getAPIURL("AdoptSalesCrmsBss/leadSource/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getLeadSourceMasterJson(leadSourceName,subLeadSourceName);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		// Fetching the desired value of a parameter
		int status = JSONResponseBody.getInt("status");
		// System.out.println("status = " + result1);

		if (status == 200) {
			System.out.println("New LeadSourceMaster is added successfully - " + leadSourceName);

			String type = "LeadSourceMaster";
			JSONObject cityJSONObject = JSONResponseBody.getJSONObject("leadSource");
			String name = cityJSONObject.getString("leadSourceName");
			int id = cityJSONObject.getInt("id");

			DBOperations dbo = new DBOperations();
			dbo.setAPIData(type, name, id, status);
		}
	}

	public void createLeadSourceMaster(Map<String, String> map) {

		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();

		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = map.get(key);
			
			String ans[] = value.split(":");

			String leadSource = ans[0];
			String subLadSource="";
			
			int find =value.lastIndexOf(":");
			int len = value.length();
			
			if((len-find)>1) {
				subLadSource = ans[1];
			}
						
			createLeadSourceMaster(leadSource,subLadSource);
		}
	}

	public Map<String, String> readUniqueLeadSourceMasterList() {

		String sheetName = "Lead Source";
		int startRow = 1;
		int endRow = 14;

		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getSalesCRMDataSheet(sheetName, startRow, endRow);

		Map<String, String> cellValue = new HashMap<String, String>();
		Map<String, String> valuemap = new HashMap<String, String>();
		
		String ans = "";
		
		for (int i = startRow; i < endRow; i++) {
			
			cellValue = sheetMap.get(i);
			String leadSource = cellValue.get("Lead Source").trim();
			String subLeadSource = cellValue.get("Sub Source").trim();
			
			if ("".equals(leadSource)) {
				ans=ans +","+subLeadSource;
				continue;
			}else {
				if (!"".equals(ans)) {
					valuemap.put(ans, ans);	
				}
			}
			
			ans = leadSource +":"+ subLeadSource;					
		}
		
		if (!"".equals(ans)) {
			valuemap.put(ans, ans);	
		}
		return valuemap;
	}

	@SuppressWarnings({ "unchecked" })
	private String getLeadSourceMasterJson(String leadSourceName,String subLeadSourceName) {

		String jsonString = "";

		try {

			org.json.simple.JSONObject leadSourceMasterJsonObject = null;
			ReadData readData = new ReadData();
			leadSourceMasterJsonObject = readData.readJSONFile("CreateLeadSourceMaster.json");
			
			leadSourceMasterJsonObject.put("leadSourceName", leadSourceName);
			List<org.json.simple.JSONObject> jsonObjectList = new ArrayList<org.json.simple.JSONObject>();
			
			if(!"".equals(subLeadSourceName)) {
				
				String totalSubleadSource[] = subLeadSourceName.split(",");
				
				for(int i=0;i<totalSubleadSource.length;i++) {
					org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
					
					jsonObject.put("id", null);
					jsonObject.put("name", totalSubleadSource[i]);
					jsonObject.put("leadSourceId", null);					
					jsonObjectList.add(jsonObject);
				}
			}
			
			leadSourceMasterJsonObject.put("leadSubSourceDtoList", jsonObjectList);			
			jsonString = leadSourceMasterJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}


}


