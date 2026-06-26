plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.example.katalog_yeni"
    compileSdk = 35 // Sabitlendi

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    defaultConfig {
        applicationId = "com.example.katalog_yeni"
        minSdk = flutter.minSdkVersion // Sabitlendi
        targetSdk = 35 // Sabitlendi
        versionCode = 1 // Sabitlendi
        versionName = "1.0.0" // Sabitlendi
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

flutter {
    source = "../.."
}
