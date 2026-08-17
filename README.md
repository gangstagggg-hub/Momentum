# Momentum – treningslogg (PWA)

Dette er en ferdig, installerbar mini-app (PWA). Følg stegene under for å legge den ut på GitHub Pages og åpne den på telefonen.

## ⚠️ Viktig: hvorfor dette er annerledes enn å bare åpne HTML-filen

Om du bare laster ned `index.html` og åpner den direkte på telefonen (eller "Legg til på hjem-skjerm" fra en lokal fil), vil nettleseren ofte behandle hver nedlastede kopi som en **ny, egen adresse**. Det betyr at data du har lagt inn kan forsvinne neste gang du får en oppdatert fil.

Løsningen er å laste opp disse filene til GitHub Pages **én gang**, slik at appen får en **fast nettadresse**. Da lagres dataen din trygt knyttet til den adressen, og forsvinner ikke selv om du senere oppdaterer koden (så lenge adressen forblir den samme).

## 1. Last opp til GitHub

1. Gå til [github.com](https://github.com) og logg inn (opprett konto om du ikke har).
2. Trykk **New repository**.
3. Gi det et navn, f.eks. `momentum-app`. La det være **Public**. Ikke kryss av for README (den ligger allerede her).
4. Trykk **Create repository**.
5. På neste side, trykk **uploading an existing file**.
6. Dra inn ALLE filene fra denne mappen (også `icons`-mappen med innhold):
   - `index.html`
   - `manifest.json`
   - `service-worker.js`
   - `icons/` (hele mappen)
7. Trykk **Commit changes**.

## 2. Skru på GitHub Pages

1. I repoet, gå til **Settings** → **Pages** (i menyen til venstre).
2. Under **Build and deployment** → **Source**, velg **Deploy from a branch**.
3. Velg branch `main` og mappe `/ (root)`. Trykk **Save**.
4. Vent ca. 1 minutt. Adressen din vises øverst, noe sånt som:
   `https://dittbrukernavn.github.io/momentum-app/`

## 3. Åpne på telefonen (Android/Chrome)

1. Åpne lenken fra steg 2 i **Chrome** på telefonen.
2. Chrome bør automatisk vise en boks nederst: **"Installer Momentum"** — trykk **Installer**.
   - Dukker den ikke opp med en gang? Trykk på de tre prikkene øverst til høyre i Chrome → **Legg til på Hjem-skjerm**.
3. Appen legger seg som et vanlig app-ikon, og åpnes i eget vindu (uten Chrome-adresselinje).

## Om lagring

Alt du legger inn (øvelser, reps, vekt, kommentar, bilde) lagres **lokalt på telefonen din** (i nettleserens/app-ens lagring). Det sendes ikke til noen server. Sletter du appen eller nettleserdataene, forsvinner også loggen — så ikke tøm nettleserdata for siden ved et uhell.

## Endre appen senere

Vil du endre noe (farger, tekst, funksjoner), rediger `index.html` og last opp filen på nytt til GitHub (samme steg som over — "Add file" → "Upload files" → overskriv).
