package testCases;

import static io.restassured.RestAssured.given;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Practice {

	@Test
	public void readAllProduct2() {
		Response response =

				given().baseUri("https://techfios.com/api-prod/api/product")
						.header("Content-Type", "application/json; charset=UTF-8")
						.auth().preemptive()
						.basic("demo@techfios.com", "abc123").
				when()
						.get("/read.php").
				then()
						.extract().response();
		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		Assert.assertEquals(statusCode, 200);

		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time: "+responseTime);
		if (responseTime <= 200) {
			System.out.println("Within Range");
		} else {
			System.out.println("Out of Range");
		}
		String responseHeader= response.getHeader("Content-Type");
		System.out.println("Response Header:"+responseHeader);
		
		String responseBody= response.getBody().asString();
		JsonPath jsonPath= new JsonPath(responseBody);
		String productID= jsonPath.get("records[1].id");
		System.out.println("Product ID: "+productID);
		
	}
}
