public class Shipment {
    private String shipmentId;
    private String origin;
    private String destination;
    private double weight:
}

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
public class ShipmentService {
    private final Table shipmentsTable;
    public ShipmentService(DynamoDB dynamoDB, String tableName) {
        this.shipmentsTable = dynamoDB.getTable(tableName);
    }
    public void addShipment(Shipment shipment) {
        PutItemSpec putItemSpec = new PutItemSpec().withItem(new Item()
                .withString("shipmentId", shipment.getShipmentId())
                .withString("origin", shipment.getOrigin())
                .withString("destination", shipment.getDestination())
                .withDouble("weight", shipment.getWeight()));
        PutItemOutcome outcome = shipmentsTable.putItem(putItemSpec);
    }
}
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final ShipmentService shipmentService;
    public LambdaHandler() {
        shipmentService = new ShipmentService(AmazonDynamoDBClientBuilder.defaultClient(), "shipments_table");
    }

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            Shipment shipment = parseInputToShipment(input.getBody());
            shipmentService.addShipment(shipment);
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Shipment added successfully");
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error adding shipment: " + e.getMessage());
        }
    }
    private Shipment parseInputToShipment(String inputJson) {
        Gson gson = new Gson();
        return gson.fromJson(inputJson, Shipment.class);
    }
}
