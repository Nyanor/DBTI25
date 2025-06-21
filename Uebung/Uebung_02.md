# MongoDB-CRUD

## Wichtige Befehle

- `show databases`
- `show collections`

---

### 1a)

Legen Sie bitte ein JSON-Dokument fur ein Exemplar des Buchs Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers von Marc-Uwe Kling (Ullstein Verlag) an.

```js
db.buecher.insertOne({
  titel: "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers",
  autor: "Marc-Uwe Kling",
  verlag: "Ullstein Verlag"
})
```

Legen Sie bitte ein JSON-Dokument fur ein Dokument fur den Leser Friedrich Funke, Bahnhofstraße 17, 23758 Oldenburg an.

```js
db.leser.insertOne({
  name: "Friedrich Funke",
  adresse: {
    strasse: "Bahnhofstraße 17",
    plz: "23758",
    ort: "Oldenburg"
  }
})
```

Legen Sie bitte mindestens funf weitere Bücher und Leser-Dokumente Ihrer Wahl an.

```js
db.buecher.insertMany([
  { titel: "Titel1", autor: "Autor1", verlag: "Verlag1" },
  { titel: "Titel2", autor: "Autor2", verlag: "Verlag2" },
  { titel: "Titel3", autor: "Autor3", verlag: "Verlag3" },
  { titel: "Titel4", autor: "Autor4", verlag: "Verlag4" },
  { titel: "Titel5", autor: "Autor5", verlag: "Verlag5" }
])
```

Wie lässt sich der Sachverhalt, dass ein Leser ein bestimmtes Buch ausgeliehen hat, ausdrucken?
```js
db.buecher.find({ titel: "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers" })
db.leser.find({ name: "Friedrich Funke" })

db.ausleihen.insertOne({
  buch_id: ObjectId("68529c4b79e068bdc469e328"),
  leser_id: ObjectId("68529d8379e068bdc469e329"),
  ausgeliehen_am: new Date(2025,06,18,12,00),
  rueckgabe_bis: new Date(2025,06,30,12,00)
})
```

## 1b)

Suchen Sie in Ihrer Mongo-Datenbank nach einem Buch mit dem Autor Marc-Uwe Kling.

```js
db.buecher.find({ autor: "Marc-Uwe Kling" })
```

## 1c)

Ermitteln Sie, wie viele Bücher Ihre Datenbank verwaltet.

```js
db.buecher.countDocuments()
```

## 1d)

Ermitteln Sie bitte alle Leser, die mehr als ein Buch ausgeliehen haben, absteigend sortiert nach Anzahl der entliehenen Bucher.
```js
TODO
```

## 1e)

Welche Operationen auf der Datenbank muss man ausfuhren, um widerzuspiegeln, dass ein Leser ein Buch ausleiht bzw. zuruckgibt? Lassen Sie bitte Friedrich Funke das Känguru-Buch ausleihen und wieder zuruckgeben.


```js
var leser = db.leser.findOne({ name: "Friedrich Funke" });
var buch = db.buecher.findOne({ titel: /Känguru-Chroniken/ });
```

```js
db.ausleihen.insertOne({
  leser_id: leser._id,
  buch_id: buch._id,
  ausgeliehen_am: new Date(),
  rueckgabe_bis: new Date("2025-07-30")
});
```

```js
db.ausleihen.deleteOne({
  leser_id: leser._id,
  buch_id: buch._id
});
```

## 1f)

Statt getrennte JSON-Collections fur Leser, Bücher und Entliehen zu haben, können entliehene Bücher auch in einer Liste innerhalb von Leser-Dokumenten verwaltet werden.

Legen Sie bitte (zusätzlich) ein Leser-Dokument für Heinz Müller, Klopstockweg 17, 38124 Braunschweig an, der das obige Känguru-Chroniken-Buch und auch noch „Der König von Berlin“ von Horst Evers (Rowohlt-Verlag) ausgeliehen hat.

```js
db.leser.insertOne({
  name: "Heinz Müller",
  adresse: {
    strasse: "Klopstockweg 17",
    plz: "38124",
    ort: "Braunschweig"
  },
  ausgeliehene_buecher: [
    {
      titel: "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers",
      autor: "Marc-Uwe Kling",
      verlag: "Ullstein Verlag",
      ausgeliehen_am: new Date("2025-07-01"),
      rueckgabe_bis: new Date("2025-07-15")
    },
    {
      titel: "Der König von Berlin",
      autor: "Horst Evers",
      verlag: "Rowohlt-Verlag",
      ausgeliehen_am: new Date("2025-07-01"),
      rueckgabe_bis: new Date("2025-07-15")
    }
  ]
})
```

Welche Vor- und Nachteile hat diese Art die Daten abzulegen?
```js
TODO
```

## 1g)

Welche Operationen auf der nun anders strukturierten Datenbank muss man ausführen, um widerzuspiegeln, dass ein Leser ein Buch ausleiht bzw. zurückgibt? Lassen Sie bitte Heinz Muller das Känguru-Buch zurückgeben.

```js
db.leser.updateOne(
  { name: "Heinz Müller" },
  { $pull: { ausgeliehene_buecher: { titel: "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers" } } }
)
```

```js
db.leser.updateOne(
  { name: "Friedrich Funke" },
  { $push: { ausgeliehene_buecher: {
      titel: "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers",
      autor: "Marc-Uwe Kling",
      verlag: "Ullstein Verlag",
      ausgeliehen_am: new Date("2025-07-18"),
      rueckgabe_bis: new Date("2025-07-30")
    }
  } }
)
```