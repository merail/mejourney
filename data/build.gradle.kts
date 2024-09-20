import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "merail.life.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 30
    }

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "FIREBASE_REPOSITORY_PATH",
                value = properties.getProperty("devFirebaseRepositoryPath"),
            )
            buildConfigField(
                type = "String",
                name = "FIREBASE_STORAGE_BUCKET",
                value = properties.getProperty("devFirebaseStorageBucket"),
            )
        }

        release {
            buildConfigField(
                type = "String",
                name = "FIREBASE_REPOSITORY_PATH",
                value = properties.getProperty("prodFirebaseRepositoryPath"),
            )
            buildConfigField(
                type = "String",
                name = "FIREBASE_STORAGE_BUCKET",
                value = properties.getProperty("prodFirebaseStorageBucket"),
            )
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
    implementation(libs.firebase.firestore)

    implementation(libs.kotlinx.immutable.collections)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    implementation(project(":core"))
    implementation(project(":data:database"))
    implementation(project(":data:server"))
}