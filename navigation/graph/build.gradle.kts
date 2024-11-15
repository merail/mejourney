plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "merail.life.navigation.graph"
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

    packaging {
        resources {
            pickFirsts += "/META-INF/{NOTICE.md,LICENSE.md}"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.navigation.compose)

    implementation(project(":core"))
    implementation(project(":design"))
    implementation(project(":navigation:domain"))
    implementation(project(":data"))
    implementation(project(":splash"))
    implementation(project(":home"))
    implementation(project(":auth:api"))
    implementation(project(":auth:impl"))
}