plugins {
    kotlin("jvm") version "2.0.20"
    id("io.papermc.paperweight.userdev") version "1.7.2"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.sky_lock"
version = "2.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenLocal {
        content {
            includeGroup("org.bukkit")
            includeGroup("games.pocketlife.play")
        }
    }
    mavenCentral()
    // For protocol-lib
    maven(url = "https://repo.dmulloy2.net/nexus/repository/public/")
    // For CommandAPI
    maven(url = "https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    implementation(kotlin("reflect"))
    implementation("dev.jorel:commandapi-bukkit-shade:9.5.3")
    implementation("dev.jorel:commandapi-bukkit-kotlin:9.5.3")
    // compileOnly("games.pocketlife.play:PocketLifeCore:1.0-SNAPSHOT")
    implementation("com.github.retrooper.packetevents:spigot:2.3.0")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    shadowJar {
        relocate("dev.jorel.commandapi", "dev.sky_lock.pocketlife.commandapi")
    }
}