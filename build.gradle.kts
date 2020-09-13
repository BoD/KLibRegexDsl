import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions") version Versions.BEN_MANES_VERSIONS_PLUGIN
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }

    wrapper {
        distributionType = Wrapper.DistributionType.ALL
        gradleVersion = Versions.GRADLE
    }

    // Configuration for gradle-versions-plugin
    withType<DependencyUpdatesTask> {
        resolutionStrategy {
            componentSelection {
                all {
                    if (setOf(
                            "alpha",
                            "beta",
                            "rc",
                            "preview",
                            "eap",
                            "m1"
                        ).any { candidate.version.contains(it, true) }
                    ) {
                        reject("Non stable")
                    }
                }
            }
        }
    }
}
