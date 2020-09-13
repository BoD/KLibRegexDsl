plugins {
    kotlin("jvm") version Versions.KOTLIN
}

apply(plugin = "java")

dependencies {
    implementation(kotlin("stdlib", Versions.KOTLIN))
    implementation(project(":library"))
}
