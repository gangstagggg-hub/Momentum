plugins {
    id("com.android.application")
}

android {
    namespace = "io.github.gangstagggg.momentum"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.gangstagggg.momentum"
        minSdk = 24
        targetSdk = 36
        versionCode = (System.getenv("VERSION_CODE") ?: "1").toInt()
        versionName = "2.0." + versionCode
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
    // Serves the web app that is bundled inside the APK to the WebView, over a
    // secure https address, so localStorage works and nothing is loaded from the internet
    implementation("androidx.webkit:webkit:1.12.1")
    // Window insets (edge-to-edge) and FileProvider (camera photos)
    implementation("androidx.core:core:1.17.0")
    // Back button handling that works on both older and newer Android versions
    implementation("androidx.activity:activity:1.10.1")
}
