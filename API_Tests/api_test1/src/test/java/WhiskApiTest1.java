
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import org.json.simple.JSONObject;

import org.junit.Assert;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import org.testng.annotations.*;
import org.testng.ITestNGListener;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import java.util.LinkedHashMap;


public class WhiskApiTest1 {

    String baseURI = "https://api.whisk-dev.com/";
    String accessToken = "Bearer 4PkSbMXhfI4xMmdWy9xpTLLHkvImTGkY84zW9xbbdR8Dm4Q8FH0BL3Sdy73mdkSo";

    // requests
    String createShoppingList = baseURI + "list/v2";
    String getShoppingList = baseURI + "list/v2/";

    private static String shoppingListName = "Holiday shopping list";

    ValidatableResponse responseCreateShoppingList;
    ValidatableResponse responseGetShoppingListByID;


    public static void main(String[] args) {

        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        testng.addListener((ITestNGListener) tla);
        testng.run();

    }

    public static String generateJSONCreateShoppingList() {
        JSONObject createShoppingListData = new JSONObject();

        createShoppingListData.put("name", shoppingListName);
        createShoppingListData.put("primary", false);

        return createShoppingListData.toJSONString();
    }

    @Test (priority = 1, description="test API methods CreateShoppingList, GetShoppingList")
    public void checkCreateShoppingListTest() {

        // generate JSON for create shopping list
        String createShoppingListJSON = generateJSONCreateShoppingList();
        System.out.printf("\n create shopping listJSON: " + createShoppingListJSON); // This need for debug


        // create shopping list
        responseCreateShoppingList = given().
                header("Content-Type", "application/json").
                header("Authorization", accessToken).
                body(createShoppingListJSON).
                when().
                post(createShoppingList).
                then().
                //statusCode(200).
                contentType(ContentType.JSON);

        // return create shopping list status code
        Integer createShoppingListStatusCode = responseCreateShoppingList.extract().statusCode();
        System.out.printf("\nNEEDFORLOG CreateShoppingList Status Code: " + createShoppingListStatusCode);

        // create shopping list Estimate Time
        long createShoppingListTime = responseCreateShoppingList.extract().time();
        System.out.printf("\nNEEDFORLOG create Shopping list Estimate time: " + createShoppingListTime);

        String responseBodyCreateList = responseCreateShoppingList.extract().asString(); // extract string from response
        System.out.printf("\nResponse createShoppingList: " + responseBodyCreateList); // Show information in console. This need for debug

        String createShoppingListID = responseCreateShoppingList.extract().path("list.id"); // destinationCode
        System.out.printf("\n ID from CreateShoppingList: " + createShoppingListID);

        // get shopping list
        responseGetShoppingListByID = given().
                header("Content-Type", "application/json").
                header("Authorization", accessToken).
                when().
                get(getShoppingList + createShoppingListID).
                then().
                //statusCode(200).
                contentType(ContentType.JSON);

        // return get shopping list status code
        Integer getShoppingListStatusCode = responseGetShoppingListByID.extract().statusCode();
        System.out.printf("\nNEEDFORLOG Get ShoppingList Status Code: " + getShoppingListStatusCode); // Show information in console. This need for debug

        // get shopping list Estimate Time
        long getShoppingListTime = responseGetShoppingListByID.extract().time();
        System.out.printf("\nNEEDFORLOG Get Shopping list Estimate time: " + getShoppingListTime); // Show information in console. This need for debug

        String responseBodyGetShoppingList = responseGetShoppingListByID.extract().asString(); // extract string from response
        System.out.printf("\nResponse Get Shopping List: " + responseBodyGetShoppingList); // Show information in console. This need for debug

        String getShoppingListID = responseGetShoppingListByID.extract().path("list.id");
        System.out.printf("\n ID from GetShoppingList: " + getShoppingListID); // Show information in console. This need for debug

        //LinkedHashMap<String, Integer> getShoppingList = responseGetShoppingListByID.extract().path("list");
        //System.out.printf("\n Content from GetShoppingList: " + getShoppingList); // Show information in console. This need for debug

        // Show information in console. This need for debug
        LinkedHashMap<String, Integer>  getShoppingList = responseGetShoppingListByID.extract().path("list");
        System.out.printf("\n Content from GetShoppingList: " + getShoppingList); // Show information in console. This need for debug
        int getShoppingListSize = getShoppingList.size();
        System.out.printf("\n list size: " + getShoppingListSize);

        if (getShoppingListSize == 0){
            Assert.fail();
        }

        Boolean idIsEqual = getShoppingListID.equals(createShoppingListID);
        if (idIsEqual != true) {
            Assert.fail();
        }

        LinkedHashMap<String, Integer>  getShoppingListContent = responseGetShoppingListByID.extract().path("content");
        System.out.printf("\n Content from GetShoppingList: " + getShoppingListContent); // Show information in console. This need for debug

        int getShoppingListContentSize = getShoppingListContent.size();
        System.out.printf("\n Content size: " + getShoppingListContentSize); // Show information in console. This need for debug

        if (getShoppingListContentSize != 0){
            Assert.fail();
        }
    }

    @AfterMethod
    public void testResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {

            String className = result.getTestClass().getName();
            System.out.printf("\n Test " + className + "." + result.getName() + " FAILED");

        }
    }
}