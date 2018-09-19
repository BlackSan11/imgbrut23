import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;

import java.util.List;

public class DBO {

    private static DBO INSTANCE = new DBO();
    private MongoClient mongoClient = new MongoClient();
    private MongoDatabase dbMongo = mongoClient.getDatabase("imgData");
    private MongoCollection singlePhotos = dbMongo.getCollection("singlePhotos");

    private DBO() {
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
        query.append("act", "a");
        Document res = (Document) singlePhotos.findOneAndUpdate(query, new BasicDBObject("$set", new BasicDBObject("act", "ci")));
        String res2 = (String) res.get("_id");
        return res2;
    }

    public String getLiveSinglePhoto() {
        BasicDBObject query = new BasicDBObject();
        query.append("act", "l");
        Document res = (Document) singlePhotos.find(query).limit(1).first();
        String res2 = (String) res.get("_id");
        return res2;
    }


    public void updateSinglePhotoAct(String sID, String act) {

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("act", act);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("_id", sID);
        singlePhotos.updateOne(
                searchQuery,
                new BasicDBObject("$set", new BasicDBObject("act", act)));
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
