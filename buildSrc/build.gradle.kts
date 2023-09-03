plugins {
    java
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.fabricmc.net")
    maven("https://maven.neoforged.net/releases")
    maven("https://repo.spongepowered.org/repository/maven-public")
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.modrinth.minotaur:Minotaur:2.2.1")
    implementation("com.github.deirn:CurseForgeGradle:c693018f92")

    implementation("org.spongepowered:vanillagradle:0.2.1-SNAPSHOT")
    implementation("fabric-loom:fabric-loom.gradle.plugin:1.3.+")
    implementation("net.neoforged.gradle:net.neoforged.gradle.gradle.plugin:[6.0.13,6.2)")
    implementation("org.spongepowered:mixingradle:0.7.+")

    // https://github.com/MinecraftForge/ForgeGradle/issues/919
    // The issue still happens because FART still uses outdated srgutils version
    implementation("net.minecraftforge:srgutils:0.5.3")
}
