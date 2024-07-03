plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "merail.life.api.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 30
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "FIREBASE_STORAGE_BUCKET",
                value = "\"gs://mejourney-c86ca.appspot.com\"")
        }

        release {
            buildConfigField(
                type = "String",
                name = "FIREBASE_STORAGE_BUCKET",
                value = "\"gs://me-journey.appspot.com\"")
        }
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
}