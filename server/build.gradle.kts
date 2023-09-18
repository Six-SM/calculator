plugins {
    application

    id("com.github.johnrengelman.shadow") version "8.1.1"

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

application {
    mainClass.set("org.ssm.server.ServerKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}


tasks.shadowJar {
    archiveFileName.set(project.name + "-shadow.jar")
    archiveClassifier.set("")
    archiveVersion.set("")
}

// TODO: move all these dependencies to libs.version.toml
dependencies {
    implementation(project(":api"))
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-compression-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("com.github.Keelar:ExprK:91fdabf")

    implementation("org.ktorm:ktorm-core:3.6.0")

    testImplementation("io.ktor:ktor-server-tests-jvm")

    testImplementation("org.testcontainers:postgresql:1.17.6")
}