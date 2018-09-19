package ru.red;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;

import java.util.Date;
import java.util.List;

public class DBO {

    private static DBO INSTANCE = new DBO();
    private MongoClient mongoClient = new MongoClient("195.146.74.90", 27023);
    private MongoDatabase dbMongo = mongoClient.getDatabase("imgData");
    private MongoCollection singlePhotos = dbMongo.getCollection("singlePhotos");

    private DBO() {
        //singlePhotos.drop();
        System.out.println(singlePhotos.countDocuments());
    }

    public static DBO getInstance() {
        return INSTANCE;
    }

    public void addSinglePhotos(List sIDs) {
        InsertManyOptions insertManyOptions = new InsertManyOptions();
        insertManyOptions.ordered(false);
        singlePhotos.insertMany(sIDs, insertManyOptions);
    }

    public synchronized String getSinglePhoto() {
        BasicDBObject query = new BasicDBObject();
        query.append("status", "");
        Document res = (Document) singlePhotos.findOneAndUpdate(query, new BasicDBObject("$set", new BasicDBObject("status", "processing")));
        String res2 = (String) res.get("_id");
        return res2;
    }

    public synchronized Document getSinglePhoto(String sID) {
        BasicDBObject query = new BasicDBObject();
        query.append("_id", sID);
        Document res = (Document) singlePhotos.find(query).limit(1).first();
        return res;
    }

    public synchronized Document getExistSinglePhoto() {
        BasicDBObject query = new BasicDBObject();
        query.append("status", "exist");
        Document res = (Document) singlePhotos.find(query).limit(1).first();
        return res;
    }

    public String getLiveSinglePhoto() {
        BasicDBObject query = new BasicDBObject();
        query.append("act", "l");
        Document res = (Document) singlePhotos.find(query).limit(1).first();
        String res2 = (String) res.get("_id");
        return res2;
    }


    public void updateAfterCheck(String sID, String status, String desc, String fullURL) {
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("status", status);
        newDocument.put("updated", new Date().getTime() / 1000);
        newDocument.put("fullURL", fullURL);
        newDocument.put("desc", desc);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("_id", sID);
        singlePhotos.updateOne(
                searchQuery,
                new BasicDBObject("$set", newDocument));
    }

    public void updateAfterCheck(String sID, String status) {
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("status", status);
        newDocument.put("updated", new Date().getTime() / 1000);
        newDocument.put("fullURL", "");
        newDocument.put("desc", "");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("_id", sID);
        singlePhotos.updateOne(
                searchQuery,
                new BasicDBObject("$set", newDocument));
    }

    public void updateAfterHide(String sID) {
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("status", "hide");
        newDocument.put("updated", new Date().getTime() / 1000);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("_id", sID);
        singlePhotos.updateOne(
                searchQuery,
                new BasicDBObject("$set", newDocument));
    }

    public void updateAfterSave(String sID) {
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("status", "saved");
        newDocument.put("updated", new Date().getTime() / 1000);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("_id", sID);
        singlePhotos.updateOne(
                searchQuery,
                new BasicDBObject("$set", newDocument));
    }

    private Boolean checkSinglePhoto(String sID) {
        BasicDBObject query = new BasicDBObject();
        query.append("sID", sID);
        if (singlePhotos.find(query).limit(1).first() != null) {
            System.out.println("СОВПАДЕНИЕ");
            return true;
        } else return false;
    }

    public void showSP() {
        System.out.println(singlePhotos.countDocuments());
    }
}
