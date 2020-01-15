package com.amazonaws.code;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.amazonaws.Response;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

public class CreatePM10Table {

	@SuppressWarnings("deprecation")
	static DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient());

	static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public static void main(String[] args) throws Exception {
		DateTime dt = new DateTime();
		DateTime dt8 = new DateTime(dt, -8);
		String TableName8 = "PM10_" + dt8.getEightDigitDate() + "";
		String TableName = "PM10_" + dt.getEightDigitDate() + "";

		try {

			deleteTable(TableName8);

			// Parameter1: table name // Parameter2: reads per second //
			// Parameter3: writes per second // Parameter4/5: partition key and data type
			// Parameter6/7: sort key and data type (if applicable)

			createTable(TableName, 10L, 5L, "location", "S");

		} catch (Exception e) {
			System.err.println("Program failed:");
			System.err.println(e.getMessage());
		}
		System.out.println("Success.");
	}

	private static void deleteTable(String tableName) {
		Table table = dynamoDB.getTable(tableName);
		try {
			System.out.println("Issuing DeleteTable request for " + tableName);
			table.delete();
			System.out.println("Waiting for " + tableName + " to be deleted...this may take a while...");
			table.waitForDelete();

		} catch (Exception e) {
			System.err.println("DeleteTable request failed for " + tableName);
			System.err.println(e.getMessage());
		}
	}

	private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
			String partitionKeyName, String partitionKeyType) {

		createTable(tableName, readCapacityUnits, writeCapacityUnits, partitionKeyName, partitionKeyType, null, null);
	}

	private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
			String partitionKeyName, String partitionKeyType, String sortKeyName1, String sortKeyType1) {

		try {

			ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
			keySchema.add(new KeySchemaElement().withAttributeName(partitionKeyName).withKeyType(KeyType.HASH)); // Partition
																													// key

			ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(
					new AttributeDefinition().withAttributeName(partitionKeyName).withAttributeType(partitionKeyType));

			if (sortKeyName1 != null) {
				keySchema.add(new KeySchemaElement().withAttributeName(sortKeyName1).withKeyType(KeyType.RANGE)); // Sort
																													// key
				attributeDefinitions
						.add(new AttributeDefinition().withAttributeName(sortKeyName1).withAttributeType(sortKeyType1));
			}

			CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(keySchema)
					.withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(readCapacityUnits)
							.withWriteCapacityUnits(writeCapacityUnits));

			request.setAttributeDefinitions(attributeDefinitions);

			System.out.println("Issuing CreateTable request for " + tableName);
			Table table = dynamoDB.createTable(request);
			System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
			table.waitForActive();

		} catch (Exception e) {
			System.err.println("CreateTable request failed for " + tableName);
			System.err.println(e.getMessage());
		}
	}

	public Response useCache() {
		Response r;

		String username = "nP5gYtCuSX2sandYc6KQ8AZV";
		String password = "6rs0VQkBSvy4DnFzVUUSBQVH";
		String endpoint = "192.155.245.70:2809,192.155.245.71:2809";
		String gridName = "gPgAy1cYRZK34udAWD2KtAZG";

		try {
			String s = "";
			s += "Testing session retrieval with the following credentials:\n";
			s += "username: " + username + "\n";
			s += "password: " + password + "\n";
			s += "endpoint: " + endpoint + "\n";
			s += "gridname: " + gridName + "\n";
			s += "...\n";
			ObjectGridManager ogm = ObjectGridManagerFactory.getObjectGridManager();

			ClientSecurityConfiguration csc = ClientSecurityConfigurationFactory.getClientSecurityConfiguration();
			csc.setCredentialGenerator(new UserPasswordCredentialGenerator(username, password));
			csc.setSecurityEnabled(true);

			ClientClusterContext ccc = ogm.connect(endpoint, csc, null);
			ObjectGrid og = ogm.getObjectGrid(ccc, password);
			Session ogSession = og.getSession();
			s += "Successfully retrieved DataCache session!";
			s += "\n\n";
		} catch (Exception e) {
			s = "Failed to get DataCache session: " + e;
			r = Response.status(503).entity(s).build();
			return r;
		}
		try {
			ObjectMap om = ogSession.getMap("sample.NONE");
			Date d = new Date();
			SimpleDateFormat sdfVal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // insertion
			String inKey = String.valueOf(d.getTime());
			String inVal = sdfVal.format(d);
			s += "Key to be inserted: [" + inKey + "]\n";
			s += "Val to be inserted: [" + inVal + "]\n";
			om.upsert(inKey, inVal);
			s += "Successfull!\n\n";

			// retrieval
			s += "Testing retrieval...\n";
			String outKey = inKey;
			Object outVal = om.get(outKey);
			s += "Key checked : [" + outKey + "]\n";
			s += "Val obtained: [" + outVal.toString() + "]\n";
			s += "Successfull!\n\n"; // deletion
			s += "Testing deletion...\n";
			om.remove(inKey);
			s += "Successfull!";
			return Response.status(200).entity(s).build();
		} catch (Exception e) {
			s = "Failed to perform operation on map: " + e;
			r = Response.status(503).entity(s).build();
			return r;
		}

	}
}