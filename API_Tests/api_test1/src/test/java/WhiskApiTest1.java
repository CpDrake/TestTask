
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

        // create shopping list
        responseCreateShoppingList = given().
                header("Content-Type", "application/json").
                header("Authorization", accessToken).
                body(createShoppingListJSON).
                when().
                post(createShoppingList).
                then().
                contentType(ContentType.JSON);

        String createShoppingListID = responseCreateShoppingList.extract().path("list.id"); // extract list ID from response CreateShoppingList

        // get shopping list
        responseGetShoppingListByID = given().
                header("Content-Type", "application/json").
                header("Authorization", accessToken).
                when().
                get(getShoppingList + createShoppingListID).
                then().
                contentType(ContentType.JSON);


        String getShoppingListID = responseGetShoppingListByID.extract().path("list.id"); // extract list ID from response GetShoppingList

        Boolean idIsEqual = getShoppingListID.equals(createShoppingListID); // compare ID from GetShoppingList with ID from CreateShoppingList
        if (idIsEqual != true) {
            Assert.fail(); // if ID not equals send Error
        }

        LinkedHashMap<String, Integer>  getShoppingListContent = responseGetShoppingListByID.extract().path("content"); // extract content from get shopping list
        int getShoppingListContentSize = getShoppingListContent.size(); // count content size
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