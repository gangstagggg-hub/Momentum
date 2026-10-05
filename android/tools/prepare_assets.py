#!/usr/bin/env python3
"""
Copies the Momentum web app (index.html in the root of the repository) into the Android
project, so the app can run without internet.

What it changes in the Android copy only (the website itself is left alone):
  * removes the links to Google Fonts / Fontshare and uses the fonts in tools/fonts instead
  * removes the web-install tags (manifest, favicon) which the Android app does not need

Run it from anywhere:  python3 android/tools/prepare_assets.py
"""
import re
import shutil
import sys
from pathlib import Path

HERE = Path(__file__).resolve().parent            # .../android/tools
ANDROID = HERE.parent                             # .../android
ROOT = ANDROID.parent                             # repository root
SRC = ROOT / "index.html"
OUT = ANDROID / "app" / "src" / "main" / "assets" / "www"


def fail(message):
    print(f"ERROR: {message}", file=sys.stderr)
    sys.exit(1)


if not SRC.is_file():
    fail(f"Could not find {SRC}. The web app's index.html must be in the root of the repository.")

html = SRC.read_text(encoding="utf-8")

# 1. No external font requests: the app must work offline
html = re.sub(
    r'<link[^>]*(?:fonts\.googleapis\.com|fonts\.gstatic\.com|fontshare\.com)[^>]*>\s*', "", html
)

# 2. Tags that only make sense for the installable website
html = re.sub(r'<link[^>]*rel="(?:manifest|icon|apple-touch-icon)"[^>]*>\s*', "", html)

# 3. Local fonts
fonts_dir = HERE / "fonts"
font_css = []
for weight in (700, 800):
    if (fonts_dir / f"syne-{weight}.woff2").is_file():
        font_css.append(
            "@font-face{font-family:'Syne';font-style:normal;font-weight:%d;font-display:swap;"
            "src:url('fonts/syne-%d.woff2') format('woff2');}" % (weight, weight)
        )
if not font_css:
    fail("No fonts found in android/tools/fonts")
if "</head>" not in html:
    fail("index.html has no </head> tag")
html = html.replace("</head>", "<style>" + "".join(font_css) + "</style>\n</head>", 1)

# 4. Sanity checks: nothing should point at the internet any more
leftovers = re.findall(r'(?:src|href)="(https?://[^"]+)"', html)
if leftovers:
    fail("index.html still loads from the internet: " + ", ".join(leftovers))

# Write the Android copy
if OUT.exists():
    shutil.rmtree(OUT)
(OUT / "fonts").mkdir(parents=True)
(OUT / "index.html").write_text(html, encoding="utf-8")
for f in fonts_dir.glob("*.woff2"):
    shutil.copy2(f, OUT / "fonts" / f.name)

print(f"Prepared {OUT / 'index.html'} ({len(html) / 1024:.0f} KB) with {len(font_css)} bundled font weights")
