package ru.red.db;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DBO2 {

    private static DBO2 INSTANCE = new DBO2();
    private MongoClient mongoClient = new MongoClient("195.146.74.90", 27023);
    private MongoDatabase dbMongo = mongoClient.getDatabase("imgData");
    private MongoCollection singlePhotos = dbMongo.getCollection("singlePhotos");

    private DBO2() {
        //singlePhotos.drop();
        System.out.println(singlePhotos.countDocuments());
    }

    public static DBO2 getInstance() {
        return INSTANCE;
    }


    public long getTotalCountSinglePhoto() {
        return singlePhotos.countDocuments();
    }

    public long getTotalCountSinglePhoto(String status) {
        BasicDBObject query = new BasicDBObject();
        query.append("status", status);
        return singlePhotos.countDocuments(query);
    }

    public long getTotalCountSinglePhoto(int i) {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject query1 = new BasicDBObject();
        query1.put("$exists","true");
        query1.put("$ne", "");
        query.put("status", query1);
        return singlePhotos.countDocuments(query);
    }

    public void showSP() {
        System.out.println(singlePhotos.countDocuments());
    }
}
