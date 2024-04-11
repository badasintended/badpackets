plugins {
    java
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.modrinth.minotaur:Minotaur:2.8.7")
    implementation("net.darkhax.curseforgegradle:CurseForgeGradle:1.1.18")
}
