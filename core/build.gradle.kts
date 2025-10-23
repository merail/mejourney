plugins {
    alias(libs.plugins.library.plugin)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger.hilt)
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}