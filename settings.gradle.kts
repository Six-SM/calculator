rootProject.name = "calculator"

include(
    "server",
    "api"
)

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://jitpack.io")
    }
}