import org.gradle.kotlin.dsl.android
import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.ksp)
}

android {
    namespace = "merail.life.data.impl"
    compileSdk = 35

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

    buildFeatures {
        buildConfig = true
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
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)

    implementation(libs.kotlinx.immutable.collections)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(project(":core"))
    implementation(project(":data:api"))
}