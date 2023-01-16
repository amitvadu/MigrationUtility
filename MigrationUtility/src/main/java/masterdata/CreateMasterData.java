package masterdata;

import java.util.HashMap;
import java.util.Map;

import utility.DBConnect;

public class CreateMasterData {

	private void createCountry() {

		Country country = new Country();
		Map<String, String> map = new HashMap<String, String>();
		map = country.readUniqueCountryList();
		country.createCountry(map);
	}

	private void createProvince() {

		Province province = new Province();
		Map<String, String> map = new HashMap<String, String>();
		map = province.readUniqueProvisionList();
		province.createProvince(map);		
	}
	
	private void createDistrict() {

		District district = new District();
		Map<String, String> map = new HashMap<String, String>();
		map = district.readUniqueDistrictList();
		district.createDistrict(map);		
	}

	private void createMuncipility() {

		Municipality municipality = new Municipality();
		Map<String, String> map = new HashMap<String, String>();
		map = municipality.readUniqueMunicipalityList();
		municipality.createMunicipality(map);		
	}
	
	private void createServiceArea() {

		ServiceArea serviceArea = new ServiceArea();
		Map<String, String> map = new HashMap<String, String>();
		map = serviceArea.readUniqueServiceAreaList();
		serviceArea.createServiceArea(map);		
	}
	
	
	private void createWard() {

		Ward ward = new Ward();
		Map<String, String> map = new HashMap<String, String>();
		map = ward.readUniqueWardList();
		ward.createWard(map);		
	}
	
	private void createBusinessUnit() {

		BusinessUnit businessUnit = new BusinessUnit();
		Map<String, String> map = new HashMap<String, String>();
		map = businessUnit.readUniqueBusinessUnitList();
		businessUnit.createBusinessUnit(map);		
	}
	
	private void createBranch() {

		Branch branch = new Branch();
		Map<String, String> map = new HashMap<String, String>();
		map = branch.readUniqueBranchList();
		branch.createBranch(map);		
	}
	
	private void createRegion() {

		Region region = new Region();
		Map<String, String> map = new HashMap<String, String>();
		map = region.readUniqueRegionList();
		region.createRegion(map);		
	}
	
	private void createBusinessVertical() {

		BusinessVertical businessVertical = new BusinessVertical();
		Map<String, String> map = new HashMap<String, String>();
		map = businessVertical.readUniqueBusinessVerticalList();
		businessVertical.createBusinessVertical(map);		
	}
	
	private void createSubBusinessUnit() {

		SubBusinessUnit subBusinessUnit = new SubBusinessUnit();
		Map<String, String> map = new HashMap<String, String>();
		map = subBusinessUnit.readUniqueSubBusinessUnitList();
		subBusinessUnit.createSubBusinessUnit(map);		
	}
	
	public void generateMasterData() {
		System.out.println("Started to generate Master Data ...!");
		
		String query = "delete from status";
		DBConnect db = new DBConnect();
		db.executeQuery(query);
		
		createCountry();
		createProvince();
		createDistrict();
		createMuncipility();
		createServiceArea();
		createWard();
		createBusinessUnit();
		createBranch();
		createRegion();
		createBusinessVertical();
		createSubBusinessUnit();
		
		System.out.println("Ended to generate MasterData ...!");
	}
}
