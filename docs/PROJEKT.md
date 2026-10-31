# Sortiervergleich – Projekt und Technik

## Idee

Die Anwendung macht unterschiedliche Sortieransätze sichtbar. Eine Vergleichsansicht stellt zwei Verfahren gegenüber. Die Balkendarstellungen zeigen Zugriffe und Änderungen. Eine Tabelle ergänzt gemessene Laufzeiten, Schrittzahlen und theoretische Best-, Average- und Worst-Case-Angaben.

## Verfügbare Verfahren

| Gruppe | Verfahren |
| --- | --- |
| Klassische Vergleiche | BubbleSort, RecursiveBubbleSort, SelectionSort, InsertionSort, MergeSort, QuickSort, QuickSortIterative, HeapSort, ShellSort, IntroSort |
| Weitere Vergleichsverfahren | CocktailSort, CombSort, CycleSort, GnomeSort, OddEvenSort, PancakeSort |
| Wertebasierte Verfahren | BucketSort, CountingSort, PigeonholeSort, RadixSort |
| Experimentelle Ansätze | BogoSort, BozoSort, StalinSort |

StalinSort entfernt Werte, die kleiner als ihr Vorgänger sind. Seine Ausgabe kann daher kürzer als die Eingabe sein. BogoSort und BozoSort arbeiten mit zufälligen Versuchen und sind für kleine Demonstrationslisten gedacht.

## Daten und Darstellung

Die Standarddatensätze heißen `unsorted`, `halfSorted`, `sorted` und `reverse`. Der Generator verwendet Werte von 0 bis zur Elementzahl minus eins. Die API begrenzt die erzeugte Elementzahl auf 5 bis 1.000.000.

Der Vergleich arbeitet auf Kopien derselben Eingabeliste. `SortingService` misst die Dauer mit `System.currentTimeMillis` und übernimmt die vom Verfahren gezählten Schritte. Die Ergebnistabelle enthält jeweils die ersten fünf Eingabe- und Ausgabewerte.

Die animierten Ansichten führen eigene Sortiervorgänge über SSE aus. Die eingestellte Pause verlangsamt die Anzeige. Sie gehört nicht zur gemessenen Vergleichslaufzeit.

## Aufbau

![Technikübersicht](media/technik.svg)

| Bereich | Dateien und Komponenten |
| --- | --- |
| Anwendung | `sorting-comparer`, Spring Boot 3.5.6, Java 17 |
| REST und SSE | `CompareController` |
| Auswahl und Vergleich | `SortingService` |
| Datenobjekte | `CompareRequest`, `AlgorithmInfo`, `SortResult`, `SortStep` |
| Sortierverfahren | `SortingAlgorithms`, Interface `SortingAlgorithm` |
| Browseroberfläche | `src/main/resources/static/test.html`, `compare.css` |
| Balkendarstellung | `visualizer.html`, `visualizer.css`, HTML-Canvas |
| Build | Maven-Wrapper 3.9.11 |

## Start und Prüfung

Im Ordner `sorting-comparer` unter Windows:

```powershell
.\mvnw.cmd verify
java -jar target/sorting-comparer-0.0.1-SNAPSHOT.jar --server.address=127.0.0.1 --server.port=8080
```

Unter Linux:

```bash
sh ./mvnw verify
java -jar target/sorting-comparer-0.0.1-SNAPSHOT.jar --server.address=127.0.0.1 --server.port=8080
```

`verify` führt den vorhandenen Test aus und erstellt die ausführbare Spring-Boot-JAR. Die Weboberfläche ist anschließend unter `http://127.0.0.1:8080/test.html` erreichbar.

## Schnittstellen

| Methode und Pfad | Inhalt |
| --- | --- |
| `GET /api/compare/algorithms` | Namen und Komplexitätsangaben aller Verfahren |
| `GET /api/compare/datasets?count=32` | Vier Standarddatensätze |
| `POST /api/compare` | Vergleich ausgewählter Verfahren für eine Eingabeliste |
| `GET /api/compare/visualizer/{algorithm}` | SSE-Stream mit einzelnen Sortierschritten |

Eine Vergleichsanfrage enthält beispielsweise:

```json
{ "algorithms": ["MergeSort", "QuickSort"], "input": [4, 1, 3, 0, 2] }
```

Für den SSE-Stream stehen `dataset`, `count`, `speed` und optional eine als JSON-Liste codierte `input` zur Verfügung.

## Bilder

Die [Galerie](GALERIE.md) zeigt echte Browseraufnahmen der unveränderten Anwendung mit 32 Elementen. Laufzeitwerte sind Beispiele dieses Durchlaufs.

[Zur Übersicht](../README.md). Alle Angaben und Downloads ohne Gewähr.
