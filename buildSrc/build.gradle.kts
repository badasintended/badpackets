plugins {
    java
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://jitpack.io")

    // https://github.com/MinecraftForge/ForgeGradle/issues/919
    maven("https://maven.minecraftforge.net")
}

dependencies {
    implementation("com.modrinth.minotaur:Minotaur:2.2.1")
    implementation("com.github.deirn:CurseForgeGradle:c693018f92")

    // https://github.com/MinecraftForge/ForgeGradle/issues/919
    // The issue still happens because FART still uses outdated srgutils version
    implementation("net.minecraftforge:srgutils:0.5.3")
}
