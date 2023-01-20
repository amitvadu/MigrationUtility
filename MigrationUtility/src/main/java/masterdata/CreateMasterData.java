package masterdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utility.DBConnect;

public class CreateMasterData {

	private void createCountry() {
		Country country = new Country();
		List<Map<String, String>> countryMapList = country.readCountryList();
		country.createCountry(countryMapList);
	}

	private void createProvince() {
		Province province = new Province();
		List<Map<String, String>> provinceMapList = province.readProvinceList();
		province.createProvince(provinceMapList);		
	}
	
	private void createDistrict() {
		District district = new District();
		List<Map<String, String>> districtMapList = district.readDistrictList();
		district.createDistrict(districtMapList);		
	}

	private void createMuncipility() {
		Municipality municipality = new Municipality();
		List<Map<String, String>> municipalitiesMapList = municipality.readMunicipalityList();
		municipality.createMunicipality(municipalitiesMapList);		
	}
	
	private void createServiceArea() {
		ServiceArea serviceArea = new ServiceArea();
		List<Map<String, String>> serviceAreaMapList = serviceArea.readServiceAreaList();
		serviceArea.createServiceArea(serviceAreaMapList);		
	}
	
	private void createWard() {
		Ward ward = new Ward();
		List<Map<String, String>> wardMapList = ward.readWardList();
		ward.createWard(wardMapList);
	}
	
	private void createBranch() {
		Branch branch = new Branch();
		List<Map<String, String>> branchMapList = branch.readBranchList();
		branch.createBranch(branchMapList);
	}
	
	private void createBusinessUnit() {
		BusinessUnit businessUnit = new BusinessUnit();
		List<Map<String, String>> businessUnitMapList = businessUnit.readBusinessUnitList();
		businessUnit.createBusinessUnit(businessUnitMapList);
	}
	
	private void createSubBusinessUnit() {
		SubBusinessUnit subBusinessUnit = new SubBusinessUnit();
		List<Map<String, String>> SubBUMapList = subBusinessUnit.readSubBusinessUnitList();
		subBusinessUnit.createSubBusinessUnit(SubBUMapList);
	}
		
	private void createRegion() {

		Region region = new Region();
		List<Map<String, String>> regionMapList = region.readRegionList();
		region.createRegion(regionMapList);		
	}
	
	private void createBusinessVertical() {
		BusinessVertical businessVertical = new BusinessVertical();
		List<Map<String, String>> regionMapList = businessVertical.readBusinessVerticalList();
		businessVertical.createBusinessVertical(regionMapList);
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
		createBranch();
		createBusinessUnit();
		createSubBusinessUnit();
		createRegion();
		createBusinessVertical();
		
		System.out.println("Ended to generate MasterData ...!");
	}
}
