package services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import com.amazonaws.services.dynamodbv2.model.*;

import play.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBService {

    private static AmazonDynamoDBClient dynamoDB;

    static {
        dynamoDB = new AmazonDynamoDBClient(new BasicAWSCredentials("", ""));
        dynamoDB.setEndpoint("http://localhost:42753");
    }

    public static void createTable(String tableName, String primaryKeyAttributeName) {
        // Create a table with a primary hash key, which holds a string
        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
            .withKeySchema(new KeySchemaElement().withAttributeName(primaryKeyAttributeName).withKeyType(KeyType.HASH))
            .withAttributeDefinitions(new AttributeDefinition().withAttributeName(primaryKeyAttributeName).withAttributeType(ScalarAttributeType.S))
            .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
        TableDescription createdTableDescription = dynamoDB.createTable(createTableRequest).getTableDescription();
        Logger.info("Created Table: " + createdTableDescription);
    }

    public static void waitForTableToBecomeAvailable(String tableName) {
        Logger.info("Waiting for " + tableName + " to become ACTIVE...");

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (10 * 60 * 1000);
        while (System.currentTimeMillis() < endTime) {
            try {
                DescribeTableRequest request = new DescribeTableRequest().withTableName(tableName);
                TableDescription tableDescription = dynamoDB.describeTable(request).getTable();
                String tableStatus = tableDescription.getTableStatus();
                Logger.info("  - current state: " + tableStatus);
                if (tableStatus.equals(TableStatus.ACTIVE.toString())) return;
            } catch (AmazonServiceException ase) {
                if (ase.getErrorCode().equalsIgnoreCase("ResourceNotFoundException") == false) throw ase;
            }
            try {
                Thread.sleep(1000 * 5);
            }
            catch (Exception e) {
            }
        }

        throw new RuntimeException("Table " + tableName + " never went active");
    }

    public static void describeTable(String tableName) {
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
        Logger.info("Table Description: " + tableDescription);
    }

    public static void putItem(String tableName, Map<String, AttributeValue> item) {
        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        Logger.info("Result: " + putItemResult);
    }

    public static ScanResult scan(String tableName, HashMap<String, Condition> scanFilter) {
        ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);
        Logger.info("Result: " + scanResult);
        return scanResult;
    }

    public static ScanResult scan(String tableName) {
        ScanRequest scanRequest = new ScanRequest(tableName);
        ScanResult scanResult = dynamoDB.scan(scanRequest);
        Logger.info("Result: " + scanResult);
        return scanResult;
    }

    public static QueryResult query(String tableName) {
        QueryRequest queryRequest = new QueryRequest(tableName);
        QueryResult queryResult = dynamoDB.query(queryRequest);
        Logger.info("Result: " + queryResult);
        return queryResult;
    }

    public static void dropTable(String tableName) {
       dynamoDB.deleteTable(tableName);
    }
}
