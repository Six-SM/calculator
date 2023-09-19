import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.compose") version "1.5.1"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(project(mapOf("path" to ":api")))
    // TODO: move all these dependencies to libs.version.toml
    implementation("io.ktor:ktor-client-core:${libs.versions.ktorVersion.get()}")
    implementation("io.ktor:ktor-client-cio:${libs.versions.ktorVersion.get()}")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:${libs.versions.ktorVersion.get()}")
    implementation("io.ktor:ktor-client-content-negotiation:${libs.versions.ktorVersion.get()}")
    implementation(project(":api"))
}

compose.desktop {
    application {
        mainClass = "org.ssm.app.AppKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "calculator"
            packageVersion = "1.0.0"
        }
    }
}
