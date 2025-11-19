plugins {
    id("fabric-loom") version "1.13.4"
}

setupPlatform()

dependencies {
    minecraft("com.mojang:minecraft:${rootProp["minecraft"]}")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:${rootProp["fabricLoader"]}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${rootProp["fabricApi"]}")
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
    val remapJar = tasks.remapJar.get()
    val remapSourcesJar = tasks.remapSourcesJar.get()

    upload {
        curseforge(remapJar)
        modrinth(remapJar)
        maven(remapJar, remapSourcesJar)
    }
}
