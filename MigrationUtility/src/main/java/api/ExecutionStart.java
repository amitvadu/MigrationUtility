package api;

import customer.CreateCustomerData;
import inventory.CreateInventoryData;
import masterdata.CreateMasterData;
import productdata.CreateProductData;
import staff.CreateStaffData;
import staff.Login;
import utility.Constant;
import utility.Utility;

public class ExecutionStart {

	public static void main(String args[]) {
		
		System.out.println("Started Migration Utility...!");
		Utility.printLog("execution.log", "MAIN", "Started Migration Utility...!","");
		
		startExecution();
		
		System.out.println("Ended Migration Utility...!");
		Utility.printLog("execution.log", "MAIN", "Ended Migration Utility...!","");
	}
	
	private static void startExecution() {
		
		Login login = new Login();
		login.setAuthBearer();
		
		/*	CreateTicketData createTicketData = new CreateTicketData();
		createTicketData.generateTicketData();
		
		CreateSalesCRMData createSalesCRMData = new CreateSalesCRMData();
		createSalesCRMData.generateSalesCRMData();
		
		CreateDunningData createDunningData = new CreateDunningData();
		createDunningData.generateDunningData();
		
	*/	
		if(Constant.MASTERDATA_MIGRATION) {
			CreateMasterData createMasterData = new CreateMasterData();
			createMasterData.generateMasterData();			
		}
		
		if(Constant.STAFFDATA_MIGRATION) {			
			CreateStaffData createStaffData = new CreateStaffData();
			createStaffData.generateStaffData();			
		}
		
		if(Constant.PLANDATA_MIGRATION) {			
			CreateProductData createProductData = new CreateProductData();
			createProductData.generateProductData();
		}
		
		if(Constant.INVENTORYDATA_MIGRATION) {
			CreateInventoryData createInventoryData = new CreateInventoryData();
			createInventoryData.generateInventoryData();			
		}
		
		if(Constant.CUSTOMERDATA_MIGRATION) {
			CreateCustomerData createCustomerData = new CreateCustomerData();
			createCustomerData.generatePrepaidCustomerData();			
		}
		
	}
}
