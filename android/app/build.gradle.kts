plugins {
    id("com.android.application")
}

android {
    namespace = "io.github.gangstagggg.momentum"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.gangstagggg.momentum"
        minSdk = 23
        targetSdk = 36
        versionCode = (System.getenv("VERSION_CODE") ?: "1").toInt()
        versionName = "1.0." + versionCode
    }

    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("UPLOAD_KEYSTORE_PATH")
            if (keystorePath != null) {
                storeFile = file(keystorePath)
                storePassword = System.getenv("UPLOAD_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("UPLOAD_KEY_ALIAS")
                keyPassword = System.getenv("UPLOAD_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("com.google.androidbrowserhelper:androidbrowserhelper:2.7.3")

    // Pinned explicitly to match the exact versions androidbrowserhelper 2.7.3
    // itself is built and tested against (confirmed from its own version
    // catalog). LauncherActivity calls WindowCompat.enableEdgeToEdge() as the
    // very first line of onCreate() — if an older/mismatched androidx.core
    // gets resolved transitively instead, the app crashes immediately on
    // launch. Pinning removes any ambiguity in dependency resolution.
    implementation("androidx.core:core:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.browser:browser:1.10.0")
}
