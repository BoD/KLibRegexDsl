plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("maven-publish")
    id("signing")
}

group = "org.jraf"
version = "1.0.0"
description = "KLibRegexDsl"

tasks {
    // Generate Javadoc (Dokka) Jar
    register<Jar>("dokkaHtmlJar") {
        archiveClassifier.set("javadoc")
        from("$buildDir/dokka")
        dependsOn(dokkaHtml)
    }
}

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

publishing {
    repositories {
        maven {
            // Note: declare your user name / password in your home's gradle.properties like this:
            // mavenCentralNexusUsername = <user name>
            // mavenCentralNexusPassword = <password>
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            name = "mavenCentralNexus"
            credentials(PasswordCredentials::class)
        }
    }

    publications.withType<MavenPublication>().forEach { publication ->

        publication.artifact(tasks.getByName("dokkaHtmlJar"))

        publication.pom {
            name.set("klibregexdsl")
            description.set("A tiny Regex DSL library for Kotlin Multiplatform.")
            url.set("https://github.com/BoD/KLibRegexDsl")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id.set("BoD")
                    name.set("Benoit 'BoD' Lubek")
                    email.set("BoD@JRAF.org")
                    url.set("https://JRAF.org")
                    organization.set("JRAF.org")
                    organizationUrl.set("https://JRAF.org")
                    roles.set(listOf("developer"))
                    timezone.set("+1")
                }
            }
            scm {
                connection.set("scm:git:https://github.com/BoD/KLibRegexDsl")
                developerConnection.set("scm:git:https://github.com/BoD/KLibRegexDsl")
                url.set("https://github.com/BoD/KLibRegexDsl")
            }
            issueManagement {
                url.set("https://github.com/BoD/KLibRegexDsl/issues")
                system.set("GitHub Issues")
            }
        }
    }
}

signing {
    // Note: declare the signature key, password and file in your home's gradle.properties like this:
    // signing.keyId=<8 character key>
    // signing.password=<your password>
    // signing.secretKeyRingFile=<absolute path to the gpg private key>
    sign(publishing.publications)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootProject.file("docs"))
}

// Run `./gradlew refreshVersions` to update dependencies
// Run `./gradlew dokkaHtml` to generate the docs
// Run `./gradlew publishToMavenLocal` to publish to the local maven repo
// Run `./gradlew publish` to publish to Maven Central (then go to https://oss.sonatype.org/#stagingRepositories and "close", and "release")
