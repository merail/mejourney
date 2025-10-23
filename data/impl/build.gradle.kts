
import org.gradle.kotlin.dsl.android
import java.util.Properties

plugins {
    alias(libs.plugins.library.plugin)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger.hilt)
}

android {
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
    testImplementation(libs.androidx.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(projects.core)
    implementation(projects.data.api)
}