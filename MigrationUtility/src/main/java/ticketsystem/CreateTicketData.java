package ticketsystem;

import java.util.HashMap;
import java.util.Map;

import prepaidplan.CreatePrepaidPlanData;

public class CreateTicketData {

	public void createRootCause() {

		RootCauseMaster rootCauseMaster = new RootCauseMaster();
		Map<String, String> map = new HashMap<String, String>();
		map = rootCauseMaster.readUniqueRootCauseList();
		rootCauseMaster.createRootCause(map);
	}

	public void createProblemDomain() {

		ProblemDomain problemDomain = new ProblemDomain();
		Map<String, String> map = new HashMap<String, String>();
		map = problemDomain.readUniqueProblemDomainList();
		problemDomain.createProblemDomain(map);
	}

	public void createSubProblemDomain() {

		SubProblemDomain subProblemDomain = new SubProblemDomain();
		Map<String, String> map = new HashMap<String, String>();
		map = subProblemDomain.readUniqueSubProblemDomainList();
		subProblemDomain.createSubProblemDomain(map);
	}

	public void generateTicketData() {
		System.out.println("Started to generte TicketData...!");

		CreatePrepaidPlanData createPrepaidPlanData = new CreatePrepaidPlanData();
		createPrepaidPlanData.createPlanService();
		
		createRootCause();
		createProblemDomain();
		createSubProblemDomain();

		System.out.println("Ended to generte TicketData...!");
	}

}
