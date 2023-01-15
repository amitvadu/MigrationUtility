package prepaidplan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customer.PrepaidCustomer;

public class CreatePrepaidPlanData {

	public void createPlanService() {

		PlanService prepaidPlanService = new PlanService();
		Map<String, String> map = new HashMap<String, String>();
		map = prepaidPlanService.readUniquePlanServiceList();
		prepaidPlanService.createPlanService(map);
	}

	private void createPlanTax() {

		PlanTax planTax = new PlanTax();
		Map<String, String> map = new HashMap<String, String>();
		map = planTax.readUniquePlanTaxList();
		planTax.createPlanTax(map);
	}
	
	private void createPlanCharge() {

		PlanCharge planCharge = new PlanCharge();
		List<Map<String, String>> chargeMapList = planCharge.readUniquePlanChargeList();
		planCharge.createPlanCharge(chargeMapList);
	}

	private void createPlanQos() {

		PlanQos planQos = new PlanQos();
		List<Map<String, String>> qosMapList = planQos.readUniquePlanQosList();
		planQos.createPlanQos(qosMapList);
	}

	private void createPrepaidPlan() {

		PrepaidPlan prepaidPlan = new PrepaidPlan();
		List<Map<String, String>> planMapList = prepaidPlan.readUniquePrepaidPlanList();
		prepaidPlan.createPrepaidPlan(planMapList);
	}
	
	private void createPrepaidCustomer() {

		PrepaidCustomer prepaidCustomer = new PrepaidCustomer();
		List<Map<String, String>> customerMapList = prepaidCustomer.readUniquePrepaidCustomerList();
		//System.out.println("customerMapList = " +customerMapList.toString());
		prepaidCustomer.createPrepaidCustomer(customerMapList);		
	}
	
	public void generatePrepaidPlanData() {
		
		System.out.println("Started Generting PrepaidPlan Data...!");
		
	//	createPlanService();
	//	createPlanTax();
	//	createPlanCharge();
	//	createPlanQos();
		createPrepaidPlan();
		
		System.out.println("Ended Generting PrepaidPlan Data...!");
	}

}
