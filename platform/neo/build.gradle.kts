plugins {
    idea
    id("net.neoforged.moddev") version "2.0.110"
}

setupPlatform()

neoForge {
    version = rootProp["neo"]

    runs {
        create("server") { server() }
        create("client") {
            client()
            programArguments.addAll("--username", "A")
        }

        configureEach {
            gameDirectory = file("run/${namer.determineName(this)}")
        }

        mods {
            create("badpackets") {
                sourceSet(sourceSets["main"])
                sourceSet(sourceSets["testmod"])
            }
        }
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
