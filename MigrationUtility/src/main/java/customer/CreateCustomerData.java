package customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customer.PrepaidCustomer;

public class CreateCustomerData {

	private void createPrepaidCustomer() {

		PrepaidCustomer prepaidCustomer = new PrepaidCustomer();
		List<Map<String, String>> customerMapList = prepaidCustomer.readUniquePrepaidCustomerList();
		prepaidCustomer.createPrepaidCustomer(customerMapList);		
	}

	
	private void recordPaymentDetails() {

		CustomerPaymentDetails customerPaymentDetails = new CustomerPaymentDetails();
		List<Map<String, String>> paymentDetailsMapList = customerPaymentDetails.readUniqueCustomerPaymentDetailsList();
		//System.out.println("paymentDetailsMapList = " +paymentDetailsMapList.toString());
		customerPaymentDetails.recordCustomerPaymentDetails(paymentDetailsMapList);
	}
	
	private void AssignInventoryToCustomer() {

		AssignInventory AssignInventory = new AssignInventory();
		List<Map<String, String>> customerMapList = AssignInventory.readAssignInventoryCustomerList();
		//System.out.println("paymentDetailsMapList = " +paymentDetailsMapList.toString());
		AssignInventory.AssignInventoryToCustomer(customerMapList);
	}

	
	
	public void generatePrepaidCustomerData() {
		
		System.out.println("Started Generting PrepaidCustomer Data...!");
		
	//	createPrepaidCustomer();
	//	recordPaymentDetails();
	//	AssignInventoryToCustomer();
		
		System.out.println("Ended Generting PrepaidCustomer Data...!");
	}

}
