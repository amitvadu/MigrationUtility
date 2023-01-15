package staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateStaffData {

	private void createTeam() {

		Team teams = new Team();
		Map<String, String> map = new HashMap<String, String>();
		map = teams.readUniqueTeamList();
		teams.createTeam(map);
	}

	private void createStaff() {

		Staff staff = new Staff();
		List<Map<String, String>> staffMapList = new ArrayList<Map<String, String>>();
		staffMapList = staff.readUniqueStaffList();
		staff.createStaff(staffMapList);
	}

	public void generateStaffData() {
		System.out.println("Started to generte Staff Data...!");
		
		createTeam();
		createStaff();

		System.out.println("Ended to generte Staff Data...!");
	}

}
