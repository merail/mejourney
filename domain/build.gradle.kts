import org.gradle.kotlin.dsl.libs

plugins {
    alias(libs.plugins.java.lib)
    alias(libs.plugins.kotlin.jvm)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain(17)
}