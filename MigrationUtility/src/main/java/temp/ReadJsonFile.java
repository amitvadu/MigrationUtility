package temp;

import java.io.IOException;
import java.util.List;

import commons.CommonList;
import customer.CustomerPaymentDetails;
import customer.PrepaidCustomer;
import productdata.PlanService;
import staff.Login;
import utility.Utility;


public class ReadJsonFile {

	public static void main(String args[]) throws IOException {
		
		Login login = new Login();
		login.setAuthBearer();
		
		PrepaidCustomer prepaidCustomer = new PrepaidCustomer(); 
		String ans = prepaidCustomer.getPlanBundleDetails("PlanBundle_5");
		System.out.println("ans = " +ans);
		
	/*	Outward outward = new Outward();
		
		List<String> macSerialList = new ArrayList<String>();
		
		macSerialList.add("B1001#B1001");
		macSerialList.add("B1004#B1004");
		macSerialList.add("B1005");
		
		outward.updateMacMapping(178,57,macSerialList);
		
		System.out.println("Done");
	*/	
		
	//	macGenerate();
		
				
	}
	
	private static void macGenerate() {
		
		for(int j=1;j<=3;j++) {
			String mac = "";
			String serial = "";
			for(int i=0;i<5*j;i++) {
				mac =  mac  + "," + Utility.getRandomMacAddress();
				serial = serial + "," + Utility.getRandomSerialNumber("A", 4);
			}
			
			System.out.println(mac);	
			System.out.println(serial);
		}
	}

	public static void temp() {
		Login login = new Login();
		login.setAuthBearer();
		
		PrepaidCustomer prepaidCustomer = new PrepaidCustomer();
		int custId = prepaidCustomer.getCustomerId("Customer_161");
		
		CommonList CommonList = new CommonList();
		String ans = CommonList.getCommonPaymentMode("CreditCard");
		System.out.println("ans = " + ans);
		
		CustomerPaymentDetails customerPaymentDetails = new CustomerPaymentDetails();
		int invId = customerPaymentDetails.getCustomerInvoiceId(custId);
		System.out.println("invId = " + invId);
	}
	
}
