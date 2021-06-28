import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "de.schoschi"
version = "1.1"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

val minecraft_version: String by project
val kspigot_version: String by project

dependencies {
    // Spigot Dependency
    // https://www.spigotmc.org/wiki/buildtools/
    compileOnly("io.papermc.paper:paper-api:$minecraft_version-R0.1-SNAPSHOT")

    // KSpigot dependency
    implementation("net.axay", "kspigot", kspigot_version)

    // You can add Dependencies here
}

tasks {
    jar {
        // Disabled, because we use the shadowJar task for building our jar
        enabled = false
    }
    build {
        finalizedBy(shadowJar)
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}
