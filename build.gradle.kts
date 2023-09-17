plugins {
    kotlin("jvm") version "1.9.10"
}

tasks.wrapper {
    gradleVersion = "8.2"
    distributionType = Wrapper.DistributionType.BIN
}

allprojects {
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    apply {
        plugin("kotlin")
    }

    tasks {
        test {
            useJUnitPlatform()
        }
    }

    dependencies {
        testImplementation(rootProject.libs.junit.jupiter)
        testRuntimeOnly(rootProject.libs.junit.engine)
    }
}
