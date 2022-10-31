plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.51.0"
}

include(":library", ":sample")
rootProject.name = "klibregexdsl-root"

project(":library").name = "klibregexdsl"
