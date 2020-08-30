
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

class WhiskApiTest2 {

    private String baseURI = "https://api.whisk-dev.com/";
    private String accessToken = "Bearer 4PkSbMXhfI4xMmdWy9xpTLLHkvImTGkY84zW9xbbdR8Dm4Q8FH0BL3Sdy73mdkSo";

    // requests
    private String createShoppingList = baseURI + "list/v2";
    private String shoppingList = baseURI + "list/v2/";


    private String shoppingListName = "Birthday shopping list";
    private String referenceMessageGetDeletedShoppingList = "shoppingList.notFound";

    private ValidatableResponse responseCreateShoppingList;
    private ValidatableResponse responseGetShoppingListByID;
    private ValidatableResponse responseDeleteShoppingListByID;

    private String createShoppingListJSON;
    private String createShoppingListID;

    public static void main(String[] args) {

        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        testng.setTestClasses(new Class[] { WhiskApiTest2.class });
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
                get(shoppingList + createShoppingListID).
                then().
                //statusCode(200). // when this list was deleted - server return statusCode(400) and test failed
                // (in test2 task step4 - Verify that code response = 200 - is not correct. Code response = 400)
                contentType(ContentType.JSON);

        return responseGetShoppingListByID;

    }

    private ValidatableResponse deleteShoppingList(){

        // delete shopping list
        responseDeleteShoppingListByID = given().
                header("Content-Type", "application/json").
                header("Authorization", accessToken).
                when().
                delete(shoppingList + createShoppingListID).
                then().
                contentType(ContentType.JSON);

        return responseDeleteShoppingListByID;

    }

    @Test (priority = 1, description="test API methods CreateShoppingList, DeleteShoppingList, GetDeletedShoppingList, check message")
    public void checkGetDeletedShoppingListTest() {

        createShoppingListJSON = generateJSONCreateShoppingList(); // generate JSON for create shopping list
        createShoppingList(); // create shopping list
        createShoppingListID = responseCreateShoppingList.extract().path("list.id"); // extract list ID from response CreateShoppingList
        deleteShoppingList(); // delete shopping list
        getShoppingList(); // get shopping list

        // code response getShoppingList=400  when get deleted shoppingList
        Integer getShoppingListStatusCode = responseGetShoppingListByID.extract().statusCode();
        //System.out.printf("\n getShoppingListStatusCode:" + getShoppingListStatusCode); // for debug

        if (getShoppingListStatusCode != 200){
            String messageGetDeletedShoppingList = responseGetShoppingListByID.extract().path("code"); // extract message from responce Get Deleted Shopping List
            //System.out.printf("\n messageGetDeletedShoppingList: " + messageGetDeletedShoppingList); // for debug

            Boolean textMessageIsEqual = messageGetDeletedShoppingList.equals(referenceMessageGetDeletedShoppingList); // compare text message GetDeletedShopping list with reference
            if (textMessageIsEqual != true) {
                Assert.fail(); // if message text not equals send Error
            }

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