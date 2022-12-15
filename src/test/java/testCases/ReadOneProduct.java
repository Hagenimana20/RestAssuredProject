package testCases;
import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
public class ReadOneProduct {
	SoftAssert softAssert= new SoftAssert();

	@Test
	public void readOneProduct() {
		Response response=
				
	given()
		.baseUri("https://techfios.com/api-prod/api/product")
		.auth().preemptive().basic("demo@techfios.com", "abc123")
		.header("Content-Type", "application/json")
		.queryParam("id", "5253").
	when()
		.get("/read_one.php").
	then().extract().response();

	int statusCode= response.getStatusCode();
	System.out.println("Status Code: "+statusCode);
	//Assert.assertEquals(satatusCode, 201);
	softAssert.assertEquals(statusCode, 200);
	
	String responseHeader= response.header("Content-Type");
	System.out.println("Response Header: "+responseHeader);
	//Assert.assertEquals(responseHeader, "application/json");
	softAssert.assertEquals(responseHeader, "application/json");
	
	String responseBody= response.getBody().asString();
	JsonPath jsonPath= new JsonPath(responseBody);
	
	String ProductName= jsonPath.getString("name");
	System.out.println("Product Name: "+ProductName);
	//Assert.assertEquals(ProductName, "Alan Walker Album");
	softAssert.assertEquals(ProductName, "Amazing Pillow 3.0 By GH");
	
	String ProductDescription= jsonPath.getString("description");
	System.out.println("Product Description:"+ProductDescription);
	//Assert.assertEquals(ProductDescription, "The best Songs for amazing programmers.");
	softAssert.assertEquals(ProductDescription, "Best product Ever.");
	
	String ProductPrice= jsonPath.getString("price");
	System.out.println("Product Description:"+ProductPrice);
	//Assert.assertEquals(ProductPrice, "199");
	softAssert.assertEquals(ProductPrice, "300");
	
	softAssert.assertAll();
	
	}
	
	
}
