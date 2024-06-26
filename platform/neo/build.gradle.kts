plugins {
    idea
    id("net.neoforged.gradle.userdev") version "7.0.142"
}

setupPlatform()

dependencies {
    implementation("net.neoforged:neoforge:${rootProp["neo"]}")
}

runs {
    configureEach {
        modSource(sourceSets["main"])
        modSource(sourceSets["testmod"])
    }
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/neoforge.mods.toml") {
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
