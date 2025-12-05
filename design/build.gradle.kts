plugins {
    alias(libs.plugins.library.plugin)
    alias(libs.plugins.kotlin.compose)
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation(libs.compose.shimmer)

    implementation(projects.core)
}