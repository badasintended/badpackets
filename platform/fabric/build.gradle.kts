plugins {
    id("fabric-loom")
}

setupPlatform()

dependencies {
    minecraft("com.mojang:minecraft:${rootProp["minecraft"]}")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:${rootProp["fabricLoader"]}")
}

loom {
    mixin.defaultRefmapName.set("badpackets.refmap.json")
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
