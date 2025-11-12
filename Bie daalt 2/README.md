# Бие Даалт №2 — Бичвэрийг жигдлэх (Greedy vs DP)

Энэ төсөл нь бичвэрийн зүүн, баруун ирмэгийг жигдлэх асуудлыг хоёр аргаар (Greedy, Dynamic Programming) хэрэгжүүлж, тестлэж, харьцуулна. Хэлүүд: Java (Maven + JUnit 5) болон Python (unittest).

## Төслийн Бүтэц
- Python: `Bie daalt 2/python/`
  - Алгоритмууд: `text_justify/algorithms_greedy.py`, `text_justify/algorithms_dp.py`
  - CLI: `main.py`
  - Тест: `tests/test_justify.py`
  - Жишээ бичвэр: `samples/en.txt`, `samples/mn.txt`
- Java (Maven): `Bie daalt 2/java/`
  - Алгоритмууд: `GreedyJustifier.java`, `DPJustifier.java`
  - CLI: `App.java`
  - Тест: `src/test/java/com/example/justify/JustifyTest.java`

## Шаардлага
- Python 3.x
- Java 17+ ба Maven

## Ажиллуулах — Python (Interactive)
path: cd "C:\Users\irmuu\OneDrive\Documents\3A\Algorithm-iin-shinjilgee-ba-zohiomj\Bie daalt 2\python"
1) Орчны бэлтгэл (анх удаа):
   - `python -m venv .venv`
   - `.venv\\Scripts\\Activate.ps1`
      - Ажиллахгүй байвал: `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned`
   - `python -m pip install --upgrade pip`
2) Ажиллуулах:
   - `python "main.py"`
   - Дараалсан асуултад хариулна: мөрийн өргөн, алгоритм (1=DP, 2=greedy),
     дараа нь текстээ оруулаад хоосон мөрөөр дуусгана.
   - Дууссан бол: `deactivate`
3) Тест:
   - `python -m unittest discover -s "tests" -v`

## Ажиллуулах — Java (Maven, Interactive)
1) `cd "Bie daalt 2/java"`
2) Build: `mvn -q -DskipTests package`
3) Run: `java -cp target/justify-1.0-SNAPSHOT.jar com.example.justify.App`
4) Дараалсан асуултад хариул: мөрийн өргөн, алгоритм, текст (хоосон мөрөөр дуусгана).
5) Тест: `mvn -q test`

## Алгоритмын Тайлбар (MN)
- Greedy: Нэг мөрт багтах хүртэл үгийг цувуулж аваад, сул зайг тэнцүү (боломжит хэмжээгээр) хуваарилж баруун ирмэгийг зэрэгцүүлнэ. Сүүлийн мөрийг зүүн талдаа жигд байлгана.
- DP: Бүх мөрийн таслалуудын нийлбэр зардлыг хамгийн бага болгоно. Мөрийн зардлыг `(extra_spaces^3)` гэж авна. Эцсийн мөрийн зардал `0`. Сүүлээс урагш `OPT[i] = min_j{ cost(i..j) + OPT[j+1] }` байдлаар бодож, таслалуудыг сэргээнэ.

## Algorithm Description (EN)
- Greedy: Pack as many words as fit on the current line, then distribute spaces to align both margins; the last line is left-justified.
- DP: Choose line breaks to minimize the total badness over the paragraph with `badness = (extra_spaces)^3`, assigning `0` to the last line; reconstruct optimal breaks from right to left.

## Зөв Байдлын Баталгаа
- Greedy — Loop Invariant: Одоог хүртэл үүсгэсэн мөрүүдийн үгс эх бичвэрийн дарааллаар байрласан, сүүлийн мөрөөс бусад нь яг `width` урттай, үлдсэн үгсийн хамгийн эхнийх нь дараагийн мөрийн эхэнд очно. Мөр бүрийг дуусгахад invariant хадгалагдана, төгсгөлд бүх үг байрласан байна.
- DP — Induction: `OPT[k]` нь `k` дугаартай үгээс төгсгөл хүртэлх хамгийн бага зардал гэдэг баталгаа. Сүүл мөрийн хувьд үнэн (`OPT[n]=0`). Хэрвээ `OPT[j+1]` хамгийн бага гэж үзвэл `i..j` гэсэн эхний мөрийн зардалд `OPT[j+1]`‑ийг нэмэх нь `OPT[i]`‑гийн хамгийн бага утгыг өгнө. Иймээс буцааж нөхөн сэргээхэд глобал оптималь хуваарилалт гарна.

## Комплексити
- Greedy: O(N) — үгс/тэмдэгтийн тоонд шугаман. Санах ой: O(1) нэмэлт (мөр бүр форматлах үед).
- DP: O(N^2) (муугаар тооцвол). Санах ой: O(N) (OPT массив, таслалтын индексүүд).

## Аль нь тохиромжтой вэ?
- Хурд, энгийн байдлыг чухалчлавал Greedy хангалттай сайн, маш хурдан.
- Илүү тэгш/боломжит хамгийн сайн хуваарилалт (global optimal) хэрэгтэй бол DP нь тохиромжтой.

## Жишээ Оролт/Гаралт
Interactive горимд:
```
Мөрийн өргөн: 30
Алгоритмаа сонгоно уу: DP -> 1, greedy -> 2
> 2
Текстээ оруулна уу (хоосон мөрөөр дуусгана):
Greedy algorithms add as many words as fit on each line

Үр дүн:
Greedy algorithms add as many
words as fit on each line    
```

## Notes (EN)
- Supports Unicode (Mongolian/English) text.
- Last line is left-justified by design (as in common word processors).
- Paste your own text directly into the interactive prompt. (Sample files were removed.)
