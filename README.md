# hokotrok

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

**💡 Extra tipp a közös munkához neked (Adminnak):** Mivel megbeszéltük, hogy a `main` ágba ne toljon senki közvetlenül kódot, érdemes ezt a GitHubon le is védeni (ezt hívják **Branch Protection**-nek). Így a rendszer fizikailag sem engedi a `main`-re pusholást, csak Pull Requesteken (PR) keresztül.

Szeretnéd, hogy megmutassam, hol tudod bekapcsolni ezt a védelmet a repó beállításaiban? Ez életmentő egy ekkora projektnél!
