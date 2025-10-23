plugins {
    alias(libs.plugins.library.plugin)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger.hilt)
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.config)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(projects.core)
    implementation(projects.auth.api)
}