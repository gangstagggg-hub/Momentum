# Momentum – Android (WebView, dataene ligger i appens egen mappe)

Dette er Android-appen for Momentum. Nettsiden (`index.html` i roten av repoet) pakkes
**inn i selve appen** når den bygges, og vises i en WebView.

## Hva som er annerledes enn før

| | Før (TWA) | Nå (WebView) |
|---|---|---|
| Hvor dataene ligger | I Chromes lagring | I **appens egen private mappe** |
| Påvirkes av å tømme Chrome | Ja | **Nei** |
| Fjernes når | Chrome-data tømmes | Appen avinstalleres eller lagringen tømmes i Android-innstillinger |
| Adresselinje øverst | Ja, til `assetlinks.json` er riktig | **Aldri** (trenger ikke `assetlinks.json`) |
| Virker uten nett | Etter første besøk | **Alltid** (alt ligger i appen, også skrifttypene) |
| Nye endringer i nettsiden | Vises automatisk | Krever ny bygging og ny utgave i Play |

Android sin automatiske sikkerhetskopi til Google er slått **av** for denne appen
(`allowBackup="false"`), så treningsdataene blir bare på telefonen.

## Slik tar du den i bruk (første gang)

1. **Last opp `android`-mappen** til repoet (Add file → Upload files, dra hele mappen).
   Filene med samme navn blir erstattet.
2. **Bytt ut arbeidsflyten:** åpne `.github/workflows/android.yml` på GitHub, trykk
   blyant-ikonet, erstatt hele innholdet med filen i denne pakken, og trykk Commit changes.
3. Sørg for at **`index.html` i roten av repoet er den nyeste** (fra `momentum-app.zip`).
   Arbeidsflyten kopierer akkurat denne filen inn i appen.
4. **Actions → Android Build → Run workflow.** Vent på grønn hake og last ned `.aab`-filen
   under *Artifacts*.
5. **Play Console → Intern testing → Opprett ny utgave**, last opp den nye `.aab`-filen
   og fullfør med *Start utrulling*.
6. Oppdater Momentum fra Play på telefonen. Appen starter **tom** første gang, fordi den
   har sin egen lagring og ikke kan hente data fra Chrome.

## Når du endrer nettsiden senere

Last opp ny `index.html` til repoet som vanlig. Det starter en ny bygging automatisk.
Last deretter ned den nye `.aab`-filen og legg den ut som en ny utgave i Play Console.

## Hvis noe ikke fungerer

Koble telefonen til PC med USB (USB-feilsøking på) og kjør:

```powershell
adb logcat | Select-String "momentum|FATAL EXCEPTION|AndroidRuntime|chromium"
```

## Teknisk

- Pakke-ID: `io.github.gangstagggg.momentum` (samme som før, så dette er en vanlig oppdatering)
- `minSdk 24`, `targetSdk 36`
- Nettsiden serveres fra `https://appassets.androidplatform.net/assets/www/` via
  `WebViewAssetLoader`, så `localStorage` virker og ingenting hentes fra internett
- Skrifttypen Syne (åpen lisens, SIL OFL) ligger i `tools/fonts`
- Tilbake-knappen lukker først åpne vinduer og går ett steg opp, og avslutter appen først
  når du står på startsiden
