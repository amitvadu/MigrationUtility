package staff;

import java.util.List;
import java.util.Map;

public class CreateStaffData {

	private void createTeam() {
		Team teams = new Team();
		List<Map<String, String>> teamsMapList = teams.readTeamList();
		teams.createTeam(teamsMapList);
	}

	private void createStaff() {
		Staff staff = new Staff();
		List<Map<String, String>> staffMapList = staff.readStaffList();
		staff.createStaff(staffMapList);
	}

	public void generateStaffData() {
		System.out.println("Started to generte Staff Data...!");
		
		createTeam();
		createStaff();

		System.out.println("Ended to generte Staff Data...!");
	}

}
