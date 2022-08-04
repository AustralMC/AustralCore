group = "net.australmc"
version = "1.0.0"
description = "AustralCore"
java.sourceCompatibility = JavaVersion.VERSION_17

object Versions {
    const val KOTLIN = "1.7.10"
    const val HIKARI = "5.0.1"
    const val REFLECTIONS = "0.10.2"
    const val COMMONS_LANG = "3.12.0"
    const val COMMONS_TEXT = "1.9"
    const val LOMBOK = "1.18.24"

    const val PAPER = "1.19.1-R0.1-SNAPSHOT"
    const val VAULT = "1.7"
    const val CANVAS = "1.7.0-SNAPSHOT"
}

plugins {
    java
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()

    mavenCentral()

    maven { url = uri("https://jitpack.io/") }

    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    // General
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}")
    api("com.zaxxer:HikariCP:${Versions.HIKARI}")
    api("org.reflections:reflections:${Versions.REFLECTIONS}")
    api("org.apache.commons:commons-lang3:${Versions.COMMONS_LANG}")
    api("org.apache.commons:commons-text:${Versions.COMMONS_TEXT}")
    compileOnlyApi("org.projectlombok:lombok:${Versions.LOMBOK}")

    // Minecraft-related
    api("org.ipvp:canvas:${Versions.CANVAS}")
    compileOnlyApi("io.papermc.paper:paper-api:${Versions.PAPER}")
    compileOnlyApi("com.github.MilkBowl:VaultAPI:${Versions.VAULT}")

}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
