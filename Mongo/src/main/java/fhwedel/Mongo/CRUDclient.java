package fhwedel.Mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class CRUDclient {

  private static final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

  public static MongoDatabase getDatabase() {
    return mongoClient.getDatabase("myMongoDB");
  }

  public static void closeClient() {
    mongoClient.close();
  }
  
}