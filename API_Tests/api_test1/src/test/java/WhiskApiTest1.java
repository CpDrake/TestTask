
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

class WhiskApiTest1 {

    private String baseURI = "https://api.whisk-dev.com/";
    private String accessToken = "Bearer 4PkSbMXhfI4xMmdWy9xpTLLHkvImTGkY84zW9xbbdR8Dm4Q8FH0BL3Sdy73mdkSo";

    // requests
    private String createShoppingList = baseURI + "list/v2";
    private String getShoppingList = baseURI + "list/v2/";

    private String shoppingListName = "Holiday shopping list";

    private ValidatableResponse responseCreateShoppingList;
    private ValidatableResponse responseGetShoppingListByID;

    private String createShoppingListJSON;
    private String createShoppingListID;
    private String getShoppingListID;

    public static void main(String[] args) {

        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        testng.setTestClasses(new Class[] { WhiskApiTest1.class });
        testng.addListener((ITestNGListener) tla);
        testng.run();

    }

    private String generateJSONCreateShoppingList() {
        JSONObject createShoppingListData = new JSONObject();

        createShoppingListData.put("name", shoppingListName);
        createShoppingListData.put("primary", false);

        return createShoppingListData.toJSONString();
    }

    private ValidatableResponse createShoppingList(){

        // create shopping list
        responseCreateShoppingList = given().
                header("Content-Type", "application/json").
                header("Authorization", accessToken).
                body(createShoppingListJSON).
                when().
                post(createShoppingList).
                then().
                contentType(ContentType.JSON);

        return responseCreateShoppingList;

    }

    private ValidatableResponse getShoppingList(){

       // get shopping list
        responseGetShoppingListByID = given().
                header("Content-Type", "application/json").
                header("Authorization", accessToken).
                when().
                get(getShoppingList + createShoppingListID).
                then().
                contentType(ContentType.JSON);

        return responseGetShoppingListByID;

    }

    @Test (priority = 1, description="test API methods CreateShoppingList, GetShoppingList")
    public void checkCreateShoppingListTest() {

        createShoppingListJSON = generateJSONCreateShoppingList(); // generate JSON for create shopping list
        createShoppingList(); // create shopping list
        createShoppingListID = responseCreateShoppingList.extract().path("list.id"); // extract list ID from response CreateShoppingList
        getShoppingList(); // get shopping list
        getShoppingListID = responseGetShoppingListByID.extract().path("list.id"); // extract list ID from response GetShoppingList

        Boolean idIsEqual = getShoppingListID.equals(createShoppingListID); // compare ID from GetShoppingList with ID from CreateShoppingList
        if (idIsEqual != true) {
            Assert.fail(); // if ID not equals send Error
        }

        LinkedHashMap<String, Integer>  getShoppingListContent = responseGetShoppingListByID.extract().path("content"); // extract content from shopping list
        int getShoppingListContentSize = getShoppingListContent.size(); // get content size
        if (getShoppingListContentSize != 0){
            Assert.fail(); // if content is not empty send Error
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