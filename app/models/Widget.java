package models;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import play.Logger;

public class Widget {

    private String name;
    private String description;
    private Integer price;

    public Widget() {
        //required default constructor
    }

    public Widget(String name, String description, Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Map<String, AttributeValue> toDynamoDBMap() {
        Map<String, AttributeValue> map = new HashMap<String, AttributeValue>();
        map.put("name", new AttributeValue(name));
        map.put("description", new AttributeValue(description));
        map.put("price", new AttributeValue().withN(Integer.toString(price)));
        return map;
    }

    public String json() {
        return write(this);
    }

    public static String jsonlist(List<Widget> widgets) {
        return write(widgets);
    }

    public static Widget fromDynamoMap(Map<String, AttributeValue> item) {
        return new Widget(item.get("name").getS(), item.get("description").getS(), Integer.parseInt(item.get("price").getN()));
    }

    private static String write(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        try {
            return writer.writeValueAsString(o);
        } catch (JsonProcessingException jpe) {
            Logger.warn("failed on JSON write: " + jpe);
        }
        return null;
    }

    public JsonNode asJsonNode() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(this.json());
        } catch (IOException ex) {
            Logger.warn("failed on JSON write: " + ex);
        }
        return null;
    }

}
