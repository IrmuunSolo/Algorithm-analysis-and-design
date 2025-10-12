# Улаанбаатарын Зам Төлөвлөгч (Java/Maven, Swing + JXMapViewer2)

Энэ төсөл нь Улаанбаатар хотын хүрээнд замын мэдээлэл (OSM shapefile) ашиглан
замын сүлжээ үүсгэж, зураг дээр эхлэл/төгсгөлийг дарж маршрутыг тооцон дүрсэлдэг жижиг desktop програм юм.

## Гол боломжууд
- Zoom: хулганаар чирэх, zoom хийх.
- UB bbox: газрын зургийг Улаанбаатарын хүрээнд хязгаарлана.
- Алгоритмууд: Dijkstra ( хугацаагаар жинлэнэ ), BFS (дамжих зангилааны тоо),
  DFS (аливаа нэг боломжит зам).
- Баруун талын самбарт үр дүнгүүд: алгоритм, зангилаа, зай (км),
  тооцоолсон хугацаа (мин), гүйцэтгэлийн хугацаа (ms), санах ой (MB).

## Шаардлага
- JDK 22 (компайл нь `--release 21` тул Java 21 байт код гарна)
- Maven 3.8+

## Программ ажиллуулах
```
mvn -q package
mvn -q exec:java
```

Тест ажиллуулах:
```
mvn -q test
```

## Өгөгдөл (mapdata хавтас)
`mapdata/` дотор Geofabrik‑ийн Монголын замын shapefile байрлуулна:
- `mapdata/gis_osm_roads_free_1.shp` (+ `.dbf/.shx/.prj/.qix`)

Татах зааврыг `mapdata/README.md` файлд оруулав. Том файлуудыг Git-д оруулахгүй, локалд байршуулж ашиглахаар `.gitignore` тохируулсан.

## Жин тооцох зарчим
- Ирмэгийн жин = зорчих хугацаа (секунд) = сегментийн урт / хурд.
- `maxspeed` байвал шууд ашиглана, үгүй бол `fclass`‑аас анхдагч хурдыг авна.
- Машинд тохироогүй шугамуудыг (`access`, `fclass`‑аар) шүүнэ.
- Нэг чигийн замуудыг (oneway) мөрдөнө.

## Хяналт/Товчнууд
- Load UB Roads: shapefile‑ийг уншиж граф үүсгэнэ.
- Зураг дээр дарах: эхний даралт нь Start, дараагийн даралт нь End.
- Route: сонгосон алгоритмаар маршрут тооцоолж зурна.
- Clear: сонголт болон маршрутыг цэвэрлэнэ.

## Төслийн бүтэц
- `src/main/java/com/example/ui/UBMapFrame.java` — Swing UI
- `src/main/java/com/example/data/ShapefileRoadGraphLoader.java` — Shapefile уншигч
- `src/main/java/com/example/graph/*` — графын бүтэц
- `src/main/java/com/example/Algorithms/*` — BFS, DFS, Dijkstra
- `src/test/java/com/example/Algorithms/AlgorithmsTest.java` — энгийн unit тестүүд


