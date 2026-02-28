# hokotrok

## 📝 Dokumentációk
** templ_02_0 google docs linkje:
 https://docs.google.com/document/d/1aJOzo8bXO5q7WdpFAm290eykW0iiMC50QNJk3B2h_eQ/edit?usp=sharing

### 🚀 Git Gyorstalpaló a Projektkhez

Szevasztok! Mivel be lettetek állítva a GitHub repóba, itt vannak a legfontosabb parancsok, amikkel dolgozni fogunk.

**1. A kód letöltése a gépedre (Ezt csak legelőször kell megcsinálni)**
Nyiss egy terminált abban a mappában, ahova a projektet akarod tenni, és írd be ezt (a linket a GitHub repó zöld "Code" gombjára kattintva tudod kimásolni):

```bash
git clone <ide_jön_a_github_repo_linkje>

```

*Ezután lépj be a letöltött mappába: `cd hokotrok*`

**2. Saját "homokozó" létrehozása (KÖTELEZŐ!)**
Közös szabály: **Közvetlenül a `main` ágba nem pusholunk!** Ha elkezdesz dolgozni egy új funkción vagy javításon, mindig csinálj neki egy saját ágat (branch):

```bash
git checkout -b <az-uj-ag-neve>

```

*(Pl.: `git checkout -b login-oldal-keszitese` vagy `git checkout -b adoskex-teszt`)*

**3. Módosítások elmentése (Miután írtál valami kódot)**
Ha készen vagy egy résszel, és el akarod menteni a Gitbe:
Minden megváltoztatott fájl hozzáadása:

```bash
git add .

```

Mentés egy üzenettel, hogy mit csináltál:

```bash
git commit -m "Megcsináltam a bejelentkezés gomb dizájnját"

```

**4. Kód felküldése a GitHubra (Push)**
Ha elmentetted a kódot (commitoltál), fel kell küldeni a közös repóba a saját ágadra:

```bash
git push origin <az-uj-ag-neve>

```

*(Ha először pusholsz egy új ágat, a Git lehet, hogy kiír egy hosszabb parancsot, egyszerűen csak másold ki és nyomj entert).*

**5. A többiek kódjának letöltése (Pull)**
Mielőtt új dologba kezdesz, érdemes mindig letölteni a legfrissebb közös kódot a `main` ágról, hogy ne a régi verzión dolgozz:

```bash
git checkout main
git pull

```
---

## 🛠 Hogyan küldj be kódot? (Pull Request Kisokos)

Mivel a `main` águnk védve van, közvetlenül oda nem tudsz pusholni. Minden új funkciót vagy javítást **Pull Request (PR)** formájában kell beküldeni, amit egy másik csapattagnak jóvá kell hagynia!

### 1. Kód beküldése (Pull Request nyitása)
1. Miután a terminálban megcsináltad a `git push origin <saját-ág-neved>` parancsot, menj fel a projekt GitHub oldalára.
2. Felül, sárgás háttérrel meg fog jelenni egy gomb: **"Compare & pull request"**. Kattints rá! *(Ha nem látod, menj a **Pull requests** fülre, és kattints a zöld **New pull request** gombra, majd válaszd ki az ágad).*
3. Adj neki egy beszédes címet (pl. *"Kész a bejelentkezés gomb"*), és röviden írd le, mit csináltál.
4. Kattints a zöld **Create pull request** gombra.

### 2. Kód átnézése és jóváhagyása (Review)
**Ezt sosem az csinálja, aki a kódot írta, hanem egy másik csapattag!**
1. Menj a GitHubon a **Pull requests** fülre, és nyisd meg a nyitott PR-t.
2. Kattints a **Files changed** fülre (itt látod pirossal, amit töröltek, és zölddel, amit hozzáadtak).
3. Nézd át a kódot. Ha minden király, kattints a jobb felső sarokban a zöld **Review changes** gombra.
4. Válaszd az **Approve** (Jóváhagyás) opciót, írhatsz egy dicsérő kommentet (pl. *"Faszán néz ki, mehet be"*), majd nyomj a **Submit review** gombra.

### 3. Összevonás (Merge)
1. Amint megvan az 1 darab jóváhagyás (zöld pipa), a PR alján zölddé válik a **Merge pull request** gomb.
2. Kattints rá, majd nyomj a **Confirm merge**-re.
3. Kész is! A kódod bekerült a `main` ágba, mindenki számára elérhető lett. 
*(Ezután a saját ágad akár törölheted is a "Delete branch" gombbal, hogy tiszta maradjon a repó).*
