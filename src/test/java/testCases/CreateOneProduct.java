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

public class CreateOneProduct {

	SoftAssert softAssert= new SoftAssert();
String baseUrl="https://techfios.com/api-prod/api/product";
String file="src\\main\\java\\data\\createProductBody.json";
String CreateProductResponseHeader;
String responseBody;
String ProductMessage;
String firstProductID ;
HashMap<String, String> createProductBody;

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
	
	CreateProductResponseHeader= response.header("Content-Type");
	System.out.println("Product Response Header: "+CreateProductResponseHeader);	
	softAssert.assertEquals(CreateProductResponseHeader, "application/json; charset=UTF-8");
	
	responseBody= response.getBody().asString();
	JsonPath jsonPath= new JsonPath(responseBody);
	
	ProductMessage= jsonPath.getString("message");
	System.out.println("Product Message: "+ProductMessage);
	softAssert.assertEquals(ProductMessage, "Product was created.");	
	
	softAssert.assertAll();
	
	}
	
	public Map<String, String> getCreateProductBody(){
		createProductBody= new HashMap<String, String>();
		
		createProductBody.put("name", "Amazing Pillow 5.0 By GH");
		createProductBody.put("price", "500");
		createProductBody.put("description", "Best product.");
		createProductBody.put("category_id", "2");
		createProductBody.put("category_name", "Electronics");		
		return createProductBody;
		
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
	
	String ActualProductName= jsonPath.getString("name");
	System.out.println("Actual Product Name: "+ActualProductName);	
	softAssert.assertEquals(ActualProductName, getCreateProductBody().get("name"));
	
	String ProductDescription= jsonPath.getString("description");
	System.out.println("Product Description:"+ProductDescription);	
	softAssert.assertEquals(ProductDescription, getCreateProductBody().get("description"));
	
	String ProductPrice= jsonPath.getString("price");
	System.out.println("Product Description:"+ProductPrice);	
	softAssert.assertEquals(ProductPrice, getCreateProductBody().get("price"));
	
	softAssert.assertAll();
	
	}
}
