plugins {
    kotlin("multiplatform")
    `maven-publish`
}

group = "org.jraf"
version = "1.0.0"
description = "KLibRegexDsl"

kotlin {
    jvm()
    js(IR) {
        browser()
        nodejs()
    }

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
    }
}

// Run `./gradlew refreshVersions` to update dependencies
// Run `./gradlew publishToMavenLocal` to publish to the local maven repo
