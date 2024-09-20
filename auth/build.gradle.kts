import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "merail.life.auth"
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.parser.email)

    implementation("androidx.compose.material:material-icons-extended:1.7.0")

    implementation(files("libs/activation.jar"))
    implementation(files("libs/additionnal.jar"))
    implementation(files("libs/mail.jar"))

    implementation(project(":core"))
    implementation(project(":design"))
}