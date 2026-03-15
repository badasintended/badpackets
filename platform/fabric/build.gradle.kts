import org.gradle.kotlin.dsl.sourcesJar

plugins {
    id("net.fabricmc.fabric-loom")
}

setupPlatform()

dependencies {
    minecraft("com.mojang:minecraft:${rootProp["minecraft"]}")

    implementation("net.fabricmc:fabric-loader:${rootProp["fabricLoader"]}")
    implementation("net.fabricmc.fabric-api:fabric-api:${rootProp["fabricApi"]}")
}

loom {
    runs.forEach {
        it.isIdeConfigGenerated = true
    }
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

afterEvaluate {
    val jar = tasks.jar.get()
    val sourcesJar = tasks.sourcesJar.get()

    upload {
        curseforge(jar)
        modrinth(jar)
        maven(jar, sourcesJar)
    }
}
