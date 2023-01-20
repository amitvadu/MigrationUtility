package prepaidplan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customer.PrepaidCustomer;

public class CreateProductData {

	public void createPlanService() {

		PlanService planService = new PlanService();
		List<Map<String, String>> serviceMapList = planService.readPlanServiceList();
		planService.createPlanService(serviceMapList);
	}

	private void createPlanTax() {

		PlanTaxNew planTax = new PlanTaxNew();
		List<Map<String, String>> taxMapList = planTax.readPlanTaxList();
		planTax.createPlanTax(taxMapList);
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
		prepaidCustomer.createPrepaidCustomer(customerMapList);		
	}
	
	private void createPlanBundle() {

		PlanBundle planBundle = new PlanBundle();
		List<Map<String, String>> planBundleMapList = planBundle.readPlanBundleList();
		planBundle.createPlanBundle(planBundleMapList);		
	}

	
	public void generateProductData() {
		
		System.out.println("Started Generting Product Data...!");
		
	//	createPlanService();
	//	createPlanTax();
		createPlanCharge();
	//	createPlanQos();
	//	createPrepaidPlan();
	//	createPlanBundle();
		
		System.out.println("Ended Generting Product Data...!");
	}

}
