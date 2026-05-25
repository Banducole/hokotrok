# Hóeltakarítási Szimulátor — Prototípus

Körökre osztott városi hóeltakarítási szimulátor parancssori (CLI) prototípusa Java-ban.
Az `IMPLEMENTATION_PLAN.md` alapján készült.

## Könyvtárszerkezet

```
proto/
├── src/             37 Java fájl (forráskód)
├── tesztek/         21 teszt input + .out kimenetek
├── out/             lefordított .class fájlok
├── build.sh         fordító szkript
└── eval_tests.sh    automatizált tesztelő szkript
```

## Fordítás és futtatás

### Telepítendő
JDK 11 vagy újabb (a projekt OpenJDK 21-en lett tesztelve).

### Build
```bash
./build.sh
```
Lefordítja az összes `src/*.java` fájlt az `out/` mappába.

### Egyetlen teszt futtatása
```bash
cat tesztek/t01.txt | java -cp out Proto > 1.out
cat 1.out
```

### Az összes teszt egyben
```bash
./eval_tests.sh
```
Lefuttatja mind a 21 tesztesetet és kiírja, mi ment át.

## Bemeneti nyelv (parancsok)

A `Proto` standard bemenetből olvas, soronként egy parancs.

| Parancs | Példa |
|---|---|
| `Csomopont <név> <típus>` | `Csomopont c1 Home` (Home, Workplace, Intersection, Terminal) |
| `Ut <név>` | `Ut u1` |
| `Sav <név>` | `Sav s1` |
| `Kapcsol <út> <csomA> <csomB>` | `Kapcsol u1 c1 c2` |
| `Hozzaad_Sav <út> <sáv>` | `Hozzaad_Sav u1 s1` |
| `Allapot <sáv> <állapot>` | `Allapot s1 IcyState` |
| `Jarmu <név> <típus>` | `Jarmu auto1 Car` (Car, Bus) |
| `Hokotro <név>` | `Hokotro hk1` |
| `Felszerel <hókotró> <fej>` | `Felszerel hk1 Sweep` (Sweep, Throw, IceBreaker, Salt, Dragon, Rock) |
| `Toltes <hókotró> <típus> <menny>` | `Toltes hk1 Salt 5` |
| `Raallit <jármű\|kotró> <sáv>` | `Raallit auto1 s1` |
| `Uticel_Auto <autó> <home> <work>` | `Uticel_Auto auto1 c1 c2` |
| `Uticel_Busz <busz> <vegA> <vegB>` | `Uticel_Busz busz1 v1 v2` |
| `Ho <sáv> <vastagság>` | `Ho s1 3` |
| `Auto_Lep <autó> <random>` | `Auto_Lep auto1 false` |
| `Busz_Lep <busz> <út> <sáv> <random>` | `Busz_Lep busz1 u1 s1 false` |
| `Hokotro_Lep <kotró> <út> <sáv>` | `Hokotro_Lep hk1 u1 s1` |
| `Allapot_Auto <jármű> Blocked=N` | `Allapot_Auto auto1 Blocked=2` |
| `Takarito <név> <pénz>` | `Takarito cp1 1000` |
| `Buszsofor <busz>` | `Buszsofor busz1` |
| `Vesz_Fej <takarító> <kotró> <fej>` | `Vesz_Fej cp1 hk1 Throw` |
| `Vesz_Hokotro <takarító> <utolsóKotró> <újNév>` | `Vesz_Hokotro cp1 hk1 hk2` |
| `Stat <típus> <név>` | `Stat Lane s1` |

## Kimeneti formátum

```
[ACTION] <objektum>: <esemény>
[STAT]   <típus> <név> <kulcs>=<érték> ...
[ERROR]  <hiba leírás>
```

## Eredmény

```
=== Summary: 21 passed, 0 failed ===
```

Mind a 21 teszteset (T01–T21) sikeresen lefut.

## Implementációs jegyzetek

A leglényegesebbeket lásd az `IMPLEMENTATION_PLAN.md` 0. fejezetében (feloldott
inkonzisztenciák), itt csak emlékeztetőül:

- `Head.java` nem készült el — a `CleanerHead` az egyetlen absztrakt fej-osztály.
- `RockHead` és `Rock` (zúzottkő-szóró) implementálva van — a `lane.rocky` zászló
  felülbírálja az `IcyState` és `BrokenIceState` csúszósságát.
- A `SweepHead` a havat **és** a zúzalékot is áttolja a szomszéd sávba.
- A `Lane.isPassable()` a sáv állapotát ÉS a rajta lévő blokkolt járműveket is
  figyeli.
- A `RegisterPassage` először növeli a számlálót, majd ellenőrzi a küszöböt.
