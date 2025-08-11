plugins {
    alias(libs.plugins.androidLib)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "merail.life.navigation"
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
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.navigation.compose)

    implementation(project(":core"))
    implementation(project(":design"))
    implementation(project(":data:api"))
    implementation(project(":home"))
    implementation(project(":error"))
}