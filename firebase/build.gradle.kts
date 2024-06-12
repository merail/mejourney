plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "merail.life.firebase"
    compileSdk = 34

    defaultConfig {
        minSdk = 30
    }

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "FIREBASE_REPOSITORY_PATH",
                value = "\"dev\"")
            buildConfigField(
                type = "String",
                name = "FIREBASE_STORAGE_BUCKET",
                value = "\"gs://mejourney-c86ca.appspot.com\"")
        }

        release {
            buildConfigField(
                type = "String",
                name = "FIREBASE_REPOSITORY_PATH",
                value = "\"prod\"")
            buildConfigField(
                type = "String",
                name = "FIREBASE_STORAGE_BUCKET",
                value = "\"gs://me-journey.appspot.com\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    implementation(libs.kotlinx.immutable.collections)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(project(":core"))
}