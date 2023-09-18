rootProject.name = "calculator"

include(
    "server",
    "api",
    "app"
)

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://jitpack.io")
    }
}