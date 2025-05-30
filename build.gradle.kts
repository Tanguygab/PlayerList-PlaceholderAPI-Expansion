plugins {
    kotlin("jvm") version "2.1.21"
    id("com.gradleup.shadow") version "9.0.0-beta15"
}

group = "io.github.tanguygab"
version = "3.1.0"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.viaversion.com")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.viaversion:viaversion-api:4.7.0")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:2.2.0-RC")
}

tasks.shadowJar {
    relocate("org.jetbrains.kotlin", "shadow.kotlin")
    archiveClassifier.set("")
}