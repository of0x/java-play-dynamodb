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

    static {
        createWidgetsTable();
    }


    public static void createWidgetsTable() {
        try {
            DynamoDBService.createTable(TABLE_NAME, KEY);
        } catch (AmazonServiceException ase) {
            if (ase.getErrorCode().equalsIgnoreCase("ResourceInUseException") == false) {
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
        Map<String, AttributeValue> item = w.toDynamoDBMap();
        DynamoDBService.putItem(TABLE_NAME, item);
    }

}
