package inventory;

import java.util.List;
import java.util.Map;

public class CreateInventoryData {

	private void createProductCategory() {

		ProductCategory productCategory = new ProductCategory();
		List<Map<String, String>> productCategoryMapList = productCategory.readUniqueProductCategoryList();
		productCategory.createProductCategory(productCategoryMapList);
	}

	private void createProduct() {

		Product product = new Product();
		List<Map<String, String>> productMapList = product.readUniqueProductList();
		product.createProduct(productMapList);
	}
	
	private void createPop() {

		POP pop = new POP();
		List<Map<String, String>> popMapList = pop.readUniquePopList();
		pop.createPop(popMapList);
	}

	
	private void createWarehouse() {

		Warehouse warehouse = new Warehouse();
		List<Map<String, String>> warehouseMapList = warehouse.readUniqueWarehouseList();
		warehouse.createWarehouse(warehouseMapList);
	}

	private void createInward() {

		Inward inward = new Inward();
		List<Map<String, String>> inwardMapList = inward.readUniqueInwardList();
		inward.createInward(inwardMapList);
	}

	private void createOutward() {

		Outward outward = new Outward();
		List<Map<String, String>> outwardMapList = outward.readUniqueOutwardList();
		outward.createOutward(outwardMapList);
	}

	
	public void generateInventoryData() {
		System.out.println("Started to generate Inventory Data ...!");

		createProductCategory();
		createProduct();
		createPop();
		createWarehouse();
		createInward();
		createOutward();
		
		System.out.println("Ended to generate Inventory Data ...!");
	}
}
