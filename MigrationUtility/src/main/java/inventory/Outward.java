package inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import utility.DBOperations;
import utility.Utility;

public class Outward extends RestExecution {

	private static String logFileName = "inventory.log";
	private static String logModuleName = "CreateOutward";
	
	private static int inwardId = 0;

	private void createOutward(Map<String, String> outwardDetails) {

		String apiURL = getAPIURL("outwards/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getOutwardJson(outwardDetails);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("responseCode");

		if (status == 0) {
			
			String inwardNumber = JSONResponseBody .getJSONObject("data").getString("outwardNumber");
			
			String message = "New Outward is added successfully - " + inwardNumber;
			System.out.println(message);
			Utility.printLog("execution.log", logModuleName, "Success", message);
			
			int outwardId = JSONResponseBody .getJSONObject("data").getInt("id");
						
			outwardMacMapping(outwardId,outwardDetails);			
			//outwardApproval(outwardId);

		} else if (status == 406) {
			String error = JSONResponseBody.getString("responseMessage") + " - " + outwardDetails.get("Product");
			System.out.println(error);
			Utility.printLog("execution.log", logModuleName, "Already Exist", error);
		}
	}

	public void createOutward(List<Map<String, String>> outwardMapList) {

		for (int i = 0; i < outwardMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = outwardMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Data", map.toString());
			createOutward(map);
		}
	}

	public List<Map<String, String>> readUniqueOutwardList() {

		String sheetName = "Outward";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getInventoryDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> outwardMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			if (!"".equals(cellValue.get("Product"))) {

				valuemap.put("Product", cellValue.get("Product"));
				valuemap.put("SourceType", cellValue.get("SourceType"));
				valuemap.put("SelectSource", cellValue.get("SelectSource"));
				
				valuemap.put("DestinationType", cellValue.get("DestinationType"));
				valuemap.put("SelectDestination", cellValue.get("SelectDestination"));
				
				valuemap.put("outwardMAC", cellValue.get("outwardMAC"));
				valuemap.put("outwardSerialNumber", cellValue.get("outwardSerialNumber"));	
			
				valuemap.put("OutwardDate", cellValue.get("OutwardDate"));
				valuemap.put("QuantityOut", cellValue.get("QuantityOut"));				
				valuemap.put("Status", cellValue.get("Status"));

				outwardMapList.add(valuemap);
			}
		}
		return outwardMapList;
	}

	@SuppressWarnings("unchecked")
	private String getOutwardJson(Map<String, String> outwardDetails) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject outwardJsonObject = new org.json.simple.JSONObject();

			CommonGetAPI commonGetAPI = new CommonGetAPI();
			String warehouse = outwardDetails.get("SelectSource");
			String productName = outwardDetails.get("Product");
			
			
			int productId = commonGetAPI.getProductId(productName);
			if(productId != 0) {
				outwardJsonObject.put("productId", productId);
				
				String find = productName +"_"+ warehouse;
				
				DBOperations dbo = new DBOperations();			
				String query = "select id from status where entitytype='Inward' and name='" + find + "'";
				inwardId = Integer.parseInt(dbo.getSingleData(query));
				outwardJsonObject.put("inwardId", inwardId);
			}
			
			int warehouseId = commonGetAPI.getWarehouseId(warehouse);
			outwardJsonObject.put("sourceId", warehouseId);
			outwardJsonObject.put("sourceType", "Warehouse");
			
			
			outwardJsonObject.put("outwardNumber", "");
			
			String destinationType = outwardDetails.get("DestinationType");
			if(!"".equals(destinationType)) {
				if(destinationType.equalsIgnoreCase("staff")) {
					
					outwardJsonObject.put("destinationType", "Staff");
					int staffId = commonGetAPI.getStaffId(outwardDetails.get("SelectDestination"));			
					outwardJsonObject.put("destinationId", staffId);
					
				} else if(destinationType.equalsIgnoreCase("partner")) {
					
					outwardJsonObject.put("destinationType", "Partner");
					int partnerId = commonGetAPI.getPartnerId(outwardDetails.get("SelectDestination"));			
					outwardJsonObject.put("destinationId", partnerId);
					
				} else if(destinationType.equalsIgnoreCase("warehouse")) {
					
					outwardJsonObject.put("destinationType", "Warehouse");
					int warehouseId1 = commonGetAPI.getWarehouseId(outwardDetails.get("SelectDestination"));			
					outwardJsonObject.put("destinationId", warehouseId1);
				}
			}
			
			
			int qtyInward = Integer.parseInt(outwardDetails.get("QuantityOut"));
			if(qtyInward > 0) {
				outwardJsonObject.put("inTransitQty", qtyInward);
				
			}
			
			String outwardDate = outwardDetails.get("OutwardDate");
			if(!"".equals(outwardDate)) {
				outwardDate = Utility.getDateTimeInRequiredFormatFromProvidedDateTime(outwardDate, "dd-MMM-yyyy", "yyyy-MM-dd'T'HH:mm:ss");
				outwardJsonObject.put("outwardDateTime", outwardDate);
			}
			
			outwardJsonObject.put("status", outwardDetails.get("Status").toUpperCase());
			
			outwardJsonObject.put("id", null);
			outwardJsonObject.put("qty", null);
			outwardJsonObject.put("usedQty", null);
			outwardJsonObject.put("unusedQty", null);
			outwardJsonObject.put("outTransitQty", null);
			outwardJsonObject.put("rejectedQty", null);
			
			outwardJsonObject.put("mvnoId", null);

			jsonString = outwardJsonObject.toJSONString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

	
	private void outwardMacMapping(int outwardNumber, Map<String, String> outwardDetails) {
		
		List<String> macSerialList = new ArrayList<String>();
		
		String serial = outwardDetails.get("outwardSerialNumber");
		if (!"".equals(serial)) {
			
			String serialArray[] = serial.split(",");
			
			String mac = outwardDetails.get("outwardMAC");
			if (!"".equals(mac)) {
				String macArray[] = mac.split(",");
				
				for(int i=0;i<serialArray.length;i++) {
					
					String result = serialArray[i] +"#"+ macArray[i];
					macSerialList.add(result);
				}
			} else {
				for(int i=0;i<serialArray.length;i++) {
					
					String result = serialArray[i];
					macSerialList.add(result);
				}
			}
		}
		
		updateMacMapping(inwardId,outwardNumber,macSerialList);
		
	}
	
	@SuppressWarnings("unchecked")
 	private String outwardApproval(int inwardNumber) {
		
		String jsonString = null;

		try {

			org.json.simple.JSONObject macMappingJsonObject = new org.json.simple.JSONObject();
			
			if(inwardNumber!=0){
				
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
					
					String inwardNumber1 = JSONResponseBody .getJSONObject("data").getString("inwardNumber");					
					String message = "New Inward is approved successfully - " + inwardNumber1;
					
					System.out.println(message);
					Utility.printLog("execution.log", logModuleName, "Success", message);

				} else {
					String error = JSONResponseBody.getString("responseMessage") + " - IN-" + inwardNumber ;
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
	
	
	private List<JSONObject> getAllMACListByInwardId(int inwardId,int outwardId,List<String> macSerialList) {

		String apiURL = "inoutWardMacMapping/getbyinwardid?id=" + inwardId;
		apiURL = getAPIURL(apiURL);

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		
		List<JSONObject> list = new ArrayList<JSONObject>();

		if (status == 200) {
			
			for(int i=0;i<macSerialList.size();i++) {
				
				String serial = macSerialList.get(i);
				String mac = null;
				if(serial.contains("#")) {
					String macSerial[] = serial.split("#");					
					mac = macSerial[0];
					serial = macSerial[1];
				}
				
				JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
				//org.json.simple.JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
				
				for (int j = 0; j < jsonArray.length(); j++) {
					
					String receivedSerial = jsonArray.getJSONObject(j).getString("serialNumber");
					
					if(receivedSerial.equalsIgnoreCase(serial)) {
						
						String receivedMAC = jsonArray.getJSONObject(j).getString("macAddress");
						if(!receivedMAC.equals("null")) {
							if(receivedMAC.equalsIgnoreCase(mac)) {
								
								JSONObject macMappingJsonObject = new JSONObject();
								
								macMappingJsonObject =  jsonArray.getJSONObject(j);						
								macMappingJsonObject.put("outwardId", outwardId);					
								list.add(macMappingJsonObject);
								break;
							}
						} else {
							JSONObject macMappingJsonObject = new JSONObject();
							
							macMappingJsonObject =  jsonArray.getJSONObject(j);						
							macMappingJsonObject.put("outwardId", outwardId);					
							list.add(macMappingJsonObject);
							break;
						}
					}
				}
			}
		}
		
		return list;
	}
	

	public String updateMacMapping(int inwardId,int outwardId,List<String> macSerialList) {
		
		String jsonString = null;

		try {
			
			
			String apiURL = getAPIURL("inoutWardMacMapping/updateMACMappingList");
			Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

			// Initializing payload or API body
			String apiBody = getAllMACListByInwardId(inwardId, outwardId, macSerialList).toString();
			Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

			JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
			String response = JSONResponseBody.toString(4);
			Utility.printLog(logFileName, logModuleName, "Response", response);

			int status = JSONResponseBody.getInt("responseCode");

			if (status == 200) {
				
				String message = "New MAC/Serials are added in outward successfully - " + outwardId;
				System.out.println(message);
				Utility.printLog("execution.log", logModuleName, "Success", message);

			} else {
				String error = JSONResponseBody.getString("responseMessage") + " - " + outwardId;
				System.out.println(error);
				Utility.printLog("execution.log", logModuleName, "Error", error);
			}

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

	
		
}
