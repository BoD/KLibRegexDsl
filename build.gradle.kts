plugins {
    kotlin("multiplatform") apply false
    kotlin("jvm") apply false
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

// Run `./gradlew refreshVersions` to update dependencies
