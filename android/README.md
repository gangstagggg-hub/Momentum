# Momentum — Android (TWA, uten PWABuilder)

Dette er et ekte, minimalt Android-prosjekt som bruker Googles eget
`androidbrowserhelper`-bibliotek (samme bibliotek PWABuilder/Bubblewrap bruker
under panseret) — men skrevet for hånd med riktig API-nivå (36) fra start,
siden PWABuilder sitt eget verktøy er fastlåst på API 35.

Appen viser Momentum (`https://gangstagggg-hub.github.io/momentum-app/`) i
fullskjerm, uten adressefelt — så lenge `assetlinks.json`-steget nedenfor
gjøres riktig.

## 1. Omorganisering før du laster opp til GitHub

Alt i denne zip-filen (`app/`, `.gitignore`, `build.gradle.kts`,
`gradle.properties`, `README.md`, `settings.gradle.kts`) skal ligge i en mappe
kalt **`android`** i repoet ditt — bortsett fra **`.github`**, som skal ligge
helt i roten av repoet (ved siden av `android`-mappen, ikke inni den), slik at
GitHub Actions faktisk finner arbeidsflyten.

Endelig struktur i repoet:
```
ditt-repo/
├── .github/
│   └── workflows/
│       └── android.yml
├── android/
│   ├── app/
│   ├── .gitignore
│   ├── build.gradle.kts
│   ├── gradle.properties
│   ├── README.md
│   └── settings.gradle.kts
└── (resten av Momentum-nettsiden din)
```

## 2. Lag en signeringsnøkkel (keystore)

Du trenger en **egen, ny** nøkkel for Momentum — ikke samme fil som Wage
Counter. På din egen PC (krever Java/JDK installert, eller bruk Android
Studios innebygde `keytool`):

```
keytool -genkeypair -v -keystore momentum-upload.jks -alias momentum -keyalg RSA -keysize 2048 -validity 10000
```

Ta vare på **passordet** og **alias** du velger — du trenger dem i steg 3.
Denne filen skal aldri lastes opp til GitHub.

## 3. Legg til GitHub Secrets

I repoet: **Settings → Secrets and variables → Actions → New repository
secret**. Legg til disse fire:

| Navn | Verdi |
|---|---|
| `UPLOAD_KEYSTORE_BASE64` | Base64-kodet innhold av `momentum-upload.jks` |
| `UPLOAD_KEYSTORE_PASSWORD` | Passordet du valgte i steg 2 |
| `UPLOAD_KEY_ALIAS` | Alias du valgte i steg 2 (f.eks. `momentum`) |
| `UPLOAD_KEY_PASSWORD` | Nøkkelpassordet (ofte samme som keystore-passordet) |

For å base64-kode filen i PowerShell:
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("momentum-upload.jks")) | Set-Clipboard
```
Lim så inn direkte i secret-verdien.

## 4. Kjør arbeidsflyten

**Actions**-fanen → **"Android Build"** → **"Run workflow"**. Når den er
ferdig, last ned `.aab`-filen fra **Artifacts**.

## 5. assetlinks.json — den vanligste fallgruven

For at appen skal åpnes i **fullskjerm uten adressefelt**, må Google kunne
bekrefte at du eier både appen og nettsiden. Dette krever en fil på:

```
https://gangstagggg-hub.github.io/.well-known/assetlinks.json
```

**Viktig:** dette er **domenets rot** (`gangstagggg-hub.github.io`), ikke
`.../momentum-app/.well-known/...`. Dette løste vi for Wage Counter ved å
opprette et eget repo kalt nøyaktig `gangstagggg-hub.github.io` som GitHub
Pages serverer fra roten.

**Hvis det repoet allerede finnes** (fra Wage Counter), skal du **ikke**
overskrive filen — `assetlinks.json` er en liste som kan inneholde flere
apper. Åpne den eksisterende filen og **legg til et nytt objekt** i arrayet:

```json
{
  "relation": ["delegate_permission/common.handle_all_urls"],
  "target": {
    "namespace": "android_app",
    "package_name": "io.github.gangstagggg.momentum",
    "sha256_cert_fingerprints": ["SHA-256-FINGERPRINTET_DITT_HER"]
  }
}
```

### Hvor du finner riktig fingerprint

Etter at du har lastet opp `.aab`-filen til Play Console minst én gang:
**Play Console → ditt Momentum-prosjekt → Setup → App integrity → App
signing** → kopier **SHA-256**-verdien under **"App signing key
certificate"**.

To ting folk (inkludert oss, under Wage Counter) går i fella på:
- Det skal være **App signing key**-fingerprintet, **ikke** "Upload key
  certificate"
- Det skal være **SHA-256**, **ikke** SHA-1

## 6. Kjente, ufarlige ting du vil se

- Første gang appen åpnes, viser Chrome en **engangs-melding** om at siden
  "kjører i Chrome". Dette er standard, obligatorisk oppførsel for Trusted
  Web Activities og kan ikke fjernes — det vises kun én gang per installasjon.
- Versjonsnummeret (`versionCode`) settes automatisk av GitHub Actions sitt
  kjøringsnummer — det økes av seg selv ved hver kjøring, du trenger ikke
  justere det manuelt i `build.gradle.kts`.

## 7. Hvis appen krasjer på telefonen

Bruk ADB for å se den faktiske feilmeldingen i stedet for å gjette:

```powershell
adb logcat | Select-String "momentum|FATAL EXCEPTION|AndroidRuntime"
```

Koble telefonen til PC-en med USB, slå på **USB-feilsøking** i
utviklerinnstillinger, installer appen, og se etter stack-trace i terminalen
i det øyeblikket den krasjer. Dette var den eneste metoden som ga et 100 %
sikkert svar under feilsøkingen av Wage Counter — gjetting fra kildekode
alene førte oss på to blindspor først.
