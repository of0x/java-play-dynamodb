package services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import models.Widget;

import play.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WidgetService {

    private static final String TABLE_NAME = "widgets";
    private static final String KEY = "name";


    private static final List<Widget> widgets = new ArrayList<Widget>();

    /*
    static {
        // TODO: check for pre-existing table instead of generic exception handling
        try {
            DynamoDBService.createTable("widgets", "name");
        }
        catch (Exception e) {
        }

        // Wait for it to become active
        DynamoDBService.waitForTableToBecomeAvailable(TABLE_NAME);

        // Describe our new table
        DynamoDBService.describeTable(TABLE_NAME);

        Widget w1 = new Widget("w1", "widget1", 1);
        widgets.add(w1);
        Map<String, AttributeValue> item = w1.toDynamoMap();
        DynamoDBService.putItem(TABLE_NAME, item);

        Widget w2 = new Widget("w2", "widget2", 2);
        widgets.add(w2);
        item = w2.toDynamoMap();
        DynamoDBService.putItem(TABLE_NAME, item);
    }
    */

    public static void createWidgetsTable() {
        try {
            DynamoDBService.createTable(TABLE_NAME, KEY);
        } catch (AmazonServiceException ase) {
            if (ase.getErrorCode().equalsIgnoreCase("ResourceNotFoundException") == false) {
                throw ase;
            } else {
                Logger.warn("Create table returned: "+ase.getErrorCode()+" ,table "+TABLE_NAME+" may already exist");
            }
        }
    }

    public static void dropWidgetsTable() {
            DynamoDBService.dropTable(TABLE_NAME);
    }

    public static List<Widget> getWidgets() {
        List<Widget> widgets = new ArrayList<Widget>();
        ScanResult scanResult = DynamoDBService.scan(TABLE_NAME);
        List<Map<String, AttributeValue>> items = scanResult.getItems();
        for (Map<String, AttributeValue> item : items) {
            Widget widget = Widget.fromDynamoMap(item);
            widgets.add(widget);
        }
        return widgets;
    }

    public static void putWidget(Widget w) {
        Map<String, AttributeValue> item = w.toDynamoMap();
        DynamoDBService.putItem(TABLE_NAME, item);
    }

}
