package fhwedel.Mongo;

import com.mongodb.client.*;
import org.bson.Document;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CRUDclientTest {

    private static MongoDatabase db;

    @BeforeAll
    static void setup() {
        db = CRUDclient.getDatabase();
    }

    @BeforeEach
    void cleanUp() {
        db.getCollection("buecher").drop();
        db.getCollection("leser").drop();
        db.getCollection("ausleihen").drop();
    }

    @AfterAll
    static void closeConnection() {
        CRUDclient.closeClient();
    }

    @Test
    void test1a() {
        //Legen Sie bitte ein JSON-Dokument für ein Exemplar des Buchs Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers von Marc-Uwe Kling (Ullstein Verlag) an.
        Document buch = new Document("titel", "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers").append("autor", "Marc-Uwe Kling").append("verlag", "Ullstein Verlag");
        assertNotNull(buch, "Document 'buch' should not be null");

        MongoCollection<Document> buecherCollection = db.getCollection("buecher");
        assertNotNull(buecherCollection, "Collection 'buecher' should not be null");

        buecherCollection.insertOne(buch);
        assertEquals(1, buecherCollection.countDocuments());

        Document found1 = buecherCollection.find(new Document("autor", "Marc-Uwe Kling")).first();
        assertNotNull(found1);
        assertEquals("Ullstein Verlag", found1.getString("verlag"));

        //Legen Sie bitte ein JSON-Dokument für ein Dokument für den Leser Friedrich Funke, Bahnhofstraße 17, 23758 Oldenburg an.
        Document leser = new Document("name", "Friedrich Funke").append("adresse", new Document("strasse", "Bahnhofstraße 17").append("plz", "23758").append("ort", "Oldenburg"));
        assertNotNull(leser, "Document 'leser' should not be null");

        MongoCollection<Document> leserCollection = db.getCollection("leser");
        assertNotNull(leserCollection, "Collection 'leser' should not be null");

        leserCollection.insertOne(leser);
        assertEquals(1, leserCollection.countDocuments());

        Document found2 = leserCollection.find(new Document("name", "Friedrich Funke")).first();
        assertNotNull(found2);
        assertEquals("Oldenburg", found2.get("adresse", Document.class).getString("ort"));
        assertEquals("23758", found2.get("adresse", Document.class).getString("plz"));
        assertEquals("Bahnhofstraße 17", found2.get("adresse", Document.class).getString("strasse"));


        //Legen Sie bitte mindestens funf weitere Bücher und Leser-Dokumente Ihrer Wahl an.
        ArrayList<Document> buecherList = new ArrayList<>();
        buecherList.add(new Document("titel", "Titel1").append("autor", "Autor1").append("verlag", "Verlag1"));
        buecherList.add(new Document("titel", "Titel2").append("autor", "Autor2").append("verlag", "Verlag2"));
        buecherList.add(new Document("titel", "Titel3").append("autor", "Autor3").append("verlag", "Verlag3"));
        buecherList.add(new Document("titel", "Titel4").append("autor", "Autor4").append("verlag", "Verlag4"));
        buecherList.add(new Document("titel", "Titel5").append("autor", "Autor5").append("verlag", "Verlag5"));

        buecherCollection.insertMany(buecherList);
        assertEquals(6, buecherCollection.countDocuments());

        //Wie lässt sich der Sachverhalt, dass ein Leser ein bestimmtes Buch ausgeliehen hat, ausdrucken?

        //db.buecher.find({ titel: "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers" })
        //db.leser.find({ name: "Friedrich Funke" })
        //db.ausleihen.insertOne({
        //buch_id: ObjectId("68529c4b79e068bdc469e328"),
        //leser_id: ObjectId("68529d8379e068bdc469e329"),
        //ausgeliehen_am: new Date(2025,06,18,12,00),
        //rueckgabe_bis: new Date(2025,06,30,12,00)
        //})

        MongoCollection<Document> ausleihenCollection = db.getCollection("ausleihen");
        assertNotNull(ausleihenCollection, "Collection 'ausleihen' should not be null");


        ////TODO
        //Wir stellen uns ein einfaches Datenmodell einer Bibliothek vor, in dem Bucher ¨ durch Inventarnummer INVNR, Autor, Titel und Verlag dargestellt werden. Außerdem sollen Daten uber ¨
        //Leser (Lesernummern LNR, Name und Adresse) festgehalten werden. Zwischen Buchern und ¨
        //Lesern besteht die 1:N-Beziehung Entliehen, fur die zus ¨ ¨atzlich auch noch das Ruckgabedatum ¨
        //festgehalten wird.

    }

}