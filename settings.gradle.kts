rootProject.name = "calculator"

include(
    "server",
    "api",
    "ui"
)

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://jitpack.io")
    }
}