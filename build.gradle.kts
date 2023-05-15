plugins {
    kotlin("jvm") version "1.8.20"
    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("xyz.jpenilla.run-paper") version "2.0.1"
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
    // For commodore
    maven(url = "https://libraries.minecraft.net/")
}

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
    implementation(kotlin("reflect"))
    implementation("me.lucko:commodore:2.2")
    // compileOnly(files("lib/PocketLifeCore-1.0-SNAPSHOT.jar"))
    // compileOnly("games.pocketlife.play:PocketLifeCore:1.0-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
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
        fun reloc(pkg: String) = relocate(pkg, "dev.sky_lock.dependency.$pkg")

        relocate("me.lucko.commodore", "dev.sky_lock.pocketlifevehicle.commodore")
    }
}