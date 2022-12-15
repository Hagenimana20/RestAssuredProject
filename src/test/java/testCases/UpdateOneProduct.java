package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateOneProduct {

	SoftAssert softAssert= new SoftAssert();
String baseUrl="https://techfios.com/api-prod/api/product";
String file="src\\main\\java\\data\\createProductBody.json";
String CreateProductResponseHeader;
String responseBody;
String updatedresponseBody;
String ProductMessage;
String updatedProductMessage;
String firstProductID ;
HashMap<String, String> createProductBody;
HashMap<String, String> updateProductBody;

public Map<String, String> getCreateProductBody(){
	createProductBody= new HashMap<String, String>();
	updateProductBody= new HashMap<String, String>();
	
	createProductBody.put("name", "Amazing Pillow 5.0 By GH");
	createProductBody.put("price", "500");
	createProductBody.put("description", "Best product.");
	createProductBody.put("category_id", "2");
	createProductBody.put("category_name", "Electronics");		
	return createProductBody;
	
}

public Map<String, String> getUpdatedProductBody(){
	updateProductBody= new HashMap<String, String>();
	
	updateProductBody.put("id", firstProductID);
	updateProductBody.put("name", "Amazing Pillow 7.0 By HAG");
	updateProductBody.put("price", "400");
	updateProductBody.put("description", "Amazing Product");
	updateProductBody.put("category_id", "2");
	updateProductBody.put("category_name", "Electronics");		
	return updateProductBody;
	
}
	@Test(priority=1)
	public void createOneProduct() {
		Response response=
				
	given()
		.baseUri(baseUrl)
		.auth().preemptive().basic("demo@techfios.com", "abc123")
		.header("Content-Type", "application/json; charset=UTF-8")
		.body(getCreateProductBody()).
		//.body(new File(file)).
	when()
		.post("/create.php").
	then().extract().response();

	int satatusCode= response.getStatusCode();
	System.out.println("Status Code: "+satatusCode);	
	softAssert.assertEquals(satatusCode, 201);
	
	responseBody= response.getBody().asString();
	JsonPath jsonPath= new JsonPath(responseBody);
	ProductMessage= jsonPath.getString("message");
	System.out.println("Product Message: "+ProductMessage);
	softAssert.assertEquals(ProductMessage, "Product was created.");	
	
	
	CreateProductResponseHeader= response.header("Content-Type");
	System.out.println("Product Response Header: "+CreateProductResponseHeader);	
	softAssert.assertEquals(CreateProductResponseHeader, "application/json; charset=UTF-8");
	
	softAssert.assertAll();
	
	}	
	
	@Test(priority=2)
	public void readAllProducts() {

		Response response =

				given().baseUri("https://techfios.com/api-prod/api/product")
						.header("Content-Type", "application/json; charset=UTF-8")
						.auth().preemptive().basic("demo@techfios.com", "abc123").
				when()
					.get("/read.php").
				then()
					.extract().response();
		
			String responseBody = response.getBody().asString();
			JsonPath jsonPath = new JsonPath(responseBody);
			firstProductID = jsonPath.get("records[0].id");
			System.out.println("FirstProductID :" + firstProductID);		
		
	}
	
	@Test(priority=3)
	public void updateOneProduct() {
		Response response=
				
	given()
		.baseUri(baseUrl)
		.auth().preemptive().basic("demo@techfios.com", "abc123")
		.header("Content-Type", "application/json; charset=UTF-8")
		.body(getUpdatedProductBody()).
		//.body(new File(file)).
	when()
		.put("/update.php").
	then().extract().response();	
	
	responseBody= response.getBody().asString();
	JsonPath jsonPath= new JsonPath(responseBody);
	
	updatedProductMessage= jsonPath.getString("message");
	System.out.println("Product Message: "+updatedProductMessage);
	softAssert.assertEquals(updatedProductMessage, "Product was updated.");	
	
	softAssert.assertAll();
	
	}
	
	
	@Test(priority=4)
	public void readOneProduct() {
		Response response=
				
	given()
		.baseUri("https://techfios.com/api-prod/api/product")
		.auth().preemptive().basic("demo@techfios.com", "abc123")
		.header("Content-Type", "application/json")
		.queryParam("id", firstProductID).
	when()
		.get("/read_one.php").
	then().extract().response();	
	
	String responseBody= response.getBody().asString();
	JsonPath jsonPath= new JsonPath(responseBody);
	
	String ActualUpdatedProductName= jsonPath.getString("name");
	System.out.println("Actual Updated Product Name: "+ActualUpdatedProductName);	
	softAssert.assertEquals(ActualUpdatedProductName, getUpdatedProductBody().get("name"));
	
	String updatedProductDescription= jsonPath.getString("description");
	System.out.println("Product Description:"+updatedProductDescription);	
	softAssert.assertEquals(updatedProductDescription, getUpdatedProductBody().get("description"));
	
	String updatedProductPrice= jsonPath.getString("price");
	System.out.println("Product Price:"+updatedProductPrice);	
	softAssert.assertEquals(updatedProductPrice, getUpdatedProductBody().get("price"));
	
	softAssert.assertAll();
	
	}
}
