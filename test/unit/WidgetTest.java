package unit;


import models.Widget;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class WidgetTest {

    private static final String EXPECTED_NAME_ITEM = "\"name\" : \"name\"";
    private static final String EXPECTED_DECSRIPTION_ITEM = "\"description\" : \"desc\"";
    private static final String EXPECTED_PRICE_ITEM = "\"price\" : 3";


    @Test
    public void testWidgetJsonifies() {
        Widget testWidget = new Widget("name","desc",3);

        assertTrue("Widget as json should contain " + EXPECTED_NAME_ITEM,
                testWidget.json().contains(EXPECTED_NAME_ITEM));
        assertTrue("Widget as json should contain "+EXPECTED_DECSRIPTION_ITEM,
                testWidget.json().contains(EXPECTED_DECSRIPTION_ITEM));
        assertTrue("Widget as json should contain "+EXPECTED_PRICE_ITEM,
                testWidget.json().contains(EXPECTED_PRICE_ITEM));

    }
}
