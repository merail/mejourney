import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "merail.life.auth.impl"
    compileSdk = 35

    defaultConfig {
        minSdk = 30
    }

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "HOST_EMAIL",
                value = properties.getProperty("hostEmail"),
            )
            buildConfigField(
                type = "String",
                name = "HOST_PASSWORD",
                value = properties.getProperty("hostPassword"),
            )
        }

        release {
            buildConfigField(
                type = "String",
                name = "HOST_EMAIL",
                value = properties.getProperty("hostEmail"),
            )
            buildConfigField(
                type = "String",
                name = "HOST_PASSWORD",
                value = properties.getProperty("hostPassword"),
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

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.compose.icons)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.config)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.android.mail)
    implementation(libs.android.activation)

    implementation(project(":design"))
    implementation(project(":core"))
    implementation(project(":navigation:domain"))
    implementation(project(":auth:api"))
}