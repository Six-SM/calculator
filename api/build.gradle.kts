plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization)
    implementation("org.ktorm:ktorm-core:3.6.0")
}