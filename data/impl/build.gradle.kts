
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.kotlin.dsl.android

plugins {
    alias(libs.plugins.library.plugin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger.hilt)
}

android {
    buildFeatures {
        buildConfig = true
    }

    val localProperties = gradleLocalProperties(rootDir, providers)

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "DOMAIN_URL",
                value = "\"${localProperties.getProperty("domainUrl")}\"",
            )

            buildConfigField(
                type = "String",
                name = "ACCESS_TOKEN",
                value = "\"${localProperties.getProperty("accessToken")}\"",
            )
        }

        release {
            buildConfigField(
                type = "String",
                name = "DOMAIN_URL",
                value = "\"${localProperties.getProperty("domainUrl")}\"",
            )

            buildConfigField(
                type = "String",
                name = "ACCESS_TOKEN",
                value = "\"${localProperties.getProperty("accessToken")}\"",
            )
        }
    }
}

dependencies {
    testImplementation(libs.androidx.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(projects.core)
    implementation(projects.data.api)
}