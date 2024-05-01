plugins {
    java
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()

    // https://github.com/MinecraftForge/ForgeGradle/issues/919
    maven("https://maven.minecraftforge.net")
}

dependencies {
    implementation("me.modmuss50:mod-publish-plugin:0.5.1")

    // https://github.com/MinecraftForge/ForgeGradle/issues/919
    // The issue still happens because FART still uses outdated srgutils version
    implementation("net.minecraftforge:srgutils:0.5.10")
}
