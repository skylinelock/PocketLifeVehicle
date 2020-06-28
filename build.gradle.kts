plugins {
    java
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "dev.sky_lock"
version = "1.3.6"

val sourceCompatibility = "1.8"

extra["kotlin_version"] = "1.3.72"

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://jitpack.io")
    maven(url = "https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://repo.dmulloy2.net/nexus/repository/public/")
    maven(url = "https://repo.codemc.org/repository/maven-public")
    maven(url = "https://libraries.minecraft.net/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("me.lucko:commodore:1.5")
    implementation("org.bstats:bstats-bukkit:1.7")
    compileOnly("com.life.pocket:PocketLifeCore:1.0-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper-api:1.14.4-R0.1-SNAPSHOT")
    compileOnly("org.bukkit:craftbukkit:1.14.4-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.5.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    shadowJar {
        archiveBaseName.set("PocketLifeVehicle")
        archiveClassifier.set("")
        relocate("org.bstats", "dev.sky_lock.pocketlifevehicle")
        relocate("me.lucko.commodore", "dev.sky_lock.pocketlifevehicle.commodore")
    }
}