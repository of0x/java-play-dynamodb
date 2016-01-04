import models.Widget;
import org.junit.*;

import play.mvc.*;
import services.WidgetService;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {


    private static final  String EXPECTED_TEST_W1 = "\"name\" : \"w1\"";
    private static final  String EXPECTED_TEST_W2 = "\"name\" : \"w2\"";
    private static final  Widget TEST_WIDGET_1 = new Widget("w1","widget one",5);
    private static final  Widget TEST_WIDGET_2 = new Widget("w2","widget two",3);


    @Before
    public void setUp() {
        WidgetService.createWidgetsTable();
    }

    @After
    public void tearDown() {
        WidgetService.dropWidgetsTable();
    }

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Widgets");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Widgets");
    }

    @Test
    public void testAPIputWidget() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Result result = route(fakeRequest(PUT, "/api/widget").withJsonBody(TEST_WIDGET_1.asJsonNode(), PUT));
                assertTrue(result != null);
                assertTrue(header("Etag",result).contains("w1"));
            }
        });
    }


    @Test
    public void testAPIgetAll() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                //Put two widgets into the DB
                Result result =
                        route(fakeRequest(PUT, "/api/widget").withJsonBody(TEST_WIDGET_1.asJsonNode(), PUT));
                result = route(fakeRequest(PUT, "/api/widget").withJsonBody(TEST_WIDGET_2.asJsonNode(), PUT));

                //Get them
                result = route(fakeRequest(GET, "/api/widget/summary"));
                assertTrue(result != null);
                String jsonResult = contentAsString(result);
                assertTrue("Expected  " + EXPECTED_TEST_W1 + " in " + jsonResult,
                        jsonResult.contains(EXPECTED_TEST_W1));

                assertTrue("Expected  " + EXPECTED_TEST_W2 + " in " + jsonResult,
                        jsonResult.contains(EXPECTED_TEST_W2));

            }
        });
    }

}
