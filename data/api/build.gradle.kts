import org.gradle.kotlin.dsl.android
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.lib)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "merail.life.data.api"
    compileSdk = 36

    defaultConfig {
        minSdk = 30
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JvmTarget.JVM_17.target
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(projects.core)
}