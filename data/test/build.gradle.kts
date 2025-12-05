plugins {
    alias(libs.plugins.library.plugin)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger.hilt)
}

dependencies {
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.testing)
    ksp(libs.hilt.compiler)

    implementation(projects.core)
    implementation(projects.data.api)
    implementation(projects.data.impl)
}